package com.ekar.counter.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterProducerTest {

    private CounterProducer classUnderTest;

    @Test
    public void shouldIncrement() {
        AtomicInteger atomicInteger = new AtomicInteger(99);
        classUnderTest = new CounterProducer(atomicInteger, null, Executors.newCachedThreadPool());
        classUnderTest.run();
        Assert.assertEquals(100, atomicInteger.get());
    }

    @Test
    public void shouldDoNothingWhenValueIsGreaterThan100() {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        classUnderTest = new CounterProducer(atomicInteger, null, Executors.newCachedThreadPool());
        classUnderTest.run();
        Assert.assertEquals(100, atomicInteger.get());
    }
}
