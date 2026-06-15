package com.minierp.minierp.controller;

import com.minierp.minierp.entity.CurrentStock;
import com.minierp.minierp.repository.CurrentStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/current-stock")
@RequiredArgsConstructor
public class CurrentStockController {

    private final CurrentStockRepository currentStockRepository;

    @GetMapping
    public List<CurrentStock> getAll() {
        return currentStockRepository.findAll();
    }

    @GetMapping("/by-product/{productNo}")
    public List<CurrentStock> getByProduct(@PathVariable String productNo) {
        return currentStockRepository.findByIdProductNo(productNo);
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    public List<CurrentStock> getByWarehouse(@PathVariable Integer warehouseId) {
        return currentStockRepository.findByIdWarehouseId(warehouseId);
    }

    @GetMapping("/by-store/{storeId}")
    public List<CurrentStock> getByStore(@PathVariable Integer storeId) {
        return currentStockRepository.findByIdStoreId(storeId);
    }
}