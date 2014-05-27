package net.johnewart.chronos;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IndexTest {
    @Test
    public void createIntegerIndexTest() {
        Index<Integer> integerIndex = Index.of(1, 2, 6, 4);
        List<Integer> results = integerIndex.asList();
        assertEquals(1, results.get(0));
        assertEquals(2, results.get(1));
        assertEquals(4, results.get(2));
        assertEquals(6, results.get(3));
    }

    @Test
    public void mergeIntegerIndicesTest() {
        Index<Integer> one = Index.of(1,3,5,7,9);
        Index<Integer> two = Index.of(2,4,6,8);
        one.merge(two);
        List<Integer> list = one.asList();

        int i = 1;
        for(Integer v : list) {
            assertTrue(v == i);
            i++;
        }

        assertEquals(10, i);
    }

    @Test
    public void mergeDateTimeIndicesTest() {
        Index<DateTime> dtIndexOne = Index.of(
                new DateTime(2000, 1, 1, 0, 5),
                new DateTime(2000, 1, 1, 0, 0),
                new DateTime(2000, 1, 1, 0, 2)
        );

        Index<DateTime> dtIndexTwo = Index.of(
                new DateTime(2000, 1, 1, 0, 3),
                new DateTime(2000, 1, 1, 0, 4),
                new DateTime(2000, 1, 1, 0, 6)
        );

        dtIndexOne.merge(dtIndexTwo);
        List<DateTime> list = dtIndexOne.asList();

        DateTime previous = null;
        for(DateTime dt : list) {

            if(previous != null) {
                assertTrue(previous.isBefore(dt));
            }

            previous = dt;
        }
    }

    @Test
    public void verifyNoopMerge() {
        Index<Integer> integerIndex = Index.of(1, 2, 6, 4);
        integerIndex.merge(integerIndex);
        List<Integer> results = integerIndex.asList();
        assertEquals(1, results.get(0));
        assertEquals(2, results.get(1));
        assertEquals(4, results.get(2));
        assertEquals(6, results.get(3));
        assertEquals(4, results.size());
    }

    @Test
    public void verifyIdempotence() {
        Index<Integer> one = Index.of(1,3,5,7,9);
        Index<Integer> two = Index.of(2,4,6,8);
        one.merge(two);
        // Merge a second time, should have no effect
        one.merge(two);
        List<Integer> list = one.asList();

        int i = 1;
        for(Integer v : list) {
            assertTrue(v == i);
            i++;
        }

        assertEquals(10, i);
    }
}

