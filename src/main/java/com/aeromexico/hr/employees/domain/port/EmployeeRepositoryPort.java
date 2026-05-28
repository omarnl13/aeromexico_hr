package com.aeromexico.hr.employees.domain.port;

import com.aeromexico.hr.employees.domain.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeRepositoryPort {

    Employee save(Employee employee);

    Optional<Employee> findByIdAndActiveTrue(Long id);

    Page<Employee> findAllByActiveTrue(Pageable pageable);

    boolean existsByEmployeeNumber(String employeeNumber);

    boolean existsByEmail(String email);

    boolean existsByEmployeeNumberAndIdNot(String employeeNumber, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
