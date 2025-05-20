package com.mihailazar.pricecomparator.service;

import com.mihailazar.pricecomparator.model.PriceAlert;
import com.mihailazar.pricecomparator.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final List<PriceAlert> alerts = new ArrayList<>();
    private final ProductService productService;

    @Autowired
    public AlertService(ProductService productService) {
        this.productService = productService;
    }

    public void addAlert(PriceAlert alert) {
        alerts.add(alert);
    }

    public void removeAlert(String productId) {
        alerts.removeIf(alert -> alert.getProductId().equalsIgnoreCase(productId));
    }

    /**
     * Verifică toate alertele salvate și returnează produsele
     * care au un preț mai mic sau egal cu pragul specificat în alertă.
     */
    public List<Product> getActiveAlerts() {
        List<Product> allProducts = productService.getAllProducts();

        return alerts.stream()
                .flatMap(alert ->
                        allProducts.stream()
                                .filter(p -> p.getId().equalsIgnoreCase(alert.getProductId()))
                                .filter(p -> p.getPrice() <= alert.getTargetPrice()))
                .collect(Collectors.toList());
    }
}
