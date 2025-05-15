package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.Discount;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Discount> loadDiscountsFromCsv(String path) {
        List<Discount> discounts = new ArrayList<>();
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            System.err.println("Discount CSV file not found: " + path);
            return discounts;
        }

        try {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            try (InputStreamReader isr = new InputStreamReader(is);
                 CSVReader reader = new CSVReaderBuilder(isr)
                         .withCSVParser(parser)
                         .build()) {

                String[] line;
                boolean first = true;
                while ((line = reader.readNext()) != null) {
                    if (first) {
                        first = false;
                        continue;
                    }

                    Discount discount = Discount.builder()
                            .productId(line[0])
                            .productName(line[1])
                            .brand(line[2])
                            .quantity(Double.parseDouble(line[3]))
                            .unit(line[4])
                            .category(line[5])
                            .fromDate(LocalDate.parse(line[6], formatter))
                            .toDate(LocalDate.parse(line[7], formatter))
                            .percentage(Integer.parseInt(line[8]))
                            .build();

                    discounts.add(discount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discounts;
    }
}
