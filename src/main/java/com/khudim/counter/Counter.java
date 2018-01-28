package com.khudim.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private final long firstIndexTime;
    private final int arraySize = 25_920_000; // 10 months in seconds
    private final int timeStep = 1_000;       // Each array index is 1 second

    private final AtomicInteger[] timeCounter = new AtomicInteger[arraySize];

    public Counter() {
        this.firstIndexTime = System.currentTimeMillis() / timeStep;
    }

    public void incrementCounter() {
        int index = getIndexFromCurrentTime();
        AtomicInteger counter = timeCounter[index];
        if (counter == null) {
            synchronized (this) {
                counter = timeCounter[index];
                if (counter == null) {
                    counter = new AtomicInteger(0);
                    timeCounter[index] = counter;
                }
            }
        }
        counter.incrementAndGet();
    }

    public long getCountForTime(int time) {
        int index = getIndexFromCurrentTime();
        long count = 0;
        for (int i = 0; i < time; i++) {
            if ((index - i) < 0) {
                break;
            }
            AtomicInteger counter = timeCounter[index - i];
            if (counter != null) {
                count += counter.get();
            }
        }
        return count;
    }

    private int getIndexFromCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000 - firstIndexTime);
    }
}
