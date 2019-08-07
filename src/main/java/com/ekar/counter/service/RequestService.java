package com.ekar.counter.service;

import com.ekar.counter.entity.Request;
import com.ekar.counter.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void save(int producerTasks, int consumerTasks) {
        executorService.execute(() -> {
            Request request = new Request();
            request.setConsumerTasks(consumerTasks);
            request.setProducerTasks(producerTasks);
            request.setDateCreated(new Date());
            requestRepository.save(request);
        });
    }
}
