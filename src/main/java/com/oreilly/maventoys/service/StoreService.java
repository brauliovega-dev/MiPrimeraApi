package com.oreilly.maventoys.service;

import com.oreilly.maventoys.dto.EmployeeDTO;
import com.oreilly.maventoys.dto.SaleDTO;
import com.oreilly.maventoys.dto.StoreDTO;
import com.oreilly.maventoys.mapper.SaleMapper;
import com.oreilly.maventoys.mapper.StoreMapper;
import com.oreilly.maventoys.models.Employee;
import com.oreilly.maventoys.models.Sales;
import com.oreilly.maventoys.models.Store;
import com.oreilly.maventoys.repository.EmployeeRepository;
import com.oreilly.maventoys.repository.SaleRepository;
import com.oreilly.maventoys.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final EmployeeRepository employeeRepository;
    private final SaleRepository saleRepository;

    @Autowired // Inyeccion de dependencias
    public StoreService(StoreRepository storeRepository, EmployeeRepository employeeRepository, SaleRepository saleRepository) {
        this.storeRepository = storeRepository;
        this.employeeRepository = employeeRepository;
        this.saleRepository = saleRepository;
    }

    // Recupera todos los DTOs de las tiendas activas
    public List<StoreDTO> findAllActiveStoreDTOs() {
        List<Store> activeStores = storeRepository.findByActive(true);
        return activeStores.stream()
                .map(StoreMapper.INSTANCE::storeToStoreDTO)
                .collect(Collectors.toList());
    }

    // Encuentra un DTO de tienda por su ID
    public StoreDTO findStoreDTOById(Long id) {
        return storeRepository.findById(id)
                .map(StoreMapper.INSTANCE::storeToStoreDTO)
                .orElseThrow(() -> new EntityNotFoundException("No se pudo encontrar la tienda con ID: " + id));
    }

    // Crea una nueva tienda a partir de un DTO y la guarda en la base de datos
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = StoreMapper.INSTANCE.storeDTOToStore(storeDTO);
        store = storeRepository.save(store);
        return StoreMapper.INSTANCE.storeToStoreDTO(store);
    }

    // Actualiza una tienda existente usando su ID y un DTO
    public StoreDTO updateStore(Long id, StoreDTO storeDTO) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se pudo encontrar la tienda con ID: " + id));

        store.setName(storeDTO.getName());
        store.setCity(storeDTO.getCity());
        store.setLocation(storeDTO.getLocation());
        store.setOpenDate(storeDTO.getOpenDate());
        store.setActive(storeDTO.getActive());

        Store updatedStore = storeRepository.save(store);
        return StoreMapper.INSTANCE.storeToStoreDTO(updatedStore);
    }

    // Convierte un objeto Employee a EmployeeDTO
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

    // Encuentra todos los empleados de una tienda específica por su ID
    public List<EmployeeDTO> findEmployeesByStoreId(Long storeId) {
        List<Employee> employees = employeeRepository.findByStoreId(storeId);
        return employees.stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());
    }

    // Encuentra todas las ventas asociadas a una tienda por su ID
    public List<SaleDTO> findSalesByStoreId(int storeId) {
        List<Sales> salesList = saleRepository.findByStoreId(storeId);
        return salesList.stream()
                .map(SaleMapper.INSTANCE::saleToSaleDTO)
                .collect(Collectors.toList());
    }

    // Recupera todas las tiendas con paginación
    public Page<StoreDTO> findAllStores(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAll(pageable);
        return storePage.map(StoreMapper.INSTANCE::storeToStoreDTO);
    }


    public StoreDTO updateStore(int id, StoreDTO storeDTO) {
        Optional<Store> storeOptional = storeRepository.findById((long) id);

        if(storeOptional.isPresent()){
            Store store = storeOptional.get();

            // Actualiza solo los campos que no son null en StoreDTO
            if(storeDTO.getName() != null) store.setName(storeDTO.getName());
            if(storeDTO.getCity() != null) store.setCity(storeDTO.getCity());
            if(storeDTO.getLocation() != null) store.setLocation(storeDTO.getLocation());
            if(storeDTO.getOpenDate() != null) store.setOpenDate(storeDTO.getOpenDate());
            if(storeDTO.getActive() != null) store.setActive(storeDTO.getActive());

            Store updatedStore = storeRepository.save(store);
            return StoreMapper.INSTANCE.storeToStoreDTO(updatedStore);
        }

        // Maneja el caso en que el store no se encuentra
        return null;
    }


    public Double getTotalSalesByStore(Long storeId) {
        return saleRepository.sumTotalByStoreId(storeId).orElse(0.0);
    }

}
