package com.ekar.counter.shopify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ShopifyStore {

    @Value("${subdomain}")
    private String subdomain;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    private String storeUrl;

    private String orderDetails;

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    @PostConstruct
    public void init() {
        StringBuilder url = new StringBuilder();
        url.append("https://");
        url.append(subdomain);
        url.append(".myshopify.com/admin/api/2020-01/");
        storeUrl = url.toString();
        StringBuilder details = new StringBuilder();
        details.append("https://");
        details.append(subdomain);
        details.append(".myshopify.com/admin/orders/");
        orderDetails = details.toString();
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomainl(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ShopifyStore{" +
               "subdomain='" + subdomain + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", storeUrl='" + storeUrl + '\'' +
               '}';
    }
}
