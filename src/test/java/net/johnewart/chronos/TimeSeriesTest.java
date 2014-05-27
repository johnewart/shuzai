package net.johnewart.chronos;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TimeSeriesTest {
    @Test
    public void testTimeSeries() {
        List<Float> elements = new LinkedList<>();
        elements.add(1.3F);
        elements.add(3.4F);
        elements.add(10.5F);

        List<DateTime> times = new LinkedList<>();
        times.add(new DateTime(2013, 1, 1, 0, 5));
        times.add(new DateTime(2013, 1, 1, 0, 10));
        times.add(new DateTime(2013, 1, 1, 0, 30));

        TimeSeries<Float> timeSeries = new TimeSeries<Float>(elements, times);
        assertEquals(timeSeries.values().size(), times.size());

        TimeSeries<Float> consolidated = timeSeries.toFrequency(new Frequency(20, TimeUnit.MINUTES));

        List<DateTime> othertimes = new LinkedList<>();
        othertimes.add(new DateTime(2013, 1, 1, 11, 0));
        othertimes.add(new DateTime(2013, 1, 1, 13, 0));
        othertimes.add(new DateTime(2013, 1, 1, 17, 0));

        List<Float> otherelements = new LinkedList<>();
        otherelements.add(2.5F);
        otherelements.add(4.2F);
        otherelements.add(8.4F);

        TimeSeries<Float> otherTimeSeries = new TimeSeries<Float>(otherelements, othertimes);

        DataFrame<DateTime, Float> df = new DataFrame<>();
        df.add("Series 1", timeSeries);
        df.add("Series 2", otherTimeSeries);
        df.add("Series 3", consolidated);

        df.print();
        df.fill();
        df.print();
        //Float value = timeSeries.fetchValue(new DateTime(2013, 1, 4, 0, 0));
    }
}
