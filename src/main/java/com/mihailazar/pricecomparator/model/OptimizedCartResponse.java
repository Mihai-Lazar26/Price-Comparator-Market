package com.mihailazar.pricecomparator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimizedCartResponse {
    private double total;
    private List<OptimizedProductMatch> items;
}
