package com.han.towersofivory.network.businesslayer.threading;

public class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
    private final int priority;
    private final Runnable task;

    public PriorityRunnable(int priority, Runnable task) {
        this.priority = priority;
        this.task = task;
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public int compareTo(PriorityRunnable o) {
        return Integer.compare(priority, o.priority);
    }

    public int getPriority() {
        return priority;
    }
}