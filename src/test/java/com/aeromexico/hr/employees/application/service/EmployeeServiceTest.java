package com.aeromexico.hr.employees.application.service;

import com.aeromexico.hr.employees.domain.exception.EmployeeAlreadyExistsException;
import com.aeromexico.hr.employees.domain.exception.EmployeeNotFoundException;
import com.aeromexico.hr.employees.domain.model.Employee;
import com.aeromexico.hr.employees.domain.model.EmploymentStatus;
import com.aeromexico.hr.employees.domain.port.EmployeeRepositoryPort;
import com.aeromexico.hr.employees.presentation.dto.EmployeeRequest;
import com.aeromexico.hr.employees.presentation.dto.EmployeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Technical Test - Employee Management Service
 *
 * Author: Omar Navarro
 * Role: Java Technical Lead
 * Date: 2026
 *
 * Description:
 * REST controller responsible for exposing employee management endpoints.
 */


@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepositoryPort employeeRepositoryPort;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest();
        request.setEmployeeNumber("EMP-001");
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setEmail("juan.perez@aeromexico.com");
        request.setPhone("5555555555");
        request.setDepartment("Human Resources");
        request.setPosition("HR Analyst");
        request.setHireDate(LocalDate.now());
        request.setStatus(EmploymentStatus.ACTIVE);
        request.setSalary(BigDecimal.valueOf(35000));

        employee = new Employee(
                1L,
                "EMP-001",
                "Juan",
                "Perez",
                "juan.perez@aeromexico.com",
                "5555555555",
                "Human Resources",
                "HR Analyst",
                LocalDate.now(),
                EmploymentStatus.ACTIVE,
                BigDecimal.valueOf(35000),
                Boolean.TRUE,
                LocalDateTime.now(),
                null
        );
    }

    @Test
    void createEmployeeShouldReturnEmployeeResponse() {
        when(employeeRepositoryPort.existsByEmployeeNumber("EMP-001")).thenReturn(false);
        when(employeeRepositoryPort.existsByEmail("juan.perez@aeromexico.com")).thenReturn(false);
        when(employeeRepositoryPort.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("EMP-001", response.getEmployeeNumber());
        verify(employeeRepositoryPort).save(any(Employee.class));
    }

    @Test
    void createEmployeeShouldThrowExceptionWhenEmployeeNumberAlreadyExists() {
        when(employeeRepositoryPort.existsByEmployeeNumber("EMP-001")).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.createEmployee(request));
        verify(employeeRepositoryPort, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeByIdShouldReturnEmployeeResponse() {
        when(employeeRepositoryPort.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getFirstName());
    }

    @Test
    void getEmployeeByIdShouldThrowExceptionWhenEmployeeDoesNotExist() {
        when(employeeRepositoryPort.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void getEmployeesShouldReturnPage() {
        when(employeeRepositoryPort.findAllByActiveTrue(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(employee)));

        Page<EmployeeResponse> result = employeeService.getEmployees(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateEmployeeShouldReturnUpdatedEmployee() {
        Employee updated = new Employee(
                1L,
                "EMP-001",
                "Juan Carlos",
                "Perez",
                "juan.perez@aeromexico.com",
                "5555555555",
                "Technology",
                "Java Developer",
                LocalDate.now(),
                EmploymentStatus.ACTIVE,
                BigDecimal.valueOf(45000),
                Boolean.TRUE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        request.setFirstName("Juan Carlos");
        request.setDepartment("Technology");
        request.setPosition("Java Developer");
        request.setSalary(BigDecimal.valueOf(45000));

        when(employeeRepositoryPort.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));
        when(employeeRepositoryPort.existsByEmployeeNumberAndIdNot("EMP-001", 1L)).thenReturn(false);
        when(employeeRepositoryPort.existsByEmailAndIdNot("juan.perez@aeromexico.com", 1L)).thenReturn(false);
        when(employeeRepositoryPort.save(any(Employee.class))).thenReturn(updated);

        EmployeeResponse response = employeeService.updateEmployee(1L, request);

        assertEquals("Juan Carlos", response.getFirstName());
        assertEquals("Technology", response.getDepartment());
    }

    @Test
    void deleteEmployeeShouldSetActiveFalse() {
        Employee deleted = new Employee(
                employee.getId(), employee.getEmployeeNumber(), employee.getFirstName(), employee.getLastName(),
                employee.getEmail(), employee.getPhone(), employee.getDepartment(), employee.getPosition(),
                employee.getHireDate(), employee.getStatus(), employee.getSalary(), Boolean.FALSE,
                employee.getCreatedAt(), LocalDateTime.now()
        );
        when(employeeRepositoryPort.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));
        when(employeeRepositoryPort.save(any(Employee.class))).thenReturn(deleted);

        employeeService.deleteEmployee(1L);

        assertFalse(employee.getActive());
        verify(employeeRepositoryPort).save(employee);
    }
}
