package com.ekar.counter.entity;

import com.opencsv.bean.CsvBindByName;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report  {

    @CsvBindByName(column = "Order Item Id")
    private String orderItemId;

    @CsvBindByName(column = "Seller SKU")
    private String sellerSku;

    @CsvBindByName(column = "Created at")
    private String createdAt;

    @CsvBindByName(column = "Order Number")
    private String orderNumber;

    public String getSellerSku() {
        return sellerSku;
    }

    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public static void main(String[] args) {
        String rts = "Antique,Batangas,Batangas,Benguet,Biliran,Bohol,Bulacan,Bulacan,Bulacan,Bulacan,Capiz,Capiz,Capiz,Cavite,Cavite,Cavite,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Davao de Oro,Davao Del Norte,Davao Del Norte,Davao Del Norte,Davao Del Sur,Davao Del Sur,Davao Del Sur,Davao Del Sur,Davao Del Sur,Ilocos Norte,Ilocos Norte,Iloilo,Iloilo,Isabela,Isabela,Isabela,Isabela,Isabela,Isabela,La Union,Laguna,Laguna,Laguna,Laguna,Laguna,Laguna,Lanao Del Norte,Leyte,Leyte,Leyte,Metro Manila~Caloocan,Metro Manila~Caloocan,Metro Manila~Las Pinas,Metro Manila~Las Pinas,Metro Manila~Makati,Metro Manila~Mandaluyong,Metro Manila~Mandaluyong,Metro Manila~Mandaluyong,Metro Manila~Manila,Metro Manila~Manila,Metro Manila~Manila,Metro Manila~Muntinlupa,Metro Manila~Muntinlupa,Metro Manila~Navotas,Metro Manila~Paranaque,Metro Manila~Paranaque,Metro Manila~Pasay,Metro Manila~Pasig,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Valenzuela,Misamis Oriental,Misamis Oriental,Misamis Oriental,Negros Occidental,Negros Oriental,North Cotabato,North Cotabato,Nueva Ecija,Nueva Ecija,Nueva Ecija,Nueva Ecija,Pampanga,Pampanga,Pampanga,Pampanga,Pampanga,Pampanga,Quezon,Rizal,Rizal,Sorsogon,Sorsogon,South Cotabato,South Cotabato,South Cotabato,Sultan Kudarat,Tarlac,Zamboanga Del Norte,Zamboanga Del Sur,Zamboanga Sibugay,Zamboanga Sibugay";

        List<String> convertedCountriesList = Arrays.asList(StringUtils.splitPreserveAllTokens(rts, ","));

        Map<String,Long> count = new HashMap<>();
        for (String location : convertedCountriesList) {
            if (location.contains("Metro Manila")) {
               location = "Metro Manila";
            }

            if (count.containsKey(location)) {
                long number = count.get(location);
                count.put(location,number+1);
            } else {
                if (location.contains("Metro Manila")) {
                    count.put("Metro Manila",1L);
                } else  {
                    count.put(location,1L);
                }
            }
        }
//        for (Map.Entry<String,Long> cc : count.entrySet()) {
//            System.out.println("cc.getKey() + "," + cc.getValue() );
//        }
    }

}
