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

    /* ── 대시보드용: 이름 JOIN 포함 ──────────────────── */
    public List<CurrentStockDto> findAll() {
        List<Tuple> rows = em.createNativeQuery("""
                SELECT
                    cs.product_no,
                    cs.lot_no,
                    cs.expire_date,
                    cs.warehouse_id,
                    cs.store_id,
                    cs.current_qty,
                    p.product_nm_ko,
                    b.brand_nm,
                    w.warehouse_nm,
                    s.store_nm,
                    p.cost_price
                FROM current_stock cs
                LEFT JOIN product   p ON p.product_no    = cs.product_no
                LEFT JOIN brand     b ON b.brand_cd      = p.brand_cd
                LEFT JOIN warehouse w ON w.warehouse_id  = cs.warehouse_id
                LEFT JOIN store     s ON s.store_id      = cs.store_id
                """, Tuple.class).getResultList();

        return rows.stream().map(t -> new CurrentStockDto(
                (String)    t.get("product_no"),
                (String)    t.get("lot_no"),
                (LocalDate) t.get("expire_date"),
                t.get("warehouse_id") != null ? ((Number) t.get("warehouse_id")).intValue() : null,
                t.get("store_id")     != null ? ((Number) t.get("store_id")).intValue()     : null,
                (BigDecimal) t.get("current_qty"),
                (String)    t.get("product_nm_ko"),
                (String)    t.get("brand_nm"),
                (String)    t.get("warehouse_nm"),
                (String)    t.get("store_nm"),
                (BigDecimal) t.get("cost_price")
        )).toList();
    }

    /* ── SalesService용: 기존 쿼리 유지 (필드 추가 없음) ── */
    public List<CurrentStockDto> findAvailableForSale(String productNo, Integer storeId) {
        List<Tuple> rows = em.createNativeQuery("""
                SELECT product_no, lot_no, expire_date, warehouse_id, store_id, current_qty
                FROM current_stock
                WHERE product_no = :productNo
                  AND store_id   = :storeId
                  AND current_qty > 0
                ORDER BY expire_date ASC NULLS LAST
                """, Tuple.class)
                .setParameter("productNo", productNo)
                .setParameter("storeId",   storeId)
                .getResultList();

        return rows.stream().map(t -> new CurrentStockDto(
                (String)    t.get("product_no"),
                (String)    t.get("lot_no"),
                (LocalDate) t.get("expire_date"),
                t.get("warehouse_id") != null ? ((Number) t.get("warehouse_id")).intValue() : null,
                t.get("store_id")     != null ? ((Number) t.get("store_id")).intValue()     : null,
                (BigDecimal) t.get("current_qty")
        )).toList();
    }
}