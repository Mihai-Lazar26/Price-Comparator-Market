package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.Discount;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final List<Discount> allDiscounts = new ArrayList<>();
    @PostConstruct
    public void init() {
        allDiscounts.addAll(loadDiscountsFromCsv("discounts/lidl_discounts_2025-05-01.csv", "Lidl"));
        allDiscounts.addAll(loadDiscountsFromCsv("discounts/kaufland_discounts_2025-05-01.csv", "Kaufland"));
        allDiscounts.addAll(loadDiscountsFromCsv("discounts/profi_discounts_2025-05-01.csv", "Profi"));
    }

    public List<Discount> getAllDiscounts() {
        return allDiscounts;
    }

    public List<Discount> getBestDiscounts(int limit) {
        return allDiscounts.stream()
                .sorted(Comparator.comparingInt(Discount::getPercentage).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Discount> loadDiscountsFromCsv(String path, String source) {
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
                            .source(source)
                            .build();

                    discounts.add(discount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discounts;
    }

    public List<Discount> getNewDiscounts() {
//        LocalDate today = LocalDate.now();
        LocalDate totday = LocalDate.of(2025, 5, 6);
        LocalDate yesterday = totday.minusDays(1);

        return allDiscounts.stream()
                .filter(d -> !d.getFromDate().isBefore(yesterday))
                .collect(Collectors.toList());
    }
}
