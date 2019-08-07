package com.ekar.counter.service;

import com.ekar.counter.entity.Counter;
import com.ekar.counter.repository.CounterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterProducer implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(CounterProducer.class);
    private static final int MAX_VALUE = 100;
    private static final int TIME_OUT_IDLE = 3;
    private static final int TIME_OUT = 1;
    private AtomicInteger atomicInteger;

    private CounterRepository counterRepository;
    private ExecutorService executorService;

    public CounterProducer(AtomicInteger atomicInteger, CounterRepository counterRepository,   ExecutorService executorService) {
        this.atomicInteger = atomicInteger;
        this.counterRepository = counterRepository;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        try {
            while (atomicInteger.get() > MAX_VALUE) {
                log.info("Producer is waiting...");
                TimeUnit.SECONDS.sleep(TIME_OUT_IDLE);
            }
            while (atomicInteger.get() < MAX_VALUE) {
                int currentValue = atomicInteger.incrementAndGet();
                if (currentValue > MAX_VALUE) {
                    log.warn("Exiting value is beyond MAX");
                    return;
                }
                log.info("Producer produced - " + currentValue);
                TimeUnit.SECONDS.sleep(TIME_OUT);
                if (currentValue == MAX_VALUE) {
                    log.info("Persisting to Database -- Max Value Produced -- " + currentValue);
                    persist();
                    return;
                }
            }
        } catch (InterruptedException e) {
            log.error("Error on producing .", e);
        }
    }

    private void persist() {
        executorService.execute(() -> {
            Counter counter = new Counter();
            counter.setThreadName("Counter Producer");
            counter.setDateCreated(new Date());
            if (counterRepository != null) {
                counterRepository.save(counter);
            }
        });
    }
}
