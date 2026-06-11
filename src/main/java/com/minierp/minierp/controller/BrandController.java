package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Brand;
import com.minierp.minierp.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private  final BrandRepository brandRepository;

    @GetMapping
    public List<Brand> getAll(){
        return brandRepository.findAll();
    }

    @PostMapping
    public Brand create(@RequestBody Brand brand){
        return brandRepository.save(brand);
    }
}
