package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bom")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Bom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bom_id")
    private Integer bomId;

    @Column(name = "parent_no", nullable = false, length = 8)
    private String parentNo;

    @Column(name = "child_no", nullable = false, length = 8)
    private String childNo;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "bom_type", length = 10)
    private String bomType;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active", length = 1)
    private String isActive = "Y";

    @Column(name = "memo", length = 200)
    private String memo;
}