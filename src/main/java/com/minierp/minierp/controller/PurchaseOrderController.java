package com.minierp.minierp.controller;

import com.minierp.minierp.entity.PurchaseOrder;
import com.minierp.minierp.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private  final PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping
    public List<PurchaseOrder> getAll(){
        return purchaseOrderRepository.findAll();
    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder purchaseOrder){
        return purchaseOrderRepository.save(purchaseOrder);
    }
}
