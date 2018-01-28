package com.khudim;

import com.khudim.counter.EventCounter;
import com.khudim.counter.IEventCounter;
import com.khudim.counter.SystemEvent;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        IEventCounter eventCounter = new EventCounter();
        eventCounter.register(SystemEvent.SEND_PHOTO);

        System.out.println(eventCounter.getEventCountForLastMinute(SystemEvent.SEND_PHOTO));
        System.out.println(eventCounter.getEventCountForLastHour(SystemEvent.SEND_PHOTO));
        System.out.println(eventCounter.getEventCountForLastDay(SystemEvent.SEND_PHOTO));
    }
}
