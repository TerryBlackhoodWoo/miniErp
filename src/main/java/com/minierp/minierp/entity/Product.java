package com.minierp.minierp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {
    @Id
    @Column(name = "product_no")
    private String productNo; //품번(PK)

    @Column(name = "brand_cd", nullable = false, length = 6)
    private String brandCd; // 브랜드 코드(FK)

    @Column(name = "vendor_cd", nullable = false,length = 6)
    private String vendorCd; // 협력사 코드(FK)

    @Column(name = "product_nm_ko", nullable = false,length = 200)
    private String productNmKo; // 상품명_한글

    @Column(name = "product_nm_en", length = 100)
    private String productNmEn; // 상품명_영어

    @Column(name = "capacity")
    private BigDecimal capacity; // 용량

    @Column(name = "unit", length = 5)
    private String unit; // 단위(EA,ml,g,cc,inch등)

    @Column(name = "barcode", length = 20)
    private String barcode; // 바코드

    @Column(name = "retail_price", nullable = false)
    private BigDecimal  retailPrice; // 정상가

    @Column(name = "sale_price")
    private BigDecimal  salePrice; // 판매가

    @Column(name = "cost_price")
    private BigDecimal  costPrice; // 매입원가(완사입)

    @Column(name = "supply_cost")
    private BigDecimal  supplyCost; // 공급원가(위탁)

    @Column(name = "mfg_cost")
    private BigDecimal mfgCost; // 제조원가(자사)

    @Column(name = "cost_base", length = 10)
    private String costBase; // RETAIL or SALE - 원가계산기준



}
