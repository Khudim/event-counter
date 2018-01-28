package com.khudim;

import com.khudim.counter.EventCounter;
import com.khudim.counter.IEventCounter;
import com.khudim.counter.SystemEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int threadNumber = 10000;

        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
        List<Future> futures = new ArrayList<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber);

        IEventCounter eventCounter = new EventCounter();

        for (int i = 0; i < threadNumber; i++) {
            Future future = executorService.submit(() -> {
                awaitBarrier(cyclicBarrier);
                eventCounter.register(SystemEvent.SEND_PHOTO);
            });
            futures.add(future);
        }
        long start = System.currentTimeMillis();
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println(eventCounter.getEventCountForLastMinute(SystemEvent.SEND_PHOTO));
        System.out.println(eventCounter.getEventCountForLastHour(SystemEvent.SEND_PHOTO));
        System.out.println(eventCounter.getEventCountForLastDay(SystemEvent.SEND_PHOTO));
        executorService.shutdown();
    }

    private static void awaitBarrier(CyclicBarrier cyclicBarrier) {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
