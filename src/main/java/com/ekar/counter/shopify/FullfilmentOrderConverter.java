package com.ekar.counter.shopify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FullfilmentOrderConverter {

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

    @Autowired
    private ShopifyStore shopifyStore;

    public static void main(String[] args) throws Exception {
        String dateStr = "2020-03-03T15:18:21+08:00";
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        Date d = null;
        try {
            d = input.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
    }

    public String getTrackingNumbers(JsonNode jsonNode) {
        List<FullfilmentOrder> fullfilmentOrders = convertAll(jsonNode);
        Comparator<FullfilmentOrder> comparator = Comparator.comparing(FullfilmentOrder::getOrderNumber);
        Collections.sort(fullfilmentOrders, comparator);
        List<Long> trackingNos =
            fullfilmentOrders.stream().filter(e -> e.getTrackingNo() != null).map(e -> e.getTrackingNo())
                .collect(Collectors.toList());
        return StringUtils.join(trackingNos, '+');
    }

    public List<FullfilmentOrder> convertAll(JsonNode mainNode) {
        List<FullfilmentOrder> fullfilmentOrders = new ArrayList<>();
        ArrayNode nodes = (ArrayNode) mainNode.get("orders");
        for (final JsonNode node : nodes) {
            FullfilmentOrder fullfilmentOrder = convert(node);
            //temp filter for qf
            if (fullfilmentOrder != null) {
                fullfilmentOrders.add(fullfilmentOrder);
            }
        }
        Comparator<FullfilmentOrder> comparator = Comparator.comparing(FullfilmentOrder::getOrderNumber);
        Collections.sort(fullfilmentOrders, comparator);
        return fullfilmentOrders;
    }

    private FullfilmentOrder convert(JsonNode jsonNode) {
        String financialStatus = jsonNode.get("financial_status").asText();
        if ("voided".equals(financialStatus)) {
            return null;
        }

        FullfilmentOrder fullfilmentOrder = new FullfilmentOrder();
        Long id = jsonNode.get("id").asLong();
        fullfilmentOrder.setId(id);
        String orderNumber = jsonNode.get("name").asText();
        fullfilmentOrder.setOrderNumber(orderNumber);
        Object noteObj = jsonNode.get("note");
        setShippingFee(jsonNode, fullfilmentOrder, noteObj);
        String createdDate = jsonNode.get("created_at").asText();

        fullfilmentOrder.setCreatedDate(createdDate);
        String name = jsonNode.get("billing_address").get("name").asText();
        fullfilmentOrder.setName(name);
        String phone = jsonNode.get("billing_address").get("phone").asText();
        double totalAmount = jsonNode.get("total_price").asDouble();
        fullfilmentOrder.setTotalAmount(totalAmount);
        fullfilmentOrder.setContactNo(phone.replaceAll("[^0-9]", ""));
        buildCompleteAddress(jsonNode, fullfilmentOrder);
        buildDeliveryType(jsonNode, fullfilmentOrder);
        fullfilmentOrder.setOrderDetails(shopifyStore.getOrderDetails() + fullfilmentOrder.getId());
        buildFulfilmentDetails(jsonNode, fullfilmentOrder);

        String tags = jsonNode.get("tags").asText();
        if (StringUtils.equalsIgnoreCase("qf", tags)) {
            fullfilmentOrder.setCourier("XIO");
        } else if (StringUtils.equalsIgnoreCase("lbc", tags)) {
            fullfilmentOrder.setCourier("LBC");
        } else if (StringUtils.equalsIgnoreCase("grab", tags)) {
            fullfilmentOrder.setCourier("GRAB");
        } else if (StringUtils.equalsIgnoreCase("LZD", tags)) {
            fullfilmentOrder.setCourier("LZD");
        } else {
            fullfilmentOrder.setCourier(tags);
        }
        if (StringUtils.isEmpty(fullfilmentOrder.getCourier())) {
            if (String.valueOf(fullfilmentOrder.getTrackingLink()).contains("xio")) {
                fullfilmentOrder.setCourier("XIO");
            } else if (String.valueOf(fullfilmentOrder.getTrackingLink()).contains("gogo")) {
                fullfilmentOrder.setCourier("GOGO");
            } else if (String.valueOf(fullfilmentOrder.getTrackingLink()).contains("lbc")) {
                fullfilmentOrder.setCourier("LBC");
            } else if (String.valueOf(fullfilmentOrder.getTrackingLink()).contains("lav")) {
                fullfilmentOrder.setCourier("LZD");
            } else {
                fullfilmentOrder.setCourier("Unfullfiled");
            }
        }

        return fullfilmentOrder;
    }

    private void setShippingFee(JsonNode jsonNode, FullfilmentOrder fullfilmentOrder, Object noteObj) {
        try {
            if (noteObj != null && StringUtils.isNotBlank(noteObj.toString())) {
                List<String> notes = Arrays.asList(jsonNode.get("note").asText().split(","));
                String sf = notes.get(notes.size() - 1);
                if (sf.contains("SF")) {
                    fullfilmentOrder.setShippingFee(Double.valueOf(sf.substring(2)));
                }//if (sf.)
            }
        } catch (Exception e) {
            fullfilmentOrder.setShippingFee(200);
        }
    }

    private void buildCompleteAddress(JsonNode jsonNode, FullfilmentOrder fullfilmentOrder) {
        String address = jsonNode.get("billing_address").get("address1").asText();
        String barangay = jsonNode.get("billing_address").get("address2").asText();
        String city = jsonNode.get("billing_address").get("city").asText();
        String landmark = jsonNode.get("billing_address").get("zip").asText();
        String country = jsonNode.get("billing_address").get("country").asText();
        StringBuilder completeAddress = new StringBuilder();
        completeAddress.append(address);
        completeAddress.append(", ");
        completeAddress.append(barangay);
        completeAddress.append(", ");
        completeAddress.append(city);
        completeAddress.append(", ");
        completeAddress.append("Landmark: ");
        completeAddress.append(landmark);
        completeAddress.append(", ");
        completeAddress.append(country);
        completeAddress.append(".");
        fullfilmentOrder.setAddress(completeAddress.toString());
    }

    private void buildDeliveryType(JsonNode jsonNode, FullfilmentOrder fullfilmentOrder) {
        String gatewway = jsonNode.get("gateway").asText();
        if (gatewway.contains("COD")) {
            fullfilmentOrder.setDeliveryOptions("COD");
        } else {
            fullfilmentOrder.setDeliveryOptions("COP");
        }
    }

    private void buildFulfilmentDetails(JsonNode jsonNode, FullfilmentOrder fullfilmentOrder) {
        JsonNode fulfillmentNode = jsonNode.get("fulfillments").get(0);
        if (fulfillmentNode != null) {
            JsonNode trackingNumberNode = fulfillmentNode.get("tracking_number");
            JsonNode trackingUrlNode = fulfillmentNode.get("tracking_url");
            if (trackingNumberNode != null) {
                long trackingNumber = trackingNumberNode.asLong();
                fullfilmentOrder.setTrackingNo(trackingNumber);
            }
            if (trackingUrlNode != null) {
                String trackingUrl = trackingUrlNode.asText();
                fullfilmentOrder.setTrackingLink(trackingUrl);
            }
        }
    }

    public List<String> pisokliCustomer() {
        String PISOKLI_NOVEMBER =
            "Dindo Juanito Alaras,Liza paterson,Jerry Latoza Makilan,Nicson Pagar,Matet Tenepere,Avegail Abraham,"
            + "Ricky Aznar,Carlos Cafe,Carlos Cafe,Michael Remoroza,Christian San juan,Leonora Albotra,Nancy "
            + "Cuaresma,Eduard Christian Poblete,Michael PatiÃ±o,John Engelbert Liao,kenneth cataquian,Rox Zy,Richard"
            + " Namuag,Daia Callino,Allen John Romano,Analyn Mascardo,Charlie Herrera,Mary Grace Padilla";
        ;
        List<String> november = Stream.of(PISOKLI_NOVEMBER.split(","))
            .map(String::trim).
                collect(Collectors.toList());

        String PISOKLI_DECEMBER =
            "Liza Magkilat,Jonna Aguilar,Roa Abenda,Cherie Vasco,Kaye Baylen,Cepe Pedro,Norberto Nobleza,Rexyl "
            + "Cabaluna,Mac Salvador,Rodrigo Pecto,Eder Salavarria,Ricky Montero,Joselito M Santos,Maricon Ausente,"
            + "Clent John Penafiel,Joan Diaz,Dolores Eulin,Teresa Diaz,oscar maramba,Mark Gil Nirza,Jose Caballes,Dan"
            + " Olay,Morvynn Barrinuevo Racsa,Yomel Barbo,Ian joseph Suasba,Pilar Quisumbing,Jetril Labajo,Josie "
            + "Necio,Waterust Foxzon,William Bautista,Michael Bacsafra,sheila camacho,Roget De vera,Jayson Torquiano,"
            + "Wilfredo Babiano,Regiere Dianon,Jim Codilan,Ding Siquinia,Mary Joy Lucilla,Rolando Bacurin,Jack "
            + "Bermisa,Alex Entrada L.,Dickie Jayag,Leslie Hingobesa,Femilyn Rara,Mary Joy Alvarez,Roy Doctolero,"
            + "Ramiro Docos,Jerome Baluyot,Antonio Gutierrez,Lora Lyn Hernandez,Lucas Tan,Faith Morato,Roy Sollesta,"
            + "Elizabeth Kilapio,Noriel Ilagan,Myrna Balisado,Ruel Jomero,Kristie Rose Golez,GREG MORA,Mergie "
            + "Pilapil,Terence Paz,Fr. Roy Aspera,Arvin Tejada,Allan Ibarlin Alabastro,Rudy Eslira,Alphe Alvarado,"
            + "leni catapang,Charles Jose Ruste,Evelyn Rivera,Nelson Incognito,Rene Comendador,Marlon Atienza,Elaine "
            + "Tan,Joelmar Mendaros,Mina Dumuk,Michelle Marco,Romero Silva,Marilyn Cobrado,Susana Candelario,Michael "
            + "Kaiser,Edison Compuesto,Maricel Nasayao,Pabz Vinluan,Maribel Monteza,Bryan C Bio,MARKTED B. TORREGOZA,"
            + "John Ferdie Robles,Ruth Alova,Melchor Semillano,Henrico Tan,Maria fe Alejo,Cristina Aspe,Wengie "
            + "Austria,Francis Levi Molina,Naneth Telliama,Evelyn Tolentino,joel abasola,Jasmin galapon,Jason Artes,"
            + "Clarence Gordo,Zeny Hernandez,Bernard Cayanan,Josephine Balsamo,JUNE SAGUM,Jyzza mae Domingo,Jayson "
            + "Basallo Salinas,Juflex Mendua,bernardito funtila,Conrado Sacay,LUISITO PACIA,Arsenio De la cruz,Arvin "
            + "Benitez,Mae Kilby Cosico,Meo Sajonia,Rafael Calvez,RELYN Rosima,marc molero,Alvin Vina,Joyce Jane "
            + "Bontilao,Gimelo Bohol,Jayson Lao,Ermin Peralta,Merland Pines,Hilario Maadil,Rowena Guevarra,Benjie "
            + "Vidal,johnny gonzales,Geraldine Manidlangan,Joel Francisco,Hershey Robles,Arniel s. Bernadez,Mark "
            + "joseph fuentes,George Palacio,Esaine francisco Gojo cruz,Nicson Pagar,Jhalmar Gallo,Ciara Lei Soriano,"
            + "PRETCHIE PADRIGO,Fidel De villa Jr,Rebecca R. De leon,Armando Cruz,Rosalinda Cantela,Ladyly Tongol,"
            + "Louie Saramosing,Maye S. Madelar,Mariel Rama,Rhon Bulilan,Wilma Tato,Arnil Bajalan,Leonila Martinez,"
            + "Emmanuel Macario,michael angelo villegas,Arnod kenneth Taliman,FERDINAND TAPIT,Alex Corpus,Marlon "
            + "Mercado,Rhena Solon,Augusto Labaguis,Arnold Hilario,Giovanni Rex Sorita,Jonathan M. Abaincia,ALEXIS "
            + "JAMES B. LABADAN,Duazo Dante,Manuel Bitang,Verna Velasco,Edeliza Dela PeÃ±a,Marlon Dela Cruz,Frances "
            + "Maxine Macapagal,Verna Velasco,Gerald Aquino,Rezah Fern Dahan,Allan Mendoza,Leopoldo David Jr,Ma "
            + "theresa Survilla,leonel jake bilbao,Eladio Bonavente jr,paolo raymundo,Richard Antonuo,Aike M. "
            + "Nisperos,Jaimark Creer,aries salolog,Sabrina Marcelo,Carl Mari Von Ramos,Mark Ardian Aban,Catherine "
            + "Haniel,Manolito Cabardo,Richie Mutia,Jess Alvaran,Medillo Domingo,michael angelo cobrador,Robles John,"
            + "Dennis Faundo,Karen Victoriano,Reymark Gines,Clyde Hingpit,fernan enrique,Edgar F. CariÃ±o,Paulino "
            + "Pardillo jr,Mark Jerome Rodillas,Denbert D. Festin,Lawrence Bajado,Orlando Manalo,Reynante Tinguha,Jay"
            + " angelo Bumacod,Michael Santos,Josephine Buan,Brian Maluya,Dellvher Hina,Kathrine honrubia,Tony Ortiz,"
            + "Rebecca R. De leon,Enger Picat,marcelino Manuel,jayjay bangot,Romel Villarin Cuarteron,Darwin "
            + "Borromeo,Antonieta Sison,JOSE MACAYAN,Maria Veronica Montano,Franco Montes,Maechelle Siatol,John aries"
            + " Rondeja,Agapito Epoc,Michael Bernardo,Ryan Prudente,Ver Alarcio,Jupit Meer,Mark jayson Tubiag,Teofilo"
            + " De Vera Jr.,Reynan Rublica,Rick del rosario,Ella Eclavea,Domingo cris Tilbe,Nonoy E. Felipe,Maeverick"
            + " Bagtasos,Joey Laxamana,BERNADETTE ADDA-OEN,Arvin Samson,Mailyn Ondoy,Mark Anthony Engcong,Jay Lucero,"
            + "Tito Pagsuyoin,Jun-jun Gomisa,Niño Lecomis,Roel e supnet,james anthony nana,Kenneth Cusi,Vincent "
            + "Gregorio,mark jayson sabila,Jo Marie Ibarra,Eddie Jr Gaborni,jannah dagdag,Rex Smith Asilo,Catherine "
            + "Humilde,Jun matthew Radoc,Rosemarie Oros,Jerome Gruta,Cesar Domingo,mary ann payad,Ma Cristina Prado,"
            + "Binoy Gacis,Clyde Joseph Antolin,Rexmel Valdez,Myca Ortega,Mark Andrew Cababan,Charlie Betita,Bernard "
            + "Bejer,Geronimo Lito Jr,Jose Micoh Valles,Shaine Panado,Gabriel Dimaisip,Edrian Mandap,Melvin "
            + "Corsonado,Alvin Angelo Rusiana,Joseph Donayre,Francisco Saez,Ronald Rollon,Gerald Velasco,paul pineda,"
            + "Aeron Angelo Ruiz,Edgar Verceles,Leizel Abaigar,Chelrio Alivio,Arienne Mallorca,Marvin Atanacio,"
            + "Aniceto Rizol jr,ramil oranza,reyann aquino,Rene Dableo,Jaypee Llegado,Christine Clemente,Joselito "
            + "Lorenzo,Rio Salvador,Vivian Busgano,John Lon Genio,Nathalie Bantiling,Noel Delos Santos,Jaime Antig,"
            + "Donna Alimpolo,jestoni lozada,Loyl Sioc,Dondon Rana,shella mae Regaton,Roldan R. Aranas,Carel joy "
            + "Garing,Mark Pepito,Jericho Valdez,Kar jefferson Caballero,Jerry Gavarra,Kenneth Dado,Juanito Bebero,"
            + "Ivan Montelijao,Mhelodie Almarez,Lister Magpantay,Mark Moralita,Prince Vincent Agoncillo,Gerick Eliab,"
            + "John Rhen Surban,Mario Mateo,Jerwin manzano,Rolando Santos,NIEL GALVAN,Joanna Paola Tumanguil,Fred "
            + "Bayron,Angelique Caguiat";

        List<String> december = Stream.of(PISOKLI_DECEMBER.split(","))
            .map(String::trim)
            .collect(Collectors.toList());

        december.addAll(november);
        return december;
    }
}
