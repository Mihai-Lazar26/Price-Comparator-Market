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
    public List<Discount> loadDiscoutns(@RequestParam("file") String file,
                                        @RequestParam("source") String source) {
        return discountService.loadDiscountsFromCsv(file, source);
    }

    @GetMapping("/best")
    public List<Discount> getBestDiscounts(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        return discountService.getBestDiscounts(limit);
    }

    @GetMapping("/new")
    public List<Discount> getNewDiscounts() {
        return discountService.getNewDiscounts();
    }
}
