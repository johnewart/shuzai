package net.johnewart.chronos;

import java.util.concurrent.TimeUnit;

public class Frequency {
    public final int offset;
    public final TimeUnit timeUnit;

    public Frequency(int offset, TimeUnit timeUnit) {
        this.offset = offset;
        this.timeUnit = timeUnit;
    }

    public static Frequency of(int offset, TimeUnit timeUnit) {
        return new Frequency(offset, timeUnit);
    }

    public long milliseconds() {
        return timeUnit.toMillis(offset);
    }
}
