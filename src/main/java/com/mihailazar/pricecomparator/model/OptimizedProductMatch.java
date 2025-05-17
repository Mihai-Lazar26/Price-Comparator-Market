package com.mihailazar.pricecomparator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimizedProductMatch {
    private String productId;
    private String name;
    private String brand;
    private double quantityRequested;
    private String unit;
    private double unitPrice;
    private double totalPrice;
    private String sourceFile; // magazinul
}
