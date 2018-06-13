package net.johnewart.shuzai;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class TimeSeriesSink {
    private final TimeSeries timeSeries;
    private final StorageMethod storageMethod;
    private final Frequency frequency;
    private final DateTime startTime;

    public TimeSeriesSink(Frequency frequency, StorageMethod storageMethod) {
        timeSeries = new TimeSeries();
        this.storageMethod = storageMethod;
        this.frequency = frequency;
        this.startTime = DateTime.now();
    }

    public void add(BigDecimal value) {

    }


}
