package net.johnewart.shuzai;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class DifferentialTimeSeries extends TimeSeries {
    private static final Logger LOG = LoggerFactory.getLogger(DifferentialTimeSeries.class);

    private BigDecimal lastValue;

    public DifferentialTimeSeries() {
        super();
        this.lastValue = null;
    }

    public DifferentialTimeSeries(List<? extends Number> elements, List<DateTime> timeIndices) {
        super(elements, timeIndices);
    }


    public void add(DateTime time, BigDecimal value) {
        BigDecimal newValue;

        if (lastValue == null) {
            newValue = value;
        } else {
            newValue = value.subtract(lastValue);
        }

        lastValue = value;

        super.add(time, newValue);
    }

    public void add(DateTime time, long value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, Long value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, int value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, Integer value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, double value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, Double value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, float value) {
        this.add(time, new BigDecimal(value));
    }

    public void add(DateTime time, Float value) {
        this.add(time, new BigDecimal(value));
    }
}
