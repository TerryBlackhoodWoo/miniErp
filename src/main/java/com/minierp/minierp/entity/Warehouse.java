package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "warehouse")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "warehouse_nm", length = 200)
    private String warehouseNm;

    @Column(name = "warehouse_type", length = 20)
    private String warehouseType;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "manager", length = 50)
    private String manager;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "cost_per_pallet")
    private BigDecimal costPerPallet;  // 파레트당 물류비
}