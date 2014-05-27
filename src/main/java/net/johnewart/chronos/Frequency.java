package net.johnewart.chronos;

import java.util.concurrent.TimeUnit;

public class Frequency {
    public final long offset;
    public final TimeUnit timeUnit;

    public Frequency(long offset, TimeUnit timeUnit) {
        this.offset = offset;
        this.timeUnit = timeUnit;
    }

    public static Frequency of(long offset, TimeUnit timeUnit) {
        return new Frequency(offset, timeUnit);
    }

    public long milliseconds() {
        return timeUnit.toMillis(offset);
    }
}
