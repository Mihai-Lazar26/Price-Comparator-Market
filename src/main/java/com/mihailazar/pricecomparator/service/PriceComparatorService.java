package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.OptimizedProductMatch;
import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.model.ShoppingItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PriceComparatorService {
    private final ProductService productService;

    private final List<Product> allProducts = new ArrayList<>();

    @Autowired
    public PriceComparatorService(ProductService productService) {
        this.productService = productService;

        allProducts.addAll(productService.loadProductsFromCsv("products-prices/lidl_2025-05-01.csv", "Lidl"));
        allProducts.addAll(productService.loadProductsFromCsv("products-prices/kaufland_2025-05-01.csv", "Kaufland"));
        allProducts.addAll(productService.loadProductsFromCsv("products-prices/profi_2025-05-01.csv", "Profi"));
    }

    public List<OptimizedProductMatch> getOptimizedShoppingCart(List<ShoppingItemRequest> requests) {
        List<OptimizedProductMatch> result = new ArrayList<>();

        for (ShoppingItemRequest req : requests) {
            Optional<Product> cheapestMatch = allProducts.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(req.getName()) && p.getUnit().equalsIgnoreCase(req.getUnit()))
                    .min(Comparator.comparingDouble(Product::getPrice));

            cheapestMatch.ifPresent(p -> {
                double unitPrice = p.getPrice() / p.getQuantity();
                double total = unitPrice * req.getQuantity();
                result.add(OptimizedProductMatch.builder()
                                .productId(p.getId())
                                .name(p.getName())
                                .brand(p.getBrand())
                                .quantityRequested(req.getQuantity())
                                .unit(p.getUnit())
                                .unitPrice(unitPrice)
                                .totalPrice(total)
                                .sourceFile(p.getSource())
                        .build());
            });
        }
        return result;
    }
}
