package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();

        discountService.getAllDiscounts().addAll(List.of(
                Discount.builder()
                        .productId("D001")
                        .productName("lapte")
                        .brand("Zuzu")
                        .category("lactate")
                        .quantity(1)
                        .unit("l")
                        .percentage(10)
                        .fromDate(LocalDate.of(2025, 5, 10))
                        .toDate(LocalDate.of(2025, 5, 20))
                        .source("Lidl")
                        .build(),

                Discount.builder()
                        .productId("D002")
                        .productName("iaurt")
                        .brand("Lidl")
                        .category("lactate")
                        .quantity(0.5)
                        .unit("kg")
                        .percentage(25)
                        .fromDate(LocalDate.of(2025, 5, 11))
                        .toDate(LocalDate.of(2025, 5, 20))
                        .source("Kaufland")
                        .build(),

                Discount.builder()
                        .productId("D003")
                        .productName("cafea")
                        .brand("Davidoff")
                        .category("cafea")
                        .quantity(0.25)
                        .unit("kg")
                        .percentage(5)
                        .fromDate(LocalDate.of(2025, 4, 25))
                        .toDate(LocalDate.of(2025, 5, 5))
                        .source("Profi")
                        .build()
        ));
    }

    @Test
    void testGetBestDiscounts_returnsSortedTopResults() {
        List<Discount> best = discountService.getBestDiscounts(2);

        assertEquals(2, best.size());
        assertEquals("D002", best.get(0).getProductId()); // 25%
        assertEquals("D001", best.get(1).getProductId()); // 10%
    }

    @Test
    void testGetNewDiscounts_withMockedToday() {
        DiscountService spyService = Mockito.spy(new DiscountService());

        Mockito.doReturn(LocalDate.of(2025, 5, 11))
                .when(spyService)
                .getToday();

        spyService.getAllDiscounts().addAll(List.of(
                Discount.builder()
                        .productId("D001")
                        .productName("lapte")
                        .brand("Zuzu")
                        .category("lactate")
                        .quantity(1)
                        .unit("l")
                        .percentage(10)
                        .fromDate(LocalDate.of(2025, 5, 10))
                        .toDate(LocalDate.of(2025, 5, 20))
                        .source("Lidl")
                        .build(),

                Discount.builder()
                        .productId("D002")
                        .productName("cafea")
                        .brand("Davidoff")
                        .category("cafea")
                        .quantity(0.25)
                        .unit("kg")
                        .percentage(20)
                        .fromDate(LocalDate.of(2025, 4, 30))
                        .toDate(LocalDate.of(2025, 5, 5))
                        .source("Profi")
                        .build()
        ));

        List<Discount> result = spyService.getNewDiscounts();

        assertEquals(1, result.size());
        assertEquals("D001", result.get(0).getProductId());
    }
}