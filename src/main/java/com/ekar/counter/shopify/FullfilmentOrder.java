package com.ekar.counter.shopify;

public class FullfilmentOrder {

//    ID
//    Order Number
//    Created Date
//    Name
//    Contact No
//    Total Amount
//    Tracking No
//    Tracking Link
//    Order Details
//    Address
//    Delivery Option
//    Courier

    private long id;

    private String orderNumber;

    private String createdDate;

    private String name;

    private String contactNo;

    private String tag;
    private String fulfillmentStatus;
    private double totalAmount;
    private Long trackingNo;
    private String trackingLink = "";
    private String orderDetails;
    private String address;
    private String deliveryOptions;
    private String courier;
    private double shippingFee;
    private String financialStatus;

    public FullfilmentOrder() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(String fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    public String getFinancialStatus() {
        return financialStatus;
    }

    public void setFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(Long trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(String deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    @Override
    public String toString() {
        return "FullfilmentOrder{" +
               "id=" + id +
               ", orderNumber='" + orderNumber + '\'' +
               ", createdDate='" + createdDate + '\'' +
               ", name='" + name + '\'' +
               ", contactNo='" + contactNo + '\'' +
               ", totalAmount=" + totalAmount +
               ", trackingNo=" + trackingNo +
               ", trackingLink='" + trackingLink + '\'' +
               ", orderDetails='" + orderDetails + '\'' +
               ", address='" + address + '\'' +
               ", deliveryOptions='" + deliveryOptions + '\'' +
               ", courier='" + courier + '\'' +
               '}';
    }
}
