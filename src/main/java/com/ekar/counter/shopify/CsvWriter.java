package com.ekar.counter.shopify;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {


    private final static Logger log = LoggerFactory.getLogger(ShopifyController.class);

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
//    Shipping Fee

    public static void writeFullfilmentOrders(PrintWriter writer, List<FullfilmentOrder> fullfilmentOrders) {

        try {

            ColumnPositionMappingStrategy<FullfilmentOrder> mapStrategy
                = new ColumnPositionMappingStrategy<>();

            mapStrategy.setType(FullfilmentOrder.class);
            String[] columns = new String[]{"id",
                                            "orderNumber",
                                            "createdDate",
                                            "name",
                                            "contactNo",
                                            "totalAmount",
                                            "trackingNo",
                                            "trackingLink",
                                            "orderDetails",
                                            "address",
                                            "deliveryOptions",
                                            "courier",
                                            "shippingFee"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<FullfilmentOrder> btcsv = new StatefulBeanToCsvBuilder<FullfilmentOrder>(writer)
                .withMappingStrategy(mapStrategy)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

            btcsv.write(fullfilmentOrders);

            String orderName = "Trạng thái";


        } catch (CsvException ex) {

            log.error("Error mapping Bean to CSV", ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("Trạng thái");
    }


}
