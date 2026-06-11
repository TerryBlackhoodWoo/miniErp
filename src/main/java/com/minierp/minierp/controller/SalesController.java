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

    @PutMapping("/{id}")
    public Sales update(@PathVariable Integer id, @RequestBody Sales sales) {
        sales.setSaleId(id);
        return salesRepository.save(sales);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        salesRepository.deleteById(id);
    }
}
