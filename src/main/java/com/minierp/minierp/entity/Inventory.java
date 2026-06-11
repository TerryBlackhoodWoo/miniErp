package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inv_id")
    private Integer invId;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "product_no", nullable = false, length = 8)
    private String productNo;

    @Column(name = "brand_cd", nullable = false, length = 6)
    private String brandCd;

    @Column(name = "vendor_cd", nullable = false, length = 6)
    private String vendorCd;

    @Column(name = "lot_no", length = 50)
    private String lotNo;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "move_type", length = 10)
    private String moveType;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "ref_id")
    private BigDecimal refId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}