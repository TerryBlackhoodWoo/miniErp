package com.minierp.minierp.repository;

import com.minierp.minierp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {


}
