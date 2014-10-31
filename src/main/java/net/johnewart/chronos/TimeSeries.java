package net.johnewart.chronos;

import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class TimeSeries implements Series<DateTime, BigDecimal> {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeries.class);

    private final TreeMap<DateTime, BigDecimal> timeValues;
    private final Index<DateTime> index;

    public TimeSeries() {
        this.index = new Index<>();
        this.timeValues = new TreeMap<>();
    }

    public TimeSeries(List<? extends Number> elements, List<DateTime> timeIndices) {
        if (elements.getClass() == LinkedList.class || timeIndices.getClass() == LinkedList.class) {
            LOG.warn("LinkedList being used for elements or time data, performance will be degraded.");
        }

        if (elements.size() != timeIndices.size()) {
            throw new IllegalArgumentException("Elements and time indices must be of the same size!");
        }

        timeValues = new TreeMap<>();
        index = new Index<>();
        index.addAll(timeIndices);

        Iterator elementIterator = elements.iterator();
        Iterator timeIterator = timeIndices.iterator();

        while(elementIterator.hasNext() && timeIterator.hasNext()) {
            DateTime time = (DateTime) timeIterator.next();
            Number n = (Number) elementIterator.next();
            BigDecimal value =  new BigDecimal(n.toString());
            timeValues.put(time, value);
        }
    }

    public TimeSeries(Number defaultValue, TimeRange range) {
        timeValues = new TreeMap<>();
        index = new Index<>();
        for(int i = 0; i < range.size(); i++) {
            timeValues.put(range.get(i), new BigDecimal(defaultValue.toString()));
            index.add(range.get(i));
        }
    }

    public void add(DateTime time, BigDecimal value) {
        timeValues.put(time, value);
        index.add(time);
    }


    /**
     * Create a copy of the time series with a new frequency, summing all points into their proper buckets
     * @param frequency The frequency to take samples (i.e every 5 minutes, 10 seconds, etc.)
     * @param sampleMethod The mechanism to use when taking samples (sum, mean, etc.)
     * @return A new TimeSeries with the specified frequency
     */
    // TODO: One interval case
    public TimeSeries downSample(Frequency frequency, SampleMethod sampleMethod) {
        DateTime start = index.first();
        DateTime end = index.last();
        return downSampleToTimeWindow(start, end, frequency, sampleMethod);
    }

    /**
     * Downsample the data while fitting to a specific time window. Non-existant data will be filled in as zero
     * TODO: Make this use NaN instead of zero for empty areas
     * @param start The start of the time window desired
     * @param end The end of the time window desired, inclusive
     * @param frequency How often to take samples
     * @param sampleMethod How to interpolate the data
     * @return A new TimeSeries object with the downsampled data
     */
    public TimeSeries downSampleToTimeWindow(DateTime start, DateTime end, Frequency frequency, SampleMethod sampleMethod) {
        // Reset start to land on a time window
        int offset;
        switch(frequency.timeUnit) {
            case MILLISECONDS:
                // i.e at 2s 502ms, if frequency is every 500ms, go to 2s 500ms
                offset = start.getMillisOfSecond() % frequency.offset;
                start = start.minusMillis(offset);
                break;
            case SECONDS:
                // i.e at 5m 24s if frequency is every 10s, go back to 5m 20s
                offset = start.getSecondOfMinute() % frequency.offset;
                start = start.minusSeconds(offset);
                break;
            case MINUTES:
                offset = start.getMinuteOfHour() % frequency.offset;
                start = start.minusMinutes(offset);
                break;
            case HOURS:
                offset = start.getHourOfDay() % frequency.offset;
                start = start.minusHours(offset);
                break;
            case DAYS:
                offset = start.getDayOfYear() % frequency.offset;
                start = start.minusDays(offset);
                break;
            default:
                throw new IllegalArgumentException("Unsupported time unit!");
        }

        List<DateTime> intervals = TimeRange.computeIntervals(start, end, frequency);
        List<BigDecimal> values = new ArrayList<>(intervals.size());


        if(intervals.size() == 1) {
            // Time window bigger than data collected, only one point in it, add all the data in the window
            values.add(sampleRegion(start, end, true, sampleMethod));
        } else {
            // More than one interval, do this!
            Iterator<DateTime> it = intervals.iterator();
            DateTime previous = null;

            while(it.hasNext()) {
                DateTime current = it.next();
                if(previous != null) {
                    values.add(sampleRegion(previous, current, false, sampleMethod));

                    if(!it.hasNext() && (current.isBefore(end) || current.isEqual(end))) {
                        // Sum up to the end
                        values.add(sampleRegion(current, end, true, sampleMethod));
                    }

                }
                previous = current;
            }
        }

        return new TimeSeries(values, intervals);
    }

    private BigDecimal sampleRegion(DateTime start, DateTime end, boolean includeEnd, SampleMethod sampleMethod) {
        Set<DateTime> region = index.subSet(start, end, includeEnd);
        switch(sampleMethod) {
            case SUM:
                return sumRegion(region);
            case MEAN:
                return meanRegion(region);
            default:
                throw new IllegalArgumentException("Unknown sample method");
        }
    }

    private BigDecimal meanRegion(Set<DateTime> region) {
        if (region.size() > 0) {
            BigDecimal sum = sumRegion(region);
            BigDecimal size = new BigDecimal(region.size());
            return sum.divide(size, 40, RoundingMode.HALF_UP);
        } else {
            return new BigDecimal(0);
        }
    }

    private BigDecimal sumRegion(Set<DateTime> region) {
        BigDecimal sum = new BigDecimal(0);
        for(DateTime dt : region) {
            sum = sum.add(new BigDecimal(timeValues.get(dt).toString()));
        }
        return sum;
    }

    public List<BigDecimal> values() {
        return ImmutableList.copyOf(timeValues.values());
    }

    public Index<DateTime> index() {
       return new Index<DateTime>(ImmutableList.copyOf(timeValues.keySet()));
    }

    @Override
    public Map<DateTime, BigDecimal> data() {
        return timeValues;
    }

    @Override
    public void expand(Index<DateTime> newIndex) {
        index.merge(newIndex);
    }

    @Override
    public BigDecimal lastValue() {
        DateTime lastIndex = index.last();
        if(lastIndex != null) {
            return timeValues.get(lastIndex);
        } else {
            return null;
        }
    }

    public Map<DateTime, BigDecimal> toMap() {
        HashMap<DateTime, BigDecimal> map = new HashMap<>();
        for(DateTime dateTime : index) {
            map.put(dateTime, timeValues.get(dateTime));
        }
        return map;
    }

    /**
     * Sum all the elements in the time-series (i.e downsample with sum across entire time
     * slice)
     * @return The sum of all elements
     */
    public BigDecimal sum() {
        return sampleRegion(index.first(), index.last(), true, SampleMethod.SUM);
    }

    /**
     * Compute the mean of all elements in the time-series
     * @return The mean of all elements
     */
    public BigDecimal mean() {
        return sampleRegion(index.first(), index.last(), true, SampleMethod.MEAN);
    }

}

