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
public class PriceSnapshot {
    private String productId;
    private String name;
    private String brand;
    private double price;
    private LocalDate date;
    private String source;
}
