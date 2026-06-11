package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Store;
import com.minierp.minierp.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private  final StoreRepository storeRepository;

    @GetMapping
    public List<Store> getAll(){
        return storeRepository.findAll();
    }

    @PostMapping
    public Store create(@RequestBody Store store){
        return storeRepository.save(store);
    }
}
