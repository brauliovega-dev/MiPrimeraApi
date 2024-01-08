package com.oreilly.maventoys.repository;
import com.oreilly.maventoys.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByActive(Boolean active);

}
