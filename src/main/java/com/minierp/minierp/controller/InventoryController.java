package com.minierp.minierp.controller;

import com.minierp.minierp.dto.InventoryLedgerDto;
import com.minierp.minierp.entity.Inventory;
import com.minierp.minierp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private  final InventoryRepository inventoryRepository;

    @GetMapping
    public List<Inventory> getAll(){
        return inventoryRepository.findAll();
    }

    @PostMapping
    public Inventory create(@RequestBody Inventory inventory){
        return inventoryRepository.save(inventory);
    }
    @PutMapping("/{id}")
    public Inventory update(@PathVariable Integer id, @RequestBody Inventory inventory) {
        inventory.setInvId(id);
        return inventoryRepository.save(inventory);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        inventoryRepository.deleteById(id);
    }

    @GetMapping("/ledger")
    public List<InventoryLedgerDto> getLedger() {
        return inventoryRepository.findAllLedger();
    }
}
