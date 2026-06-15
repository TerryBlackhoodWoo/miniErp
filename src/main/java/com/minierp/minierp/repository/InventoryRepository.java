package com.minierp.minierp.repository;

import com.minierp.minierp.dto.InventoryLedgerDto;
import com.minierp.minierp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    @Query("""
                SELECT new com.minierp.minierp.dto.InventoryLedgerDto(
                    i.invId,
                    i.productNo,
                    p.productNmKo,
                    i.lotNo,
                    i.expireDate,
                    i.moveType,
                    CASE WHEN i.moveType = 'IN' THEN '입고' WHEN i.moveType = 'OUT' THEN '출고' ELSE i.moveType END,
                    i.qty,
                    i.warehouseId,
                    w.warehouseNm,
                    i.storeId,
                    s.storeNm,
                    i.createdAt
                )
                FROM Inventory i
                JOIN Product p ON p.productNo = i.productNo
                LEFT JOIN Warehouse w ON w.warehouseId = i.warehouseId
                LEFT JOIN Store s ON s.storeId = i.storeId
                ORDER BY i.createdAt DESC, i.invId DESC
            """)
    List<InventoryLedgerDto> findAllLedger();
}