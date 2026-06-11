package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promotion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_id")
    private Integer promoId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "product_no", nullable = false, length = 8)
    private String productNo;

    @Column(name = "promo_nm", length = 200)
    private String promoNm;

    @Column(name = "promo_price")
    private BigDecimal promoPrice;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}