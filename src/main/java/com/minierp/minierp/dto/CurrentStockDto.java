package com.minierp.minierp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CurrentStockDto {
    private String productNo;
    private String lotNo;
    private LocalDate expireDate;
    private Integer warehouseId;
    private Integer storeId;
    private BigDecimal currentQty;
}