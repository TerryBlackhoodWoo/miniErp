package com.minierp.minierp.controller;

import com.minierp.minierp.entity.ProductDocument;
import com.minierp.minierp.repository.ProductDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-documents")
@RequiredArgsConstructor
public class ProductDocumentController {
    private  final ProductDocumentRepository productDocumentRepository;

    @GetMapping
    public List<ProductDocument> getAll(){
        return productDocumentRepository.findAll();
    }

    @PostMapping
    public ProductDocument create(@RequestBody ProductDocument productDocument){
        return productDocumentRepository.save(productDocument);
    }
}
