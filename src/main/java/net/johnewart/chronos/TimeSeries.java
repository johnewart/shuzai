package net.johnewart.chronos;

import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
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
public class TimeSeries<E extends Number> implements Series<DateTime, E> {
    private final TreeMap<DateTime, E> timeValues;
    private final Index<DateTime> index;

    public TimeSeries(List<E> elements, List<DateTime> timeIndices) {
        if (elements.size() != timeIndices.size()) {
            throw new IllegalArgumentException("Elements and time indices must be of the same size!");
        }

        timeValues = new TreeMap<>();
        index = new Index<>();

        for(int i = 0; i < elements.size(); i++) {
            timeValues.put(timeIndices.get(i), elements.get(i));
            index.add(timeIndices.get(i));
        }
    }

    public TimeSeries(E defaultValue, TimeRange range) {
        timeValues = new TreeMap<>();
        index = new Index<>();
        for(int i = 0; i < range.size(); i++) {
            timeValues.put(range.get(i), defaultValue);
            index.add(range.get(i));
        }
    }

    public void merge(TimeSeries<E> otherTimeSeries) {
        this.merge(otherTimeSeries, FillMethod.LINEAR);
    }

    public void merge(TimeSeries<E> otherTimeSeries, FillMethod fillMethod) {
        /*if (fillMethod == FillMethod.LINEAR) {
            // Our time steps
            double x[] = { 1.0, 2.0, 3.0   };
            double y[] = { 1.0, -1.0, 2.0};
            UnivariateInterpolator interpolator = new SplineInterpolator();
            UnivariateFunction function = interpolator.interpolate(x, y);
            double interpolationX = 0.5;
            double interpolatedY = function.value(interpolationX);
            System.out.println("f(" + interpolationX + ") = " + interpolatedY);
        }*/

    }

    /**
     * Create a copy of the time series with a new frequency, summing all points into their proper buckets
     * @param frequency
     * @return A new TimeSeries with the specified frequency
     */
    // TODO: One interval case
    public TimeSeries<E> toFrequency(Frequency frequency) {
        DateTime start = index.first();
        DateTime end = index.last();
        List<DateTime> intervals = TimeRange.computeIntervals(start, end, frequency);
        List<E> values = new ArrayList<E>(intervals.size());

        Iterator<DateTime> it = intervals.iterator();
        DateTime previous = null;

        while(it.hasNext()) {
            DateTime current = it.next();
            if(previous != null) {
                values.add(sumRegion(previous, current, false));

                if(!it.hasNext() && current.isBefore(end)) {
                    // Sum up to the end
                    values.add(sumRegion(current, end, true));
                }

            }
            previous = current;
        }

        return new TimeSeries<>(values, intervals);
    }

    private E sumRegion(DateTime start, DateTime end, boolean includeEnd) {
        Set<DateTime> subSet = index.subSet(start, end, includeEnd);
        BigDecimal sum = new BigDecimal(0);
        for(DateTime dt : subSet) {
            sum = sum.add(new BigDecimal(timeValues.get(dt).doubleValue()));
        }
        return (E) sum;
    }

    public List<E> values() {
        return ImmutableList.copyOf(timeValues.values());
    }

    public Index<DateTime> index() {
       return new Index<DateTime>(ImmutableList.copyOf(timeValues.keySet()));
    }

    @Override
    public Map<DateTime, E> data() {
        return timeValues;
    }

    @Override
    public void expand(Index<DateTime> newIndex) {
        index.merge(newIndex);
    }


}

