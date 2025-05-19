package com.mihailazar.pricecomparator.controller;

import com.mihailazar.pricecomparator.model.*;
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
    public OptimizedCartResponse optimizeCart(@RequestBody List<ShoppingItemRequest> items) {
        return priceComparatorService.getOptimizedShoppingCart(items);
    }

    @GetMapping("/history")
    public List<PriceSnapshot> getPriceHistory(@RequestParam(value = "productId", required = false) String productId,
                                               @RequestParam(value = "store", required = false) String store,
                                               @RequestParam(value = "category",required = false) String category,
                                               @RequestParam(value = "brand",required = false) String brand) {
        return productService.getPriceHistory(productId, store, category, brand);
    }

    @GetMapping("/recommendations")
    public List<RecommendedProduct> getRecommendations(@RequestParam(value = "productId") String productId) {
        return productService.getRecommendations(productId);
    }
}
