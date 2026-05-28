package com.aeromexico.hr.employees.domain.exception;

public class EmployeeAlreadyExistsException extends BusinessException {

    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
