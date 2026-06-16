package com.minierp.minierp.repository;

import com.minierp.minierp.dto.CurrentStockDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CurrentStockRepository {

    @PersistenceContext
    private EntityManager em;

    public List<CurrentStockDto> findAll() {
        List<Tuple> rows = em.createNativeQuery("""
                SELECT product_no, lot_no, expire_date, warehouse_id, store_id, current_qty
                FROM current_stock
                """, Tuple.class).getResultList();

        return rows.stream().map(t -> new CurrentStockDto(
                (String) t.get("product_no"),
                (String) t.get("lot_no"),
                (LocalDate) t.get("expire_date"),
                t.get("warehouse_id") != null ? ((Number) t.get("warehouse_id")).intValue() : null,
                t.get("store_id") != null ? ((Number) t.get("store_id")).intValue() : null,
                (BigDecimal) t.get("current_qty")
        )).toList();
    }

    public List<CurrentStockDto> findAvailableForSale(String productNo, Integer storeId) {
        List<Tuple> rows = em.createNativeQuery("""
                SELECT product_no, lot_no, expire_date, warehouse_id, store_id, current_qty
                FROM current_stock
                WHERE product_no = :productNo
                  AND store_id = :storeId
                  AND current_qty > 0
                ORDER BY expire_date ASC NULLS LAST
                """, Tuple.class)
                .setParameter("productNo", productNo)
                .setParameter("storeId", storeId)
                .getResultList();

        return rows.stream().map(t -> new CurrentStockDto(
                (String) t.get("product_no"),
                (String) t.get("lot_no"),
                (LocalDate) t.get("expire_date"),
                t.get("warehouse_id") != null ? ((Number) t.get("warehouse_id")).intValue() : null,
                t.get("store_id") != null ? ((Number) t.get("store_id")).intValue() : null,
                (BigDecimal) t.get("current_qty")
        )).toList();
    }
}