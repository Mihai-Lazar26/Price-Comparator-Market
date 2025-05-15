package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.Product;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public List<Product> loadProductsFromCsv(String path) {
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
                            .build();
                    products.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

}
