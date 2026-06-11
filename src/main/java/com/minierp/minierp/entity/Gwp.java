package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gwp")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Gwp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gwp_id")
    private Integer gwpId;

    @Column(name = "product_no", nullable = false, length = 8)
    private String productNo;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "bom_id")
    private Integer bomId;

    @Column(name = "gift_nm", length = 200)
    private String giftNm;

    @Column(name = "gift_qty")
    private BigDecimal giftQty;

    @Column(name = "min_purchase")
    private BigDecimal minPurchase;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}