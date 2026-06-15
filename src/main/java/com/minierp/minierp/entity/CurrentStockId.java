package com.minierp.minierp.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class CurrentStockId implements Serializable {

    private String productNo;
    private String lotNo;
    private LocalDate expireDate;
    private Integer warehouseId;
    private Integer storeId;
}