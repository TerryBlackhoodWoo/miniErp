package com.minierp.minierp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InventoryLedgerDto {
    private Integer invId;
    private String productNo;
    private String productNmKo;
    private String lotNo;
    private LocalDate expireDate;
    private String moveType;            // IN / OUT
    private String moveTypeLabel;       // 입고 / 출고
    private BigDecimal qty;
    private Integer warehouseId;
    private String warehouseNm;         // null 가능
    private Integer storeId;
    private String storeNm;             // null 가능
    private LocalDateTime createdAt;
}