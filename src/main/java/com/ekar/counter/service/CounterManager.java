package com.ekar.counter.service;

import com.ekar.counter.repository.CounterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterManager {

    private final static Logger log = LoggerFactory.getLogger(CounterManager.class);
    private static final int INITIAL_VALUE = 50;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private AtomicInteger sharedCounter = new AtomicInteger(INITIAL_VALUE);

    @Autowired
    private CounterRepository counterRepository;

    public void executeConsumerAndProducer(int producerCount, int consumerCount) {
        log.info("Initial Value: {}", sharedCounter.get());
        executorService.execute(() -> {
            List<Runnable> tasks = new ArrayList<>();
            for (int x = 0; x < producerCount; x++) {
                tasks.add(new CounterProducer(sharedCounter, counterRepository , executorService));
            }
            for (int x = 0; x < consumerCount; x++) {
                tasks.add(new CounterConsumer(sharedCounter, counterRepository, executorService));
            }
            execute(tasks);
        });
    }

    private void execute(List<Runnable> tasks) {
        Collections.shuffle(tasks);
        log.info("Executing...");
        for (Runnable task : tasks) {
            executorService.execute(task);
        }
    }

    public void updateValue(int newValue) {
        log.info("Updating Value from: {} , to:{} ",sharedCounter.get(), newValue);
        executorService.execute(() -> {
            sharedCounter.set(newValue);
        });
    }
}
