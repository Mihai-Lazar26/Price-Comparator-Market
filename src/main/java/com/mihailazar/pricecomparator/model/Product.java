package com.mihailazar.pricecomparator.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private String id;
    private String name;
    private String category;
    private String brand;
    private double quantity;
    private String unit;
    private double price;
    private String currency;
}
