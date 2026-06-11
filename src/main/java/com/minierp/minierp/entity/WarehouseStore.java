package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "warehouse_store")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class WarehouseStore {

    @EmbeddedId
    private WarehouseStoreId id;

    @Column(name = "is_primary", length = 1)
    private String isPrimary;

    @Embeddable
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode
    public static class WarehouseStoreId implements Serializable {
        @Column(name = "warehouse_id")
        private Integer warehouseId;

        @Column(name = "store_id")
        private Integer storeId;
    }
}