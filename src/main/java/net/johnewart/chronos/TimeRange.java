package net.johnewart.chronos;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeRange {
    public final DateTime startTime, endTime;
    private final TimeRangeIndex index;

    public TimeRange(DateTime startTime, DateTime endTime, Frequency frequency) {
        List<DateTime> timeIndices = TimeRange.computeIntervals(startTime, endTime, frequency);
        this.startTime = startTime;
        this.endTime = endTime;
        this.index = new TimeRangeIndex(timeIndices);
    }

    public TimeRange(List<DateTime> timeIndices) {
        this.startTime = timeIndices.get(0);
        this.endTime = timeIndices.get(timeIndices.size() - 1);
        this.index = new TimeRangeIndex(timeIndices);
    }

    public static List<DateTime> computeIntervals(DateTime startTime, DateTime endTime, Frequency frequency) {
        int slices = (int) ((endTime.getMillis() - startTime.getMillis()) / frequency.milliseconds());
        List<DateTime> timeIndices = new ArrayList<>(slices);
        Period period = new Period(frequency.milliseconds());

        for(DateTime time = startTime; time.isBefore(endTime) || time.isEqual(endTime); time = time.plus(period)) {
            timeIndices.add(time);
        }

        return timeIndices;
    }

    public int size() {
        return index.size();
    }

    public DateTime get(int i) {
        return index.get(i);
    }

    /**
     * Pre-compute useful time series bits
     */
    private class TimeRangeIndex {
        final List<Long> indices;

        public TimeRangeIndex(List<DateTime> timeIndices) {
            List<Long> index = new ArrayList<>();
            for(DateTime dt : timeIndices) {
                index.add(dt.getMillis());
            }
            indices = index;
        }

        public int size() {
            return indices.size();
        }

        public DateTime get(int i) {
            return new DateTime(indices.get(i));
        }
    }
}

