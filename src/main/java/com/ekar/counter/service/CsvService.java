package com.ekar.counter.service;

import com.ekar.counter.entity.Report;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvService {

    private static final String SAMPLE_CSV_FILE_PATH = "/Users/clarcinfante/Downloads/x.csv";
    public List<Report> beanBuilderExample(Path path, Class clazz) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(';')
        .withIgnoreQuotations(true)
        .build();


        CsvToBean csvToBean =  new CsvToBeanBuilder<Report>(reader).withType(Report.class)
            .withThrowExceptions(false)
            .build();

        return csvToBean.parse();
    }
}
