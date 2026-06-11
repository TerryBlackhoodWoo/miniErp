package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Sales;
import com.minierp.minierp.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {
    private  final SalesRepository salesRepository;

    @GetMapping
    public List<Sales> getAll(){
        return salesRepository.findAll();
    }

    @PostMapping
    public Sales create(@RequestBody Sales sales){
        return salesRepository.save(sales);
    }
}
