package com.mihailazar.pricecomparator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discount {
    private String productId;
    private String productName;
    private String brand;
    private double quantity;
    private String unit;
    private String category;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentage;
}
