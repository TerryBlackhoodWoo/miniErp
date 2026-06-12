package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "received_qty")
    private BigDecimal receivedQty;     // 실입고 수량

    @Column(name = "status", length = 10)
    private String status;              // PENDING/APPROVED/RECEIVED/REJECTED

    // LOT 정보
    @Column(name = "lot_no", length = 50)
    private String lotNo;

    @Column(name = "expire_date")
    private LocalDate expireDate;       // 유통기한

    // 물류비 자동계산
    @Column(name = "box_count")
    private BigDecimal boxCount;        // 박스 수

    @Column(name = "pallet_count")
    private BigDecimal palletCount;     // 파레트 수

    @Column(name = "logistics_cost")
    private BigDecimal logisticsCost;   // 물류비 합계

    @Column(name = "logistics_memo", length = 200)
    private String logisticsMemo;       // 운송사/비고

    @Column(name = "received_at")
    private LocalDateTime receivedAt;   // 실입고 일시

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}