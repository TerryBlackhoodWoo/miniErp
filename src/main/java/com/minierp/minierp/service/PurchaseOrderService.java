package com.minierp.minierp.service;

import com.minierp.minierp.entity.*;
import com.minierp.minierp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public PurchaseOrder receive(Integer orderId, ReceiveRequest req) {
        PurchaseOrder po = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("발주를 찾을 수 없습니다: " + orderId));

        Product product = productRepository.findById(po.getProductNo())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + po.getProductNo()));

        Warehouse warehouse = warehouseRepository.findById(po.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다: " + po.getWarehouseId()));

        BigDecimal receivedQty = req.receivedQty();

        // ── 박스/파레트/물류비 자동계산 ──
        BigDecimal boxCount = null, palletCount = null, logisticsCost = null;
        if (product.getQtyPerBox() != null && product.getQtyPerBox() > 0) {
            boxCount = receivedQty.divide(BigDecimal.valueOf(product.getQtyPerBox()), 0, RoundingMode.CEILING);
            if (product.getBoxPerPallet() != null && product.getBoxPerPallet() > 0) {
                palletCount = boxCount.divide(BigDecimal.valueOf(product.getBoxPerPallet()), 1, RoundingMode.CEILING);
                if (warehouse.getCostPerPallet() != null) {
                    logisticsCost = palletCount.multiply(warehouse.getCostPerPallet()).setScale(0, RoundingMode.HALF_UP);
                }
            }
        }

        // ── purchase_order 업데이트 ──
        po.setReceivedQty(receivedQty);
        po.setLotNo(req.lotNo());
        po.setExpireDate(req.expireDate());
        po.setBoxCount(boxCount);
        po.setPalletCount(palletCount);
        po.setLogisticsCost(logisticsCost);
        po.setLogisticsMemo(req.logisticsMemo());
        po.setReceivedAt(LocalDateTime.now());
        po.setStatus("RECEIVED");
        purchaseOrderRepository.save(po);

        boolean isDirect = Boolean.TRUE.equals(po.getIsDirect());

        if (!isDirect) {
            // 1) 협력사 → 창고 입고 (경유 기록, store=NULL)
            inventoryRepository.save(Inventory.builder()
                    .warehouseId(po.getWarehouseId())
                    .storeId(null)
                    .productNo(po.getProductNo())
                    .brandCd(po.getBrandCd())
                    .vendorCd(po.getVendorCd())
                    .lotNo(req.lotNo())
                    .expireDate(req.expireDate())
                    .moveType("IN")
                    .qty(receivedQty)
                    .refId(BigDecimal.valueOf(po.getOrderId()))
                    .build());
        }

        // 2) 창고 → 지점 배정 (직배송이면 협력사 → 지점 직행)
        inventoryRepository.save(Inventory.builder()
                .warehouseId(po.getWarehouseId())
                .storeId(po.getStoreId())
                .productNo(po.getProductNo())
                .brandCd(po.getBrandCd())
                .vendorCd(po.getVendorCd())
                .lotNo(req.lotNo())
                .expireDate(req.expireDate())
                .moveType("IN")
                .qty(receivedQty)
                .refId(BigDecimal.valueOf(po.getOrderId()))
                .build());

        return po;
    }

    // 입고 요청 DTO
    public record ReceiveRequest(
            BigDecimal receivedQty,
            String lotNo,
            LocalDate expireDate,
            String logisticsMemo
    ) {
    }
}
