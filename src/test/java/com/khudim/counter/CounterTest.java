package com.khudim.counter;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CounterTest {

    @Test
    public void shouldIncrement() {
        Counter counter = new Counter();
        counter.incrementCounter();
        assertEquals(1, counter.getCountForTime(500));
    }
}
