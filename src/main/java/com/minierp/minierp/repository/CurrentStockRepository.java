package com.minierp.minierp.repository;

import com.minierp.minierp.entity.CurrentStock;
import com.minierp.minierp.entity.CurrentStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CurrentStockRepository extends JpaRepository<CurrentStock, CurrentStockId> {

    List<CurrentStock> findByIdProductNo(String productNo);

    List<CurrentStock> findByIdWarehouseId(Integer warehouseId);

    List<CurrentStock> findByIdStoreId(Integer storeId);

    // 판매 시 재고 검증용 - 특정 지점/상품의 가용 재고 합계
    @Query("""
        SELECT COALESCE(SUM(cs.currentQty), 0)
        FROM CurrentStock cs
        WHERE cs.id.productNo = :productNo
          AND cs.id.storeId = :storeId
    """)
    BigDecimal getAvailableQty(String productNo, Integer storeId);

    // FIFO 차감용 - 유통기한 빠른 순으로 재고 있는 LOT 조회
    @Query("""
        SELECT cs FROM CurrentStock cs
        WHERE cs.id.productNo = :productNo
          AND cs.id.storeId = :storeId
          AND cs.currentQty > 0
        ORDER BY cs.id.expireDate ASC NULLS LAST
    """)
    List<CurrentStock> findAvailableLotsForSale(String productNo, Integer storeId);
}