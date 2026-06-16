package com.minierp.minierp.service;

import com.minierp.minierp.dto.CurrentStockDto;
import com.minierp.minierp.entity.Inventory;
import com.minierp.minierp.entity.Product;
import com.minierp.minierp.entity.Sales;
import com.minierp.minierp.repository.CurrentStockRepository;
import com.minierp.minierp.repository.InventoryRepository;
import com.minierp.minierp.repository.ProductRepository;
import com.minierp.minierp.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    private final CurrentStockRepository currentStockRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public Sales createSale(SaleRequest req) {
        Product product = productRepository.findById(req.productNo())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + req.productNo()));

        BigDecimal requestedQty = req.saleQty();

        // FIFO LOT 목록 조회 (유통기한 빠른 순)
        List<CurrentStockDto> availableLots = currentStockRepository.findAvailableForSale(req.productNo(), req.storeId());

        BigDecimal totalAvailable = availableLots.stream()
                .map(CurrentStockDto::getCurrentQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalAvailable.compareTo(requestedQty) < 0) {
            throw new IllegalStateException(
                    "재고가 부족합니다. 요청: " + requestedQty + ", 가용재고: " + totalAvailable);
        }

        // FIFO로 LOT별 차감 (inventory OUT 여러 건 생성 가능)
        BigDecimal remaining = requestedQty;
        String firstLotNo = null;
        for (CurrentStockDto lot : availableLots) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal deductQty = lot.getCurrentQty().min(remaining);
            if (firstLotNo == null) firstLotNo = lot.getLotNo();

            inventoryRepository.save(Inventory.builder()
                    .warehouseId(null)
                    .storeId(req.storeId())
                    .productNo(req.productNo())
                    .brandCd(product.getBrandCd())
                    .vendorCd(product.getVendorCd())
                    .lotNo(lot.getLotNo())
                    .expireDate(lot.getExpireDate())
                    .moveType("OUT")
                    .qty(deductQty)
                    .build());

            remaining = remaining.subtract(deductQty);
        }

        // sales 등록 (대표 LOT - 가장 먼저 차감된 LOT 기록)
        Sales sale = Sales.builder()
                .storeId(req.storeId())
                .productNo(req.productNo())
                .brandCd(product.getBrandCd())
                .vendorCd(product.getVendorCd())
                .channel(req.channel())
                .orderNo(req.orderNo())
                .lotNo(firstLotNo)
                .saleQty(requestedQty)
                .salePrice(req.salePrice())
                .promoId(null)
                .build();

        return salesRepository.save(sale);
    }

    public record SaleRequest(
            Integer storeId,
            String productNo,
            String channel,
            String orderNo,
            BigDecimal saleQty,
            BigDecimal salePrice
    ) {
    }
}