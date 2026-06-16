package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Sales;
import com.minierp.minierp.repository.SalesRepository;
import com.minierp.minierp.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepository salesRepository;
    private final SalesService salesService;

    @GetMapping
    public List<Sales> getAll() {
        return salesRepository.findAll();
    }

    @PostMapping
    public Sales create(@RequestBody SalesService.SaleRequest req) {
        return salesService.createSale(req);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleStockShortage(IllegalStateException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }
}