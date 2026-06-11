package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Bom;
import com.minierp.minierp.repository.BomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boms")
@RequiredArgsConstructor
public class BomController {
    private  final BomRepository bomRepository;

    @GetMapping
    public List<Bom> getAll(){
        return bomRepository.findAll();
    }

    @PostMapping
    public Bom create(@RequestBody Bom bom){
        return bomRepository.save(bom);
    }
}
