package com.khudim.counter;

public interface IEventCounter {

    void register(SystemEvent event);

    long getEventCountForLastMinute(SystemEvent event);

    long getEventCountForLastHour(SystemEvent event);

    long getEventCountForLastDay(SystemEvent event);
}
