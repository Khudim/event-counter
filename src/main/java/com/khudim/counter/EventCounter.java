package com.khudim.counter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventCounter implements IEventCounter {

    private static final int MINUTE = 60;
    private static final int HOUR = 60 * 60;
    private static final int DAY = 60 * 60 * 24;

    private final Map<SystemEvent, Counter> eventCounter = new ConcurrentHashMap<>();

    public void register(SystemEvent event) {
        Counter counter = eventCounter.get(event);
        if (counter == null) {
            synchronized (this) {
                counter = eventCounter.get(event);
                if (counter == null) {
                    counter = new Counter();
                    eventCounter.put(event, counter);
                }
            }
        }
        counter.incrementCounter();
    }

    public long getEventCountForLastMinute(SystemEvent event) {
        return getEventCount(event, MINUTE);
    }

    public long getEventCountForLastHour(SystemEvent event) {
        return getEventCount(event, HOUR);
    }

    public long getEventCountForLastDay(SystemEvent event) {
        return getEventCount(event, DAY);
    }

    private long getEventCount(SystemEvent event, int time) {
        if (event == null || eventCounter.get(event) == null) {
            return 0;
        }
        return eventCounter.get(event).getCountForTime(time);
    }
}
