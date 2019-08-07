package com.ekar.counter.controller;

import com.ekar.counter.service.CounterManager;
import com.ekar.counter.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ekar/counter")
public class CounterController {

    private final static Logger log = LoggerFactory.getLogger(CounterController.class);

    private CounterManager counterManager;

    private RequestService requestService;

    public CounterController(@Autowired CounterManager counterManager, @Autowired RequestService requestService) {
        this.counterManager = counterManager;
        this.requestService = requestService;
    }

    @PostMapping(value = "/spawn")
    public ResponseEntity spawnTasks(@RequestParam int producerCount, int consumerCount) {
        log.info("Producer count :{} , Consumer count : {}", producerCount, consumerCount);
        requestService.save(producerCount, consumerCount);
        counterManager.executeConsumerAndProducer(producerCount, consumerCount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity updateValue(@RequestParam int newValue) {
        counterManager.updateValue(newValue);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
