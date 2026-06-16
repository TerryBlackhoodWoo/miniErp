package com.minierp.minierp.controller;

import com.minierp.minierp.dto.CurrentStockDto;
import com.minierp.minierp.repository.CurrentStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/current-stock")
@RequiredArgsConstructor
public class CurrentStockController {

    private final CurrentStockRepository currentStockRepository;

    @GetMapping
    public List<CurrentStockDto> getAll() {
        return currentStockRepository.findAll();
    }
}