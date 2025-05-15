package com.mihailazar.pricecomparator.controller;

import com.mihailazar.pricecomparator.model.Discount;
import com.mihailazar.pricecomparator.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/load")
    public List<Discount> loadDiscoutns(@RequestParam("file") String file) {
        return discountService.loadDiscountsFromCsv(file);
    }
}
