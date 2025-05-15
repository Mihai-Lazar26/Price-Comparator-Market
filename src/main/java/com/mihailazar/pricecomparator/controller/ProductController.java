package com.mihailazar.pricecomparator.controller;

import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/load")
    public List<Product> loadProducts(@RequestParam("file") String file) {
        return productService.loadProductsFromCsv(file);
    }
}
