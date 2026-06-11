package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Product;
import com.minierp.minierp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private  final ProductRepository productRepository;

    @GetMapping
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product){
        return productRepository.save(product);
    }
}
