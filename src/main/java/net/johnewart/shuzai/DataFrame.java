package net.johnewart.shuzai;

import java.io.PrintStream;
import java.util.*;

// TODO: Make sure we copy instead of point
public class DataFrame<I, V> {

    private final Index<I> index;
    private final Map<String, Map<I,V>> valuesMap;

    public DataFrame() {
        this.index = new Index<>();
        this.valuesMap = new HashMap<>();
    }

    public DataFrame(List<I> indexValues) {
        this.index = new Index<>(indexValues);
        this.valuesMap = new HashMap<>();
    }

    public DataFrame(List<I> index, Map<String, Series<I, V>> values) {
        this(index);
        add(values);
    }

    public void add(String key, Series<I, V> series) {
        // Merge index
        index.merge(series.index());

        Map<I, V> map = valuesMap.get(key);
        if (map == null) {
            map = new HashMap<>();
        }

        map.putAll(series.data());
        valuesMap.put(key, map);
    }

    public void add(Map<String, Series<I,V>> values) {
        for(String key : values.keySet()) {
            Series<I,V> series = values.get(key);
            this.add(key, series);
        }
    }

    public Index<I> getIndex() {
        return index;
    }

    public Map<String, Map<I, V>> getValuesMap() {
        return valuesMap;
    }

    public void print() {
        PrintStream out = System.out;

        out.print("         ");
        for(String label : valuesMap.keySet()) {
            out.print(label);
        }
        out.println();

        for (I indexValue : index) {
            out.print(indexValue);
            out.print(" | ");
            for(String k : valuesMap.keySet()) {
                out.printf("%10.3f", valuesMap.get(k).get(indexValue));
                out.print(" | ");
            }
            out.println();
        }
    }

    public void forwardFill() {
        for(Map<I, V> m : valuesMap.values()) {
            V lastValue = null;
            for(I indexValue : index) {
                if(m.containsKey(indexValue)) {
                    lastValue = m.get(indexValue);
                }

                if(!m.containsKey(indexValue)) {
                    m.put(indexValue, lastValue);
                }
            }
        }
    }

    public void backFill() {
        for(Map<I, V> m : valuesMap.values()) {
            List<I> backfillKeys = new LinkedList<>();
            for(I indexValue : index) {
                if(!m.containsKey(indexValue)) {
                    backfillKeys.add(indexValue);
                }

                if(m.containsKey(indexValue)) {
                    V goodValue = m.get(indexValue);
                    for(I emptyIndex : backfillKeys) {
                        m.put(emptyIndex, goodValue);
                    }
                    backfillKeys = new LinkedList<>();
                }
            }
        }
    }
}
