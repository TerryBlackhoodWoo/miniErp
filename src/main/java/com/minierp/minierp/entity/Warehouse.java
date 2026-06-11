package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
