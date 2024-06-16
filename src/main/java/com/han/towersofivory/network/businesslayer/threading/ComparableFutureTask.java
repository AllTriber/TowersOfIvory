package com.han.towersofivory.network.businesslayer.threading;

import java.util.concurrent.FutureTask;

public class ComparableFutureTask<V> extends FutureTask<V> implements Comparable<ComparableFutureTask<V>> {
    private final int priority;

    public ComparableFutureTask(Runnable runnable, V result, int priority) {
        super(runnable, result);
        this.priority = priority;
    }

    @Override
    public int compareTo(ComparableFutureTask<V> o) {
        return Integer.compare(priority, o.priority);
    }
}