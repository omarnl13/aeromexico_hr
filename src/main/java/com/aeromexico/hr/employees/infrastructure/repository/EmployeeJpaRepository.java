package com.aeromexico.hr.employees.infrastructure.repository;

import com.aeromexico.hr.employees.infrastructure.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

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

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByIdAndActiveTrue(Long id);

    Page<EmployeeEntity> findAllByActiveTrue(Pageable pageable);

    boolean existsByEmployeeNumber(String employeeNumber);

    boolean existsByEmail(String email);

    boolean existsByEmployeeNumberAndIdNot(String employeeNumber, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
