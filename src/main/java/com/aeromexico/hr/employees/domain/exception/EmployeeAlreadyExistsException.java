package com.aeromexico.hr.employees.domain.exception;

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

public class EmployeeAlreadyExistsException extends BusinessException {

    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
