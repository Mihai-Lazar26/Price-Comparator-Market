package com.mihailazar.pricecomparator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendedProduct {
    private String id;
    private String name;
    private String brand;
    private String category;
    private double quantity;
    private String unit;
    private double price;
    private double unitPrice;
    private String currency;
    private String source;

}
