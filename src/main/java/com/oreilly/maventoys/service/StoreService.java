package com.oreilly.maventoys.service;

import com.oreilly.maventoys.dto.EmployeeDTO;
import com.oreilly.maventoys.dto.SaleDTO;
import com.oreilly.maventoys.dto.StoreDTO;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.mapper.StoreMapper;
import com.oreilly.maventoys.models.Employee;
import com.oreilly.maventoys.models.Sales;
import com.oreilly.maventoys.repository.EmployeeRepository;
import com.oreilly.maventoys.repository.SaleRepository;
import com.oreilly.maventoys.repository.StoreRepository;
import com.oreilly.maventoys.models.Store;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final EmployeeRepository employeeRepository;
    private final SaleRepository saleRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, EmployeeRepository employeeRepository, SaleRepository saleRepository) {
        this.storeRepository = storeRepository;
        this.employeeRepository = employeeRepository;
        this.saleRepository = saleRepository;
    }


    public List<StoreDTO> findAllActiveStoreDTOs() {
        List<Store> activeStores = storeRepository.findByActive(true);
        return activeStores.stream()
                .map(StoreMapper.INSTANCE::storeToStoreDTO)
                .collect(Collectors.toList());
    }

    public StoreDTO findStoreDTOById(Long id) {
        return storeRepository.findById(id)
                .map(StoreMapper.INSTANCE::storeToStoreDTO)
                .orElseThrow(() -> new EntityNotFoundException("No se pudo encontrar la tienda con ID: " + id));
    }

    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = StoreMapper.INSTANCE.storeDTOToStore(storeDTO);
        store = storeRepository.save(store);
        return StoreMapper.INSTANCE.storeToStoreDTO(store);
    }

    public StoreDTO updateStore(Long id, StoreDTO storeDTO) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se pudo encontrar la tienda con ID: " + id));

        // Actualiza solo el nombre y la ciudad
        store.setName(storeDTO.getName());
        store.setCity(storeDTO.getCity());
        store.setLocation(storeDTO.getLocation());
        store.setOpenDate(storeDTO.getOpenDate());
        store.setActive(storeDTO.getActive());

        Store updatedStore = storeRepository.save(store);
        return StoreMapper.INSTANCE.storeToStoreDTO(updatedStore);
    }
    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId((long) employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setHireDate(employee.getHireDate());
        dto.setGender(employee.getGender());
        dto.setBirthDate(employee.getBirthDate());
        dto.setActive(employee.isActive());
        return dto;
    }
    public List<EmployeeDTO> findEmployeesByStoreId(Long storeId) {
        List<Employee> employees = employeeRepository.findByStoreId(storeId);
        return employees.stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());
    }

    public List<SaleDTO> findSalesByStoreId(int storeId) {
        List<Sales> salesList = saleRepository.findByStoreId(storeId);
        return salesList.stream()
                .map(SaleMapper.INSTANCE::saleToSaleDTO)
                .collect(Collectors.toList());
    }




}
