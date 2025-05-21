package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.PriceAlert;
import com.mihailazar.pricecomparator.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertServiceTest {
    private AlertService alertService;
    private ProductService productServiceMock;

    @BeforeEach
    void setUp() {
        productServiceMock = Mockito.mock(ProductService.class);
        alertService = new AlertService(productServiceMock);
    }

    @Test
    void testAddAndGetActiveAlerts() {
        alertService.addAlert(new PriceAlert("P001", 10.0));

        List<Product> fakeProducts = List.of(
                Product.builder().id("P001").price(9.5).build(),
                Product.builder().id("P002").price(12.0).build()
        );

        Mockito.when(productServiceMock.getAllProducts()).thenReturn(fakeProducts);

        List<Product> result = alertService.getActiveAlerts();

        assertEquals(1, result.size());
        assertEquals("P001", result.get(0).getId());
    }

    @Test
    void testRemoveAlert() {
        alertService.addAlert(new PriceAlert("P001", 10.0));
        alertService.removeAlert("P001");

        Mockito.when(productServiceMock.getAllProducts()).thenReturn(List.of(
                Product.builder().id("P001").price(8.0).build()
        ));

        List<Product> result = alertService.getActiveAlerts();

        assertTrue(result.isEmpty());
    }
}