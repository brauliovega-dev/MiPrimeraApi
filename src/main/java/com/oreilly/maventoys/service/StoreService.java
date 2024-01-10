package com.oreilly.maventoys.service;

import com.oreilly.maventoys.dto.EmployeeDTO;
import com.oreilly.maventoys.dto.SaleDTO;
import com.oreilly.maventoys.dto.StoreDTO;
import com.oreilly.maventoys.mapper.EmployeeMapper;
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

    // Método para recuperar todos los Data Transfer Objects (DTOs) de tiendas que están actualmente activas.
// Esto se hace mediante la consulta al repositorio de tiendas (storeRepository) para obtener todas las tiendas con el estado activo (true).
// Luego, cada entidad de tienda se mapea a su correspondiente DTO para su uso en capas superiores como la interfaz de usuario o APIs.
    public List<StoreDTO> findAllActiveStoreDTOs() {
        List<Store> activeStores = storeRepository.findByActive(true); // Consulta para obtener todas las tiendas activas.
        return activeStores.stream()
                .map(StoreMapper.INSTANCE::storeToStoreDTO) // Convierte cada entidad de tienda en un DTO.
                .collect(Collectors.toList()); // Recopila y devuelve la lista de DTOs.
        //Servicio > Repositorio > Mapper > DTO
    }

    // Método para encontrar el DTO de una tienda específica usando su identificador único (ID).
// Se busca en el repositorio de tiendas por el ID proporcionado. Si se encuentra la tienda, se convierte en un DTO.
// Si no se encuentra la tienda, se lanza una excepción personalizada para indicar que no se pudo encontrar la tienda.
    public StoreDTO findStoreDTOById(Long id) {
        return storeRepository.findById(id) // Busca la tienda por su ID.
                .map(StoreMapper.INSTANCE::storeToStoreDTO) // Convierte la tienda encontrada a DTO.
                .orElseThrow(() -> new EntityNotFoundException("No se pudo encontrar la tienda con ID: " + id)); // Manejo de error si no se encuentra la tienda.
    }

    // Método para crear una nueva tienda en el sistema.
// Acepta un DTO de tienda, lo convierte en una entidad de tienda, y luego guarda esta entidad en la base de datos a través del repositorio.
// Finalmente, devuelve el DTO de la tienda recién creada, lo cual nos sirve para confirmar la creación y mostrar los detalles de la tienda creada.
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = StoreMapper.INSTANCE.storeDTOToStore(storeDTO); // Convierte DTO a entidad.
        store = storeRepository.save(store); // Guarda la entidad en la base de datos.
        return StoreMapper.INSTANCE.storeToStoreDTO(store); // Convierte la entidad guardada de vuelta a DTO y la devuelve.
    }

    // Método para actualizar los detalles de una tienda existente usando su ID y un DTO actualizado.
// Si no se encuentra la tienda con el ID dado, se lanza una excepción.
// Este método es útil para modificar detalles de una tienda como su nombre, ubicación, etc., y luego guardar estos cambios en la base de datos.
    public StoreDTO updateStore(Long id, StoreDTO storeDTO) {
        Store store = storeRepository.findById(id) // Busca la tienda por su ID.
                .orElseThrow(() -> new EntityNotFoundException("No se pudo encontrar la tienda con ID: " + id)); // Error si no se encuentra la tienda.

        // Actualiza los campos de la tienda con la información del DTO.
        // Cada línea verifica si el campo en el DTO no es nulo antes de actualizarlo en la entidad de la tienda.
        store.setName(storeDTO.getName());
        store.setCity(storeDTO.getCity());
        store.setLocation(storeDTO.getLocation());
        store.setOpenDate(storeDTO.getOpenDate());
        store.setActive(storeDTO.getActive());

        Store updatedStore = storeRepository.save(store); // Guarda los cambios en la base de datos.
        return StoreMapper.INSTANCE.storeToStoreDTO(updatedStore); // Devuelve el DTO de la tienda actualizada.
    }

    // Método auxiliar para convertir una entidad de empleado a un DTO de empleado.
