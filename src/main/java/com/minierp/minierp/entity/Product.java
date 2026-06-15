package com.minierp.minierp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(name = "product_no")
    private String productNo;

    @Column(name = "brand_cd", nullable = false, length = 6)
    private String brandCd;

    @Column(name = "vendor_cd", nullable = false, length = 6)
    private String vendorCd;

    @Column(name = "product_nm_ko", nullable = false, length = 200)
    private String productNmKo;

    @Column(name = "product_nm_en", length = 100)
    private String productNmEn;

    @Column(name = "capacity")
    private BigDecimal capacity;

    @Column(name = "unit", length = 5)
    private String unit;

    @Column(name = "barcode", length = 20)
    private String barcode;

    @Column(name = "retail_price", nullable = false)
    private BigDecimal retailPrice;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "supply_cost")
    private BigDecimal supplyCost;

    @Column(name = "mfg_cost")
    private BigDecimal mfgCost;

    @Column(name = "cost_base", length = 10)
    private String costBase;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "qty_per_box")
    private Integer qtyPerBox;       // 박스당 EA 수

    @Column(name = "box_per_pallet")
    private Integer boxPerPallet;    // 파레트당 박스 수

    @Column(name = "tax_free")
    private Boolean taxFree;
}