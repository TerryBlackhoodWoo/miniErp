package com.minierp.minierp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CurrentStockDto {
    // 기존 필드 (SalesService에서 사용 중 — 변경 금지)
    private String productNo;
    private String lotNo;
    private LocalDate expireDate;
    private Integer warehouseId;
    private Integer storeId;
    private BigDecimal currentQty;

    // 대시보드용 추가 필드
    private String productNmKo;
    private String brandNm;
    private String warehouseNm;
    private String storeNm;
    private BigDecimal costPrice;

    /* SalesService가 쓰는 기존 5+1 생성자 — 하위호환 유지 */
    public CurrentStockDto(String productNo, String lotNo, LocalDate expireDate,
                           Integer warehouseId, Integer storeId, BigDecimal currentQty) {
        this(productNo, lotNo, expireDate, warehouseId, storeId, currentQty,
                null, null, null, null, null);
    }
}