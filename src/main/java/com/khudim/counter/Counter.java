package com.khudim.counter;

import java.util.concurrent.atomic.LongAdder;

public class Counter {
    private final long firstIndexTime;
    private final int arraySize = 25_920_000; // 10 months in seconds
    private final int timeStep = 1_000;       // Each array index is 1 second

    private final LongAdder[] timeCounter = new LongAdder[arraySize];

    public Counter() {
        this.firstIndexTime = System.currentTimeMillis() / timeStep;
    }

    public void incrementCounter() {
        int index = getIndexFromCurrentTime();
        LongAdder counter = timeCounter[index];
        if (counter == null) {
            synchronized (this) {
                counter = timeCounter[index];
                if (counter == null) {
                    counter = new LongAdder();
                    timeCounter[index] = counter;
                }
            }
        }
        counter.increment();
    }

    public long getCountForTime(int time) {
        int index = getIndexFromCurrentTime();
        long count = 0;
        for (int i = 0; i < time; i++) {
            if ((index - i) < 0) {
                break;
            }
            LongAdder counter = timeCounter[index - i];
            if (counter != null) {
                count += counter.sum();
            }
        }
        return count;
    }

    private int getIndexFromCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000 - firstIndexTime);
    }
}
