package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_order")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @Column(name = "product_no", nullable = false, length = 8)
    private String productNo;

    @Column(name = "brand_cd", nullable = false, length = 6)
    private String brandCd;

    @Column(name = "vendor_cd", nullable = false, length = 6)
    private String vendorCd;

    @Column(name = "order_qty")
    private BigDecimal orderQty;

    @Column(name = "approved_qty")
    private BigDecimal approvedQty;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}