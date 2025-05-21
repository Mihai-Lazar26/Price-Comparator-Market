package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.PriceSnapshot;
import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.model.RecommendedProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductService();

        productService.getAllProducts().add(Product.builder()
                .id("P001")
                .name("lapte")
                .category("lactate")
                .brand("Brand1")
                .quantity(1)
                .unit("l")
                .price(10.0)
                .currency("RON")
                .source("Lidl")
                .build());

        productService.getAllProducts().add(Product.builder()
                .id("P002")
                .name("lapte")
                .category("lactate")
                .brand("Brand2")
                .quantity(1)
                .unit("l")
                .price(8.5)
                .currency("RON")
                .source("Profi")
                .build());

        productService.getAllProducts().add(Product.builder()
                .id("P003")
                .name("lapte")
                .category("lactate")
                .brand("Brand3")
                .quantity(1)
                .unit("l")
                .price(12.0)
                .currency("RON")
                .source("Kaufland")
                .build());
    }

    @Test
    void testGetRecommendations_returnsCheaperProductsFromSameCategory() {
        List<RecommendedProduct> recommendations = productService.getRecommendations("P001");

        assertEquals(1, recommendations.size());
        assertEquals("P002", recommendations.get(0).getId());
        assertTrue(recommendations.get(0).getPrice() < 10.0);
    }

    @Test
    void testGetRecommendations_returnsEmptyListWhenProductNotFound() {
        List<RecommendedProduct> result = productService.getRecommendations("P999");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractSource() {
        assertEquals("Lidl", productService.extractSource("products-prices/lidl_2025-05-01.csv"));
        assertEquals("Kaufland", productService.extractSource("products-prices/kaufland_2025-05-08.csv"));
        assertEquals("Profi", productService.extractSource("products-prices/profi_2025-05-15.csv"));
        assertEquals("Unknown", productService.extractSource("products-prices/altceva_2025-05-01.csv"));
    }

    @Test
    void testExtractDate() {
        LocalDate expectedDate = LocalDate.of(2025, 5, 8);
        assertEquals(expectedDate, productService.extractDate("products-prices/lidl_2025-05-08.csv"));

        expectedDate = LocalDate.of(2025, 1, 1);
        assertEquals(expectedDate, productService.extractDate("products-prices/kaufland_2025-01-01.csv"));
    }

    @Test
    void testGetAllProductsReturnsCorrectSize() {
        int initialSize = productService.getAllProducts().size();

        productService.getAllProducts().add(Product.builder()
                .id("PX")
                .name("test")
                .category("test")
                .brand("test")
                .quantity(1)
                .unit("buc")
                .price(1.0)
                .currency("RON")
                .source("Test")
                .build());

        assertEquals(initialSize + 1, productService.getAllProducts().size());
    }

    @Test
    void testGetPriceHistoryWithMockedCsvLoading() {
        ProductService spyService = Mockito.spy(new ProductService());

        List<Product> fakeProducts = List.of(
                Product.builder()
                        .id("PX001")
                        .name("lapte")
                        .category("Lactate")
                        .brand("Zuzu")
                        .quantity(1)
                        .unit("l")
                        .price(9.99)
                        .currency("RON")
                        .source("Lidl")
                        .build()
        );

        Mockito.doReturn(fakeProducts)
                .when(spyService)
                .loadProductsFromCsv(Mockito.anyString(), Mockito.anyString());

        Mockito.doReturn(List.of("products-prices/lidl_2025-05-01.csv"))
                .when(spyService)
                .getAllProductCsvPaths();

        List<PriceSnapshot> result = spyService.getPriceHistory("PX001", "Lidl", null, null);

        assertEquals(1, result.size());
        PriceSnapshot snap = result.get(0);
        assertEquals("PX001", snap.getProductId());
        assertEquals("Lidl", snap.getSource());
        assertEquals(9.99, snap.getPrice(), 0.01);
    }
}