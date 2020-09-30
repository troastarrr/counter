package com.ekar.counter.shopify;

import com.bazaarvoice.jolt.JsonUtils;
import com.ekar.counter.controller.CounterController;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class ShopifyRestClient {

    private final static Logger log = LoggerFactory.getLogger(ShopifyRestClient.class);

    private RestTemplate restTemplate = new RestTemplate();


    private ShopifyStore shopifyStore;

    public ShopifyRestClient(@Autowired ShopifyStore shopifyStore) {
        this.shopifyStore = shopifyStore;
    }

    private HttpHeaders createHttpHeaders(String user, String password) {
        String notEncoded = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    public JsonNode consume(HttpMethod method, String action) {
        String theUrl = shopifyStore.getStoreUrl() + "/" + action;
        log.info("***" + shopifyStore.toString());
        try {
            HttpHeaders headers = createHttpHeaders(shopifyStore.getUsername(),shopifyStore.getPassword());
            HttpEntity<String> entity = new HttpEntity("parameters", headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(theUrl, method, entity, JsonNode.class);
            log.info("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
            return response.getBody();
        }
        catch (Exception eek) {
            log.error("** Exception: "+ eek.getMessage());
            return null;
        }

    }


}
