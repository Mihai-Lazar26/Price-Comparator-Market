package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.OptimizedCartResponse;
import com.mihailazar.pricecomparator.model.OptimizedProductMatch;
import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.model.ShoppingItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceComparatorServiceTest {
    private ProductService productService;
    private PriceComparatorService priceComparatorService;

    @BeforeEach
    void setup() {
        productService = new ProductService();
        priceComparatorService = new PriceComparatorService(productService);

        productService.getAllProducts().addAll(List.of(
                Product.builder()
                        .id("P001")
                        .name("lapte")
                        .brand("Zuzu")
                        .category("lactate")
                        .quantity(1.0)
                        .unit("l")
                        .price(9.0)
                        .currency("RON")
                        .source("Lidl")
                        .build(),

                Product.builder()
                        .id("P002")
                        .name("lapte")
                        .brand("AltBrand")
                        .category("lactate")
                        .quantity(1.0)
                        .unit("l")
                        .price(7.5)
                        .currency("RON")
                        .source("Profi")
                        .build()
        ));
    }

    @Test
    void testGetOptimizedShoppingCart_returnsCheapestMatch() {
        ShoppingItemRequest request = new ShoppingItemRequest("lapte", 2.0, "l");

        OptimizedCartResponse response = priceComparatorService.getOptimizedShoppingCart(List.of(request));

        assertEquals(1, response.getItems().size());

        OptimizedProductMatch match = response.getItems().get(0);
        assertEquals("P002", match.getProductId());
        assertEquals(2.0, match.getQuantityRequested());
        assertEquals(7.5, match.getUnitPrice());
        assertEquals(15.0, match.getTotalPrice());
        assertEquals(15.0, response.getTotal());
    }

    @Test
    void testGetOptimizedShoppingCart_returnsEmptyIfNoMatch() {
        ShoppingItemRequest request = new ShoppingItemRequest("ciocolata", 1.0, "buc");

        OptimizedCartResponse response = priceComparatorService.getOptimizedShoppingCart(List.of(request));

        assertTrue(response.getItems().isEmpty());
        assertEquals(0.0, response.getTotal());
    }

}