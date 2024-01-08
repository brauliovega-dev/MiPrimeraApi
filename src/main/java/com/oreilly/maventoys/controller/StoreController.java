package com.oreilly.maventoys.controller;

import com.oreilly.maventoys.dto.EmployeeDTO;
import com.oreilly.maventoys.dto.SaleDTO;
import com.oreilly.maventoys.dto.StoreDTO;
import com.oreilly.maventoys.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public List<StoreDTO> getActiveStores() {
        return storeService.findAllActiveStoreDTOs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
        try {
            StoreDTO storeDTO = storeService.findStoreDTOById(id);
            return ResponseEntity.ok(storeDTO);
        } catch (EntityNotFoundException e) {
            // Aquí puedo meter las validaciones

            return ResponseEntity.notFound().build();
        }


}

    @PostMapping
    public ResponseEntity<StoreDTO> createStore(@RequestBody StoreDTO storeDTO) {
        StoreDTO createdStore = storeService.createStore(storeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDTO> updateStore(@PathVariable Long id, @RequestBody StoreDTO storeDTO) {
        try {
            StoreDTO updatedStore = storeService.updateStore(id, storeDTO);
            return ResponseEntity.ok(updatedStore);
        } catch (EntityNotFoundException e) {
            // Manejo del caso donde la tienda no se encuentra.
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByStoreId(@PathVariable Long id) {
        try {
            List<EmployeeDTO> employees = storeService.findEmployeesByStoreId(id);
            return ResponseEntity.ok(employees);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/sales")
    public ResponseEntity<List<SaleDTO>> getSalesByStoreId(@PathVariable int id) {
        try {
            List<SaleDTO> sales = storeService.findSalesByStoreId(id);
            return ResponseEntity.ok(sales);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<StoreDTO>> getStoresPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<StoreDTO> storePage = storeService.findAllStores(pageable);
        return ResponseEntity.ok(storePage);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoreDTO> updateStore(@PathVariable int id, @RequestBody StoreDTO storeDTO) {
        StoreDTO updatedStore = storeService.updateStore(id, storeDTO);

        if(updatedStore == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedStore);
    }

    @GetMapping("/{id}/totalSales")
    public ResponseEntity<Double> getTotalSalesByStore(@PathVariable Long id) {
        Double totalSales = storeService.getTotalSalesByStore(id);
        return ResponseEntity.ok(totalSales);
    }


}
