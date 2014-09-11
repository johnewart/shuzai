package net.johnewart.chronos;

import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/*
def fetch_dataset_as_timeseries(dataset, time_window, frequency="5min"):
    time_range = date_range(start=time_window.start_time, end=time_window.end_time, freq=frequency)
    # Build a zero-based time-series
    time_series = TimeSeries([0] * len(time_range), index=time_range)

    data_series = fetch_dataset_data(dataset, time_window)
    data_series = data_series.interpolate()
    data_series = data_series.apply(lambda x: convert(x, dataset.stored_unit, dataset.display_unit))
    data_series = data_series.asfreq(frequency, method='pad')

    time_series = time_series.combine_first(data_series)

    return time_series


def frame_datasets(datasets, time_window, frequency="5min"):
    d = {}
    for dataset in datasets:
        time_series = fetch_dataset_as_timeseries(dataset, time_window, frequency)
        d[dataset.label] = time_series

    data_frame = DataFrame(d)
    return data_frame
 */
public class TimeSeries implements Series<DateTime, BigDecimal> {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeries.class);

    private final TreeMap<DateTime, BigDecimal> timeValues;
    private final Index<DateTime> index;

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


    /**
     * Create a copy of the time series with a new frequency, summing all points into their proper buckets
     * @param frequency
     * @return A new TimeSeries with the specified frequency
     */
    // TODO: One interval case
    public TimeSeries downSample(Frequency frequency, SampleMethod sampleMethod) {
        DateTime start = index.first();
        DateTime end = index.last();
        // Reset start to beginning of hour
        start = start.minusMinutes(start.getMinuteOfHour());

        List<DateTime> intervals = TimeRange.computeIntervals(start, end, frequency);
        List<BigDecimal> values = new ArrayList<>(intervals.size());

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


}

