package com.aeromexico.hr.employees.infrastructure.adapter;

import com.aeromexico.hr.employees.domain.model.Employee;
import com.aeromexico.hr.employees.domain.port.EmployeeRepositoryPort;
import com.aeromexico.hr.employees.infrastructure.entity.EmployeeEntity;
import com.aeromexico.hr.employees.infrastructure.repository.EmployeeJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EmployeeRepositoryAdapter implements EmployeeRepositoryPort {

    private final EmployeeJpaRepository employeeJpaRepository;

    public EmployeeRepositoryAdapter(EmployeeJpaRepository employeeJpaRepository) {
        this.employeeJpaRepository = employeeJpaRepository;
    }

    @Override
    public Employee save(Employee employee) {
        return toDomain(employeeJpaRepository.save(toEntity(employee)));
    }

    @Override
    public Optional<Employee> findByIdAndActiveTrue(Long id) {
        return employeeJpaRepository.findByIdAndActiveTrue(id).map(this::toDomain);
    }

    @Override
    public Page<Employee> findAllByActiveTrue(Pageable pageable) {
        return employeeJpaRepository.findAllByActiveTrue(pageable).map(this::toDomain);
    }

    @Override
    public boolean existsByEmployeeNumber(String employeeNumber) {
        return employeeJpaRepository.existsByEmployeeNumber(employeeNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmployeeNumberAndIdNot(String employeeNumber, Long id) {
        return employeeJpaRepository.existsByEmployeeNumberAndIdNot(employeeNumber, id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return employeeJpaRepository.existsByEmailAndIdNot(email, id);
    }

    private EmployeeEntity toEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(employee.getId());
        entity.setEmployeeNumber(employee.getEmployeeNumber());
        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setEmail(employee.getEmail());
        entity.setPhone(employee.getPhone());
        entity.setDepartment(employee.getDepartment());
        entity.setPosition(employee.getPosition());
        entity.setHireDate(employee.getHireDate());
        entity.setStatus(employee.getStatus());
        entity.setSalary(employee.getSalary());
        entity.setActive(employee.getActive());
        entity.setCreatedAt(employee.getCreatedAt());
        entity.setUpdatedAt(employee.getUpdatedAt());
        return entity;
    }

    private Employee toDomain(EmployeeEntity entity) {
        return new Employee(
                entity.getId(),
                entity.getEmployeeNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getDepartment(),
                entity.getPosition(),
                entity.getHireDate(),
                entity.getStatus(),
                entity.getSalary(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
