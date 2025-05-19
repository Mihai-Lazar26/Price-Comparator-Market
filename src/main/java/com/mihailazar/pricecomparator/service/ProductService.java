package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.PriceSnapshot;
import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.model.RecommendedProduct;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final List<Product> allProducts = new ArrayList<>();

    @PostConstruct
    public void init() {
        allProducts.addAll(loadProductsFromCsv("products-prices/lidl_2025-05-01.csv", "Lidl"));
        allProducts.addAll(loadProductsFromCsv("products-prices/kaufland_2025-05-01.csv", "Kaufland"));
        allProducts.addAll(loadProductsFromCsv("products-prices/profi_2025-05-01.csv", "Profi"));
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

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

    public List<RecommendedProduct> getRecommendations(String productId) {
        Optional<Product> reference = allProducts.stream()
                .filter(p -> p.getId().equalsIgnoreCase(productId))
                .findFirst();

        if (reference.isEmpty()) return List.of();

        Product ref = reference.get();
        double refUnitPrice = ref.getPrice() / ref.getQuantity();

        return allProducts.stream()
                .filter(p -> !p.getId().equalsIgnoreCase(ref.getId()))
                .filter(p -> p.getCategory().equalsIgnoreCase(ref.getCategory()))
                .filter(p -> p.getUnit().equalsIgnoreCase(ref.getUnit()))
                .filter(p -> (p.getPrice() / p.getQuantity()) < refUnitPrice)
                .sorted(Comparator.comparingDouble(p -> p.getPrice() / p.getQuantity()))
                .limit(5)
                .map(p -> RecommendedProduct.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .brand(p.getBrand())
                        .category(p.getCategory())
                        .quantity(p.getQuantity())
                        .unit(p.getUnit())
                        .price(p.getPrice())
                        .unitPrice(p.getPrice() / p.getQuantity())
                        .currency(p.getCurrency())
                        .source(p.getSource())
                        .build())
                .collect(Collectors.toList());
    }

}
