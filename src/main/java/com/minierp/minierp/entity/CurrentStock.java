package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "current_stock")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CurrentStock {

    @EmbeddedId
    private CurrentStockId id;

    @Column(name = "current_qty")
    private BigDecimal currentQty;
}