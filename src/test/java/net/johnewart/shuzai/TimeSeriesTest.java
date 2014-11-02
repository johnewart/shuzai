package net.johnewart.shuzai;

import com.google.common.base.Stopwatch;
import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TimeSeriesTest {
    @Test
    public void testTimeSeries() {
        List<Float> elements = new LinkedList<>();
        elements.add(0.1F);
        elements.add(0.5F);
        elements.add(1.2F);
        elements.add(0.123456F);
        elements.add(1.3F);
        elements.add(3.4F);
        elements.add(10.5F);

        List<DateTime> times = new LinkedList<>();
        times.add(new DateTime(2013, 1, 1, 0, 1));
        times.add(new DateTime(2013, 1, 1, 0, 2));
        times.add(new DateTime(2013, 1, 1, 0, 3));
        times.add(new DateTime(2013, 1, 1, 0, 4));
        times.add(new DateTime(2013, 1, 1, 0, 5));
        times.add(new DateTime(2013, 1, 1, 0, 10));
        times.add(new DateTime(2013, 1, 1, 0, 30));

        TimeSeries timeSeries = new TimeSeries(elements, times);
        assertEquals(timeSeries.values().size(), times.size());

        TimeSeries consolidated = timeSeries.downSample(new Frequency(20, TimeUnit.MINUTES), SampleMethod.SUM);
        TimeSeries averaged = timeSeries.downSample(new Frequency(5, TimeUnit.MINUTES), SampleMethod.MEAN);

        List<DateTime> othertimes = new LinkedList<>();
        othertimes.add(new DateTime(2013, 1, 1, 11, 0));
        othertimes.add(new DateTime(2013, 1, 1, 13, 0));
        othertimes.add(new DateTime(2013, 1, 1, 17, 0));

        List<Float> otherelements = new LinkedList<>();
        otherelements.add(2.5F);
        otherelements.add(4.2F);
        otherelements.add(8.4F);

        TimeSeries otherTimeSeries = new TimeSeries(otherelements, othertimes);

        DataFrame<DateTime, BigDecimal> df = new DataFrame<>();
        df.add("Series 1", timeSeries);
        df.add("Series 2", otherTimeSeries);
        df.add("Summed", consolidated);
        df.add("Averaged", averaged);

        df.print();
        df.backFill();
        df.print();
        df.forwardFill();
        df.print();
        //Float value = timeSeries.fetchValue(new DateTime(2013, 1, 4, 0, 0));
    }

    @Test
    public void testBigTimeSeries() {
        Stopwatch stopWatch = Stopwatch.createStarted();
        int size = 1000000;
        List<Float> elements = new ArrayList<>(size);
        List<DateTime> times = new ArrayList<>(size);
        //List<Float> elements = new LinkedList<>();
        //List<DateTime> times = new LinkedList<>();

        DateTime startTime = new DateTime(2014, 1, 1, 0, 0);
        Random random = new Random();
        System.err.println("Random start: " + stopWatch.toString());
        for(int i = 0; i < size; i++) {
            DateTime time = startTime.plusMinutes(i);
            times.add(time);
            elements.add(random.nextFloat());
        }
        System.err.println("Random generated: " + stopWatch.toString());


        TimeSeries timeSeries = new TimeSeries(elements, times);
        assertEquals(timeSeries.values().size(), times.size());

        System.err.println("Insert: " + stopWatch.toString());
        TimeSeries consolidated = timeSeries.downSample(new Frequency(1, TimeUnit.HOURS), SampleMethod.SUM);
        System.err.println("Sum: " + stopWatch.toString());
        TimeSeries averaged = timeSeries.downSample(new Frequency(1, TimeUnit.HOURS), SampleMethod.MEAN);
        System.err.println("Mean: " + stopWatch.toString());


        DataFrame<DateTime, BigDecimal> df = new DataFrame<>();
        //df.add("Series 1", timeSeries);
        //System.err.println("Add: " + stopWatch.toString());
        df.add("Summed", consolidated);
        System.err.println("Add: " + stopWatch.toString());
        df.add("Averaged", averaged);
        System.err.println("Add: " + stopWatch.toString());

        df.print();
        //Float value = timeSeries.fetchValue(new DateTime(2013, 1, 4, 0, 0));
        stopWatch.stop();
        System.err.println("Took: " + stopWatch.toString());
    }
}
