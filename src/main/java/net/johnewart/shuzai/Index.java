package net.johnewart.shuzai;

import java.util.*;

public class Index<T> implements Iterable<T> {
    private final TreeSet<T> index;

    public Index() {
        index = new TreeSet<>();
    }

    public Index(List<T> values) {
        index = new TreeSet<>();
        index.addAll(values);
    }


    /**
     * Merge two indices.
     * @param otherIndex
     * @return true if index modified, false otherwise
     */
    public boolean merge(Index<T> otherIndex) {
        return index.addAll(otherIndex.index);
    }

    @Override
    public Iterator<T> iterator() {
        return index.iterator();
    }

    public void addAll(Collection<T> values) {
        index.addAll(values);
    }

    public void add(T value) {
        index.add(value);
    }

    public List<T> asList() {
        List<T> results = new LinkedList<>();

        for(T v : index) {
            results.add(v);
        }

        return results;
    }

    public static <T> Index<T> of(T... things) {
        Index<T> idx = new Index<>();
        for(T t : things) {
            idx.add(t);
        }
        return idx;
    }

    public T first() {
        try {
            return index.first();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public T last() {
        try {
            return index.last();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Return a subset [from -> to
     * @param from
     * @param to
     * @return A sorted subset
     */
    public SortedSet<T> subSet(T from, T to, boolean includeEnd) {
        return index.subSet(from, true, to, includeEnd);
    }
}
