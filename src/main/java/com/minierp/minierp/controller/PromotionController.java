package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Promotion;
import com.minierp.minierp.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private  final PromotionRepository promotionRepository;

    @GetMapping
    public List<Promotion> getAll(){
        return promotionRepository.findAll();
    }

    @PostMapping
    public Promotion create(@RequestBody Promotion promotion){
        return promotionRepository.save(promotion);
    }
}
