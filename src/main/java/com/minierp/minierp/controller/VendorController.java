package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Vendor;
import com.minierp.minierp.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    private  final VendorRepository vendorRepository;

    @GetMapping
    public List<Vendor> getAll(){
        return vendorRepository.findAll();
    }

    @PostMapping
    public Vendor create(@RequestBody Vendor vendor){
        return vendorRepository.save(vendor);
    }
}
