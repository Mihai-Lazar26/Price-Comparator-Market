package com.mihailazar.pricecomparator.controller;

import com.mihailazar.pricecomparator.model.OptimizedProductMatch;
import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.model.ShoppingItemRequest;
import com.mihailazar.pricecomparator.service.PriceComparatorService;
import com.mihailazar.pricecomparator.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final PriceComparatorService priceComparatorService;

    @Autowired
    public ProductController(ProductService productService, PriceComparatorService priceComparatorService) {
        this.productService = productService;
        this.priceComparatorService = priceComparatorService;
    }

    @GetMapping("/load")
    public List<Product> loadProducts(@RequestParam("file") String file,
                                      @RequestParam("source") String source) {
        return productService.loadProductsFromCsv(file, source);
    }

    @PostMapping("/optimize-cart")
    public List<OptimizedProductMatch> optimizeCart(@RequestBody List<ShoppingItemRequest> items) {
        return priceComparatorService.getOptimizedShoppingCart(items);
    }
}
