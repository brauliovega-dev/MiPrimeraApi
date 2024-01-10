package com.oreilly.maventoys.repository;
import com.oreilly.maventoys.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByStoreId(Long storeId);
    @Query("SELECT e FROM Employee e JOIN FETCH e.store WHERE e.active = :active")
    List<Employee> findByActiveWithStore(Boolean active);
}
