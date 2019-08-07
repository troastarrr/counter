package com.ekar.counter.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterConsumerTest {

    private CounterConsumer classUnderTest;

    @Test
    public void shouldDecrement() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        classUnderTest = new CounterConsumer(atomicInteger, null, Executors.newCachedThreadPool());
        classUnderTest.run();
        Assert.assertEquals(0, atomicInteger.get());
    }

    @Test
    public void shouldDoNothingWhenValueIsLessthan0() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        classUnderTest = new CounterConsumer(atomicInteger, null, Executors.newCachedThreadPool());
        classUnderTest.run();
        Assert.assertEquals(0, atomicInteger.get());
    }
}
