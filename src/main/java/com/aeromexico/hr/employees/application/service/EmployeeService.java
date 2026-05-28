package com.aeromexico.hr.employees.application.service;

import com.aeromexico.hr.employees.common.constants.EmployeeConstants;
import com.aeromexico.hr.employees.domain.exception.EmployeeAlreadyExistsException;
import com.aeromexico.hr.employees.domain.exception.EmployeeNotFoundException;
import com.aeromexico.hr.employees.domain.model.Employee;
import com.aeromexico.hr.employees.domain.port.EmployeeRepositoryPort;
import com.aeromexico.hr.employees.presentation.dto.EmployeeRequest;
import com.aeromexico.hr.employees.presentation.dto.EmployeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepositoryPort employeeRepositoryPort;

    public EmployeeService(EmployeeRepositoryPort employeeRepositoryPort) {
        this.employeeRepositoryPort = employeeRepositoryPort;
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        LOGGER.info("Creating employee with employeeNumber={}", request.getEmployeeNumber());

        validateUniqueEmployee(request.getEmployeeNumber(), request.getEmail());

        Employee employee = toDomain(request);
        employee.setActive(Boolean.TRUE);
        employee.setCreatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepositoryPort.save(employee);
        LOGGER.info("Event={}, employeeId={}", EmployeeConstants.EMPLOYEE_CREATED_EVENT, savedEmployee.getId());

        return toResponse(savedEmployee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = getActiveEmployee(id);
        LOGGER.info("Event={}, employeeId={}", EmployeeConstants.EMPLOYEE_CONSULTED_EVENT, id);
        return toResponse(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getEmployees(Pageable pageable) {
        return employeeRepositoryPort.findAllByActiveTrue(pageable).map(this::toResponse);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        LOGGER.info("Updating employee with id={}", id);

        Employee employee = getActiveEmployee(id);
        validateUniqueEmployeeForUpdate(id, request.getEmployeeNumber(), request.getEmail());

        employee.setEmployeeNumber(request.getEmployeeNumber());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPosition());
        employee.setHireDate(request.getHireDate());
        employee.setStatus(request.getStatus());
        employee.setSalary(request.getSalary());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepositoryPort.save(employee);
        LOGGER.info("Event={}, employeeId={}", EmployeeConstants.EMPLOYEE_UPDATED_EVENT, id);

        return toResponse(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        LOGGER.info("Deleting employee logically with id={}", id);

        Employee employee = getActiveEmployee(id);
        employee.setActive(Boolean.FALSE);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepositoryPort.save(employee);

        LOGGER.info("Event={}, employeeId={}", EmployeeConstants.EMPLOYEE_DELETED_EVENT, id);
    }

    private Employee getActiveEmployee(Long id) {
        return employeeRepositoryPort.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    private void validateUniqueEmployee(String employeeNumber, String email) {
        if (employeeRepositoryPort.existsByEmployeeNumber(employeeNumber)) {
            throw new EmployeeAlreadyExistsException("Employee already exists with employeeNumber: " + employeeNumber);
        }
        if (employeeRepositoryPort.existsByEmail(email)) {
            throw new EmployeeAlreadyExistsException("Employee already exists with email: " + email);
        }
    }

    private void validateUniqueEmployeeForUpdate(Long id, String employeeNumber, String email) {
        if (employeeRepositoryPort.existsByEmployeeNumberAndIdNot(employeeNumber, id)) {
            throw new EmployeeAlreadyExistsException("Employee already exists with employeeNumber: " + employeeNumber);
        }
        if (employeeRepositoryPort.existsByEmailAndIdNot(email, id)) {
            throw new EmployeeAlreadyExistsException("Employee already exists with email: " + email);
        }
    }

    private Employee toDomain(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(request.getEmployeeNumber());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPosition());
        employee.setHireDate(request.getHireDate());
        employee.setStatus(request.getStatus());
        employee.setSalary(request.getSalary());
        return employee;
    }

    private EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setEmployeeNumber(employee.getEmployeeNumber());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDepartment(employee.getDepartment());
        response.setPosition(employee.getPosition());
        response.setHireDate(employee.getHireDate());
        response.setStatus(employee.getStatus());
        response.setSalary(employee.getSalary());
        response.setActive(employee.getActive());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());
        return response;
    }
}
