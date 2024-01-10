package com.oreilly.maventoys.mapper;

import com.oreilly.maventoys.dto.EmployeeDTO;
import com.oreilly.maventoys.models.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
    EmployeeDTO employeeToEmployeeDTO(Employee employee);
}