// Esto es útil para presentar datos de empleados en un formato estandarizado y desacoplado de la estructura de la base de datos.


    // Método para encontrar y devolver todos los empleados asociados a una tienda específica, identificada por su ID.
// Cada empleado encontrado se convierte en un DTO antes de ser devuelto.
// Esto es útil para obtener información detallada de los empleados de una tienda específica, como para informes o gestión de recursos humanos.
    public List<EmployeeDTO> findEmployeesByStoreId(Long storeId) {
        List<Employee> employees = employeeRepository.findByStoreId(storeId);
        return employees.stream()
                .map(EmployeeMapper.INSTANCE::employeeToEmployeeDTO) // Utiliza MapStruct para convertir
                .collect(Collectors.toList());
    }

    // Método para encontrar y devolver todas las ventas asociadas a una tienda específica, identificada por su ID.
// Cada venta encontrada se convierte en un DTO antes de ser devuelta.
// Esto es útil para analizar las ventas de una tienda, como para informes de ventas o análisis de rendimiento.
    public List<SaleDTO> findSalesByStoreId(int storeId) {
        List<Sales> salesList = saleRepository.findByStoreId(storeId); // Busca ventas por ID de tienda.
        return salesList.stream()
                .map(SaleMapper.INSTANCE::saleToSaleDTO) // Convierte cada venta a DTO.
                .collect(Collectors.toList()); // Recopila y devuelve la lista de DTOs de ventas.
    }

    // Método para recuperar todas las tiendas con paginación aplicada.
// Esto es crucial para manejar grandes conjuntos de datos y evitar sobrecargar la memoria o el rendimiento de la aplicación.
// Devuelve una página de DTOs de tiendas, lo cual es útil para mostrar en interfaces de usuario con paginación.
    public Page<StoreDTO> findAllStores(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAll(pageable); // Encuentra todas las tiendas con la paginación.
        return storePage.map(StoreMapper.INSTANCE::storeToStoreDTO); // Convierte cada tienda a DTO y devuelve la página de DTOs.
    }

    // Parchar tienda
// Maneja casos donde algunos campos del DTO podrían ser nulos y no deben sobrescribir los datos existentes.
// Esencial para actualizar selectivamente los detalles de una tienda sin afectar otros campos no especificados.
    public StoreDTO updateStore(int id, StoreDTO storeDTO) {
        Optional<Store> storeOptional = storeRepository.findById((long) id); // Busca la tienda por ID.

        if(storeOptional.isPresent()){
            Store store = storeOptional.get();

            // Actualiza solo los campos que no son null en el DTO.
            // Cada condición verifica si un campo en el DTO es no nulo antes de actualizarlo en la entidad de la tienda.
            if(storeDTO.getName() != null) store.setName(storeDTO.getName());
            if(storeDTO.getCity() != null) store.setCity(storeDTO.getCity());
            if(storeDTO.getLocation() != null) store.setLocation(storeDTO.getLocation());
            if(storeDTO.getOpenDate() != null) store.setOpenDate(storeDTO.getOpenDate());
            if(storeDTO.getActive() != null) store.setActive(storeDTO.getActive());

            Store updatedStore = storeRepository.save(store); // Guarda los cambios en la base de datos.
            return StoreMapper.INSTANCE.storeToStoreDTO(updatedStore); // Devuelve el DTO de la tienda actualizada.
        }

        // Devuelve null si la tienda no se encuentra, indicando que la actualización no fue posible.
        return null;
    }

    // Método para calcular el total de ventas de una tienda específica, identificada por su ID.
// Utiliza una consulta agregada en el repositorio de ventas para sumar todas las ventas de la tienda.
// Devuelve el total como un valor Double, lo cual es útil para análisis financiero y de ventas.
    public Double getTotalSalesByStore(Long storeId) {
        return saleRepository.sumTotalByStoreId(storeId).orElse(0.0); // Calcula y devuelve el total de ventas, o 0.0 si no hay ventas.
    }


}
