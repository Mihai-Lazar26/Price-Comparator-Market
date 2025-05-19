package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.PriceSnapshot;
import com.mihailazar.pricecomparator.model.Product;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public List<Product> loadProductsFromCsv(String path, String source) {
        List<Product> products = new ArrayList<>();
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);

        if (is == null) {
            System.err.println("CSV file not found: " + path);
            return products;
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
                    Product p = Product.builder()
                            .id(line[0])
                            .name(line[1])
                            .category(line[2])
                            .brand(line[3])
                            .quantity(Double.parseDouble(line[4]))
                            .unit(line[5])
                            .price(Double.parseDouble(line[6]))
                            .currency(line[7])
                            .source(source)
                            .build();
                    products.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private String extractSource(String filePath) {
        if (filePath.contains("lidl")) return "Lidl";
        if (filePath.contains("kaufland")) return "Kaufland";
        if (filePath.contains("profi")) return "Profi";
        return "Unknown";
    }

    private LocalDate extractDate(String filePath) {
        String dateStr = filePath.replaceAll(".*_(\\d{4}-\\d{2}-\\d{2})\\.csv", "$1");
        return LocalDate.parse(dateStr);
    }

    public List<PriceSnapshot> getPriceHistory(String productId, String store, String category, String brand) {
        List<PriceSnapshot> history = new ArrayList<>();

        List<String> sources = List.of(
                "products-prices/lidl_2025-05-01.csv",
                "products-prices/lidl_2025-05-08.csv",
                "products-prices/profi_2025-05-01.csv",
                "products-prices/profi_2025-05-08.csv",
                "products-prices/kaufland_2025-05-01.csv",
                "products-prices/kaufland_2025-05-08.csv"
        );

        for (String file : sources) {
            String source = extractSource(file);
            if (store != null && !source.equalsIgnoreCase(store)) continue;

            LocalDate date = extractDate(file);
            List<Product> products = loadProductsFromCsv(file, source);

            products.stream()
                    .filter(p -> productId == null || p.getId().equalsIgnoreCase(productId))
                    .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                    .filter(p -> brand == null || p.getBrand().equalsIgnoreCase(brand))
                    .findFirst()
                    .ifPresent(p -> history.add(PriceSnapshot.builder()
                                    .productId(p.getId())
                                    .name(p.getName())
                                    .price(p.getPrice())
                                    .date(date)
                                    .source(source)
                            .build()));
        }

        return history;
    }

}
