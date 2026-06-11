package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Brand {
    @Id
    @Column(name = "brand_cd")
    private String brandCd; // 브랜드코드(PK)

    @Column(name = "vendor_cd", nullable = false, length = 6)
    private String vendorCd; // 협력사코드(FK)

    @Column(name = "brand_nm", nullable = false, length = 200)
    private String brandNm; // 브랜드명

    @Column(name = "brand_type", nullable = false, length = 10)
    private String brandType; // 'DOMESTIC(국산), IMPORT(수입산)'

    @Column(name = "supply_rate")
    private BigDecimal supplyRate; // 공급률
}
