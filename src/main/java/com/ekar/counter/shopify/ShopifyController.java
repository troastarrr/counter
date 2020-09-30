package com.ekar.counter.shopify;

import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/shopify" )

public class ShopifyController {

    private final static Logger log = LoggerFactory.getLogger(ShopifyController.class);

    @Autowired
    private ShopifyRestClient shopifyRestClient;

    @Autowired
    private FullfilmentOrderConverter fullfilmentOrderConverter;


    @GetMapping(value = "/fullfilmentOrders" , produces = "text/csv")
    public void get(@RequestParam(value = "since_id", required = false) Long id, HttpServletResponse response) throws Exception {
        log.info("getting fullifilment orders , since_id {}",  id);
        StringBuilder parameter = requestParamBuilder(id);
        JsonNode mainNode = shopifyRestClient.consume(HttpMethod.GET,parameter.toString());
        String dateNow = LocalDateTime.now().toString();
        String fileName = dateNow+".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + fileName + "\"");
        CsvWriter.writeFullfilmentOrders(response.getWriter(), fullfilmentOrderConverter.convertAll(mainNode));
        //return ResponseEntity.status(HttpStatus.OK).body(fullfilmentOrderConverter.convertAll(mainNode));
    }

    @GetMapping(value = "/tracking")
    public String getTrackingNumbers(@RequestParam(value = "since_id", required = false) Long id) {
        log.info("getting tracking numbers..");
        StringBuilder parameter = requestParamBuilder(id);
        JsonNode mainNode = shopifyRestClient.consume(HttpMethod.GET,parameter.toString());
        return fullfilmentOrderConverter.getTrackingNumbers(mainNode);
        //return ResponseEntity.status(HttpStatus.OK).body(fullfilmentOrderConverter.convertAll(mainNode));
    }

    private StringBuilder requestParamBuilder(
        @RequestParam(value = "since_id", required = false) Long id) {
        StringBuilder parameter = new StringBuilder();
        parameter.append("orders.json");
        parameter.append("?");
        parameter.append("status=any");
        parameter.append("&");
        parameter.append("limit=250");

        if (id != null) {
            parameter.append("&");
            parameter.append("since_id=");
            parameter.append(id);
        }
        return parameter;
    }

    @GetMapping(value = "/pisokli")
    public List<String> checkCustomerFromPisokli(@RequestParam(value = "since_id", required = false) Long id) {
        log.info("getting tracking numbers..");
        List<String> pisokli = fullfilmentOrderConverter.pisokliCustomer();
        StringBuilder parameter = requestParamBuilder(id);
        JsonNode mainNode = shopifyRestClient.consume(HttpMethod.GET,parameter.toString());
        List<String> customers = new ArrayList<>();
        List<FullfilmentOrder> fullfilmentOrders = fullfilmentOrderConverter.convertAll(mainNode);
        for (FullfilmentOrder fullfilmentOrder: fullfilmentOrders) {
            customers.add(fullfilmentOrder.getName());
        }
        List<String> same = new ArrayList<>();
        for (String laventes : customers) {
            boolean containsSearchStr = pisokli.stream().anyMatch(laventes::equalsIgnoreCase);
            if (containsSearchStr) {
                same.add(laventes);
            }
        }
        return same;
        //return ResponseEntity.status(HttpStatus.OK).body(fullfilmentOrderConverter.convertAll(mainNode));
    }
}
