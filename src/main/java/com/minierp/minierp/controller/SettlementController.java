package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Settlement;
import com.minierp.minierp.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {
    private  final SettlementRepository settlementRepository;

    @GetMapping
    public List<Settlement> getAll(){
        return settlementRepository.findAll();
    }

    @PostMapping
    public Settlement create(@RequestBody Settlement settlement){
        return settlementRepository.save(settlement);
    }

    @PutMapping("/{id}")
    public Settlement update(@PathVariable Integer id, @RequestBody Settlement settlement) {
        settlement.setSettlementId(id);
        return settlementRepository.save(settlement);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        settlementRepository.deleteById(id);
    }
}
