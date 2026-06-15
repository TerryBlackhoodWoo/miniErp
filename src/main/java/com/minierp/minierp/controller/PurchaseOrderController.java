package com.minierp.minierp.controller;

import com.minierp.minierp.entity.PurchaseOrder;
import com.minierp.minierp.repository.PurchaseOrderRepository;
import com.minierp.minierp.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrder> getAll(){
        return purchaseOrderRepository.findAll();
    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder purchaseOrder){
        purchaseOrder.setStatus("PENDING");
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Integer id, @RequestBody PurchaseOrder purchaseOrder) {
        purchaseOrder.setOrderId(id);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        purchaseOrderRepository.deleteById(id);
    }

    // ── 입고 처리 ──
    @PostMapping("/{id}/receive")
    public PurchaseOrder receive(@PathVariable Integer id,
                                 @RequestBody PurchaseOrderService.ReceiveRequest req) {
        return purchaseOrderService.receive(id, req);
    }

    @PostMapping("/{id}/allocate")
    public PurchaseOrder allocate(@PathVariable Integer id) {
        return purchaseOrderService.allocate(id);
    }
}