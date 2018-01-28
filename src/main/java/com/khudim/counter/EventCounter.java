package com.khudim.counter;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class EventCounter implements IEventCounter {

    private final static int MINUTE = 60;
    private final static int HOUR = 60 * 60;
    private final static int DAY = 60 * 60 * 24;

    private Logger log = Logger.getLogger(EventCounter.class.getName());

    private final Map<SystemEvent, Counter> eventCounter = new ConcurrentHashMap<>();

    {
        for (SystemEvent event : SystemEvent.values()) {
            eventCounter.put(event, new Counter());
        }
    }

    public void register(SystemEvent event) {
        if (event == null) {
            log.warning("Event is null");
            return;
        }
        Counter counter = eventCounter.get(event);
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
            log.warning("Can't get count for event: " + event);
            return 0;
        }
        return eventCounter.get(event).getCountForTime(time);
    }
}
