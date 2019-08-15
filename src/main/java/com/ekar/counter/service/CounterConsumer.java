package com.ekar.counter.service;

import com.ekar.counter.entity.Counter;
import com.ekar.counter.repository.CounterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterConsumer implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(CounterConsumer.class);
    private static final int MIN_VALUE = 0;
    private static final int TIME_OUT_IDLE = 3;
    private static final int TIME_OUT = 1;
    private static final int RETRY_COUNT = 5;
    private AtomicInteger atomicInteger;
    private CounterRepository counterRepository;
    private ExecutorService executorService;



    public CounterConsumer(AtomicInteger atomicInteger, CounterRepository counterRepository,
                           ExecutorService executorService) {
        this.atomicInteger = atomicInteger;
        this.counterRepository = counterRepository;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        try {
            int retryCount = 0;
            while (atomicInteger.get() < MIN_VALUE) {
                log.info("Consumer is waiting...");
                TimeUnit.SECONDS.sleep(TIME_OUT_IDLE);
                retryCount++;
                if (retryCount == RETRY_COUNT) {
                    return;
                }
            }
            while (atomicInteger.get() > MIN_VALUE) {
                int currentValue = atomicInteger.decrementAndGet();
                if (currentValue < MIN_VALUE) {
                    log.warn("Exiting value is beyond MIN");
                    return;
                }
                log.info("Consumer consumed : {} " , currentValue);
                TimeUnit.SECONDS.sleep(TIME_OUT);
                if (currentValue == MIN_VALUE) {
                    log.info("Persisting to Database -- Min Value Consumed : {} " , currentValue);
                    persist();
                    return;
                }
            }
        } catch (InterruptedException e) {
            log.error("Error on consuming .", e);
        }
    }

    private void persist() {
        executorService.execute(() -> {
            Counter counter = new Counter();
            counter.setThreadName("CounterConsumer");
            counter.setDateCreated(new Date());
            if (counterRepository != null) {
                counterRepository.save(counter);
            }
        });
    }
}
