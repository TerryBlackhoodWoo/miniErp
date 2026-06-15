package com.minierp.minierp.service;

import com.minierp.minierp.entity.Inventory;
import com.minierp.minierp.entity.Product;
import com.minierp.minierp.entity.PurchaseOrder;
import com.minierp.minierp.entity.Warehouse;
import com.minierp.minierp.repository.InventoryRepository;
import com.minierp.minierp.repository.ProductRepository;
import com.minierp.minierp.repository.PurchaseOrderRepository;
import com.minierp.minierp.repository.WarehouseRepository;
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

        // 협력사 → 창고 (IN)
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

        return po;
    }

    @Transactional
    public PurchaseOrder allocate(Integer orderId) {
        PurchaseOrder po = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("발주를 찾을 수 없습니다: " + orderId));

        if (!"RECEIVED".equals(po.getStatus())) {
            throw new IllegalStateException("입고 완료 상태의 발주만 배분할 수 있습니다: " + orderId);
        }

        BigDecimal qty = po.getReceivedQty();

        // 창고 → 배분 차감 (OUT)
        inventoryRepository.save(Inventory.builder()
                .warehouseId(po.getWarehouseId())
                .storeId(null)
                .productNo(po.getProductNo())
                .brandCd(po.getBrandCd())
                .vendorCd(po.getVendorCd())
                .lotNo(po.getLotNo())
                .expireDate(po.getExpireDate())
                .moveType("OUT")
                .qty(qty)
                .refId(BigDecimal.valueOf(po.getOrderId()))
                .build());

        // 창고 → 지점 (IN)
        inventoryRepository.save(Inventory.builder()
                .warehouseId(null)
                .storeId(po.getStoreId())
                .productNo(po.getProductNo())
                .brandCd(po.getBrandCd())
                .vendorCd(po.getVendorCd())
                .lotNo(po.getLotNo())
                .expireDate(po.getExpireDate())
                .moveType("IN")
                .qty(qty)
                .refId(BigDecimal.valueOf(po.getOrderId()))
                .build());

        po.setStatus("COMPLETED");
        purchaseOrderRepository.save(po);

        return po;
    }

    // 입고 요청 DTO
    public record ReceiveRequest(BigDecimal receivedQty, String lotNo, LocalDate expireDate, String logisticsMemo) {
    }
}