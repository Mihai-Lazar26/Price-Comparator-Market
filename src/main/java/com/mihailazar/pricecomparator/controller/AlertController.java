package com.mihailazar.pricecomparator.controller;

import com.mihailazar.pricecomparator.model.PriceAlert;
import com.mihailazar.pricecomparator.model.Product;
import com.mihailazar.pricecomparator.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public void addAlert(@RequestBody PriceAlert alert) {
        alertService.addAlert(alert);
    }

    @GetMapping("/active")
    public List<Product> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    @DeleteMapping
    public void removeAlert(@RequestParam(value = "productId") String productId) {
        alertService.removeAlert(productId);
    }
}
