package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sales, Integer> {
    List<Sales> findByStoreId(int storeId);
}
