package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Integer saleId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "product_no", nullable = false, length = 8)
    private String productNo;

    @Column(name = "brand_cd", nullable = false, length = 6)
    private String brandCd;

    @Column(name = "vendor_cd", nullable = false, length = 6)
    private String vendorCd;

    @Column(name = "channel", length = 10)
    private String channel;       // ONLINE / OFFLINE

    @Column(name = "order_no", length = 50)
    private String orderNo;       // 온라인 주문번호 (OFFLINE 시 NULL)

    @Column(name = "lot_no", length = 50)
    private String lotNo;         // FIFO로 자동 결정됨

    @Column(name = "sale_qty")
    private BigDecimal saleQty;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "promo_id")
    private Integer promoId;      // 프로모션 미구현, 항상 NULL

    @Column(name = "sale_at")
    private LocalDateTime saleAt;

    @PrePersist
    protected void onCreate() {
        if (this.saleAt == null) this.saleAt = LocalDateTime.now();
    }
}