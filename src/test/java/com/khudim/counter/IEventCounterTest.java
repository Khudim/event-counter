package com.khudim.counter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IEventCounterTest {

    @Test
    public void shouldCount() {
        IEventCounter counter = new EventCounter();
        counter.register(SystemEvent.SEND_PHOTO);
        assertEquals(1, counter.getEventCountForLastMinute(SystemEvent.SEND_PHOTO));
    }

    @Test
    public void shouldCountCorrect() {
        int numberOfThreads = 10000;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future> futures = new ArrayList<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfThreads);

        IEventCounter eventCounter = new EventCounter();

        for (int i = 0; i < numberOfThreads; i++) {
            Future future = executorService.submit(() -> {
                awaitBarrier(cyclicBarrier);
                eventCounter.register(SystemEvent.SEND_PHOTO);
            });
            futures.add(future);
        }
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();

        assertEquals(numberOfThreads, eventCounter.getEventCountForLastMinute(SystemEvent.SEND_PHOTO));
    }

    private static void awaitBarrier(CyclicBarrier cyclicBarrier) {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
