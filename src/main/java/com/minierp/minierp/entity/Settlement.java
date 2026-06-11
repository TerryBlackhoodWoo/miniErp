package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlement")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id")
    private Integer settlementId;

    @Column(name = "vendor_cd", nullable = false, length = 6)
    private String vendorCd;

    @Column(name = "brand_cd", nullable = false, length = 6)
    private String brandCd;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "settle_month", length = 7)
    private String settleMonth;

    @Column(name = "opening_qty")
    private BigDecimal openingQty;

    @Column(name = "in_qty")
    private BigDecimal inQty;

    @Column(name = "return_qty")
    private BigDecimal returnQty;

    @Column(name = "closing_qty")
    private BigDecimal closingQty;

    @Column(name = "sale_qty")
    private BigDecimal saleQty;

    @Column(name = "sale_amt")
    private BigDecimal saleAmt;

    @Column(name = "supply_amt")
    private BigDecimal supplyAmt;

    @Column(name = "vat_amt")
    private BigDecimal vatAmt;

    @Column(name = "tax_invoice_amt")
    private BigDecimal taxInvoiceAmt;

    @Column(name = "store_fee")
    private BigDecimal storeFee;

    @Column(name = "incentive_amt")
    private BigDecimal incentiveAmt;

    @Column(name = "final_amt")
    private BigDecimal finalAmt;

    @Column(name = "settle_status", length = 10)
    private String settleStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}