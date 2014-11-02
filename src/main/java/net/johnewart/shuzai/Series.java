package net.johnewart.shuzai;

import java.util.Map;

public interface Series<I,V> {
    Index<I> index();
    Map<I, V> data();
    void expand(Index<I> newIndex);
    V lastValue();
}
