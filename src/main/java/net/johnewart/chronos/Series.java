package net.johnewart.chronos;

import java.util.Map;

public interface Series<I,V> {
    Index<I> index();
    Map<I, V> data();
    void expand(Index<I> newIndex);
}
