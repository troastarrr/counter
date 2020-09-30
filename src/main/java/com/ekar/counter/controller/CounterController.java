package com.ekar.counter.controller;

import com.ekar.counter.entity.Report;
import com.ekar.counter.service.CounterManager;
import com.ekar.counter.service.CsvService;
import com.ekar.counter.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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


    private static final String SAMPLE_CSV_FILE_PATH = "/Users/clarcinfante/Downloads/x.csv";

    public static void main(String[] args) {
        try {
            CounterController x = new CounterController(null, null);
            CsvService csvService = new CsvService();
            csvService.beanBuilderExample(null,null);
            for (Report row: csvService.beanBuilderExample(null,null)) {
                System.out.println(row.getOrderNumber());
                System.out.println(row.getSellerSku());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public List<CsvBean> beanBuilderExample(Path path, Class clazz) throws Exception {
//        CsvTransfer csvTransfer = new CsvTransfer();
//        ColumnPositionMappingStrategy ms = new ColumnPositionMappingStrategy();
//        ms.setType(clazz);
//
//        Reader reader = Files.newBufferedReader(path);
//        CsvToBean cb = new CsvToBeanBuilder<>(reader)
//            .withType(clazz)
//            .withMappingStrategy(ms)
//            .build();
//
//        csvTransfer.setCsvList(cb.parse());
//        reader.close();
//        return csvTransfer.getCsvList();
//    }
//
//    public List<String[]> readAllExample() throws Exception {
//
//        Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
//        CSVParser parser = new CSVParserBuilder()
//            .withSeparator(';')
//            .withIgnoreQuotations(true)
//            .build();
//
//        CSVReader csvReader = new CSVReaderBuilder(reader)
//            .withSkipLines(0)
//            .withCSVParser(parser)
//            .build();
//        return csvReader.readAll();
//    }
//
////    CSVParser parser = new CSVParserBuilder()
////        .withSeparator(';')
////        .withIgnoreQuotations(true)
////        .build();
////
////    CSVReader csvReader = new CSVReaderBuilder(reader)
////        .withSkipLines(0)
////        .withCSVParser(parser)
////        .build();
////
////
////    public List<String[]> readAll(Reader reader) throws Exception {
////        CSVReader csvReader = new CSVReader(reader);
////        List<String[]> list = csvReader.readAll();
////        reader.close();
////        csvReader.close();
////        return list;
////    }
////
////    public List<String[]> oneByOne(Reader reader) throws Exception {
////        List<String[]> list = new ArrayList<>();
////        CSVReader csvReader = new CSVReader(reader);
////        String[] line;
////        while ((line = csvReader.readNext()) != null) {
////            list.add(line);
////        }
////        reader.close();
////        csvReader.close();
////        return list;
////    }



}
