package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "store")
@Getter@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder

public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_nm", length = 200)
    private String storeNm;

    @Column(name = "store_type", length = 20)
    private String storeType;

    @Column(name = "store_cd", length = 20)
    private String storeCd;
}
