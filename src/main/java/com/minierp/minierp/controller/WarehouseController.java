package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Warehouse;
import com.minierp.minierp.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private  final WarehouseRepository warehouseRepository;

    @GetMapping
    public List<Warehouse> getAll(){
        return warehouseRepository.findAll();
    }

    @PostMapping
    public Warehouse create(@RequestBody Warehouse warehouse){
        return warehouseRepository.save(warehouse);
    }
}
