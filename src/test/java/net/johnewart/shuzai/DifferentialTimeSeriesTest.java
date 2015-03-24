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

// 10, 20, 40, 50, 100 =

public class DifferentialTimeSeriesTest {
    @Test
    public void testDifferentialTimeSeries() {
        List<Float> elements = new LinkedList<>();
        elements.add(1.0F);
        elements.add(2.0F);
        elements.add(4.0F);
        elements.add(10.0F);

        List<DateTime> times = new LinkedList<>();
        times.add(new DateTime(2013, 1, 1, 0, 1));
        times.add(new DateTime(2013, 1, 1, 0, 2));
        times.add(new DateTime(2013, 1, 1, 0, 3));
        times.add(new DateTime(2013, 1, 1, 0, 4));

        DifferentialTimeSeries timeSeries = new DifferentialTimeSeries(elements, times);
        assertEquals(timeSeries.values().size(), times.size());

        assertEquals(timeSeries.values().get(0).floatValue(), 1.0F);
        assertEquals(timeSeries.values().get(1).floatValue(), 1.0F);
        assertEquals(timeSeries.values().get(2).floatValue(), 2.0F);
        assertEquals(timeSeries.values().get(3).floatValue(), 6.0F);
    }

}
