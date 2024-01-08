package com.oreilly.maventoys.repository;

import com.oreilly.maventoys.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sales, Integer> {
    List<Sales> findByStoreId(int storeId);
    @Query("SELECT SUM(s.total) FROM Sales s WHERE s.store.id = :storeId")
    Optional<Double> sumTotalByStoreId(Long storeId);

}
