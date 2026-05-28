package com.aeromexico.hr.employees.presentation.handler;

import com.aeromexico.hr.employees.domain.exception.EmployeeAlreadyExistsException;
import com.aeromexico.hr.employees.domain.exception.EmployeeNotFoundException;
import com.aeromexico.hr.employees.presentation.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleEmployeeNotFoundShouldReturnNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/employees/1");

        ResponseEntity<ErrorResponse> response = handler.handleEmployeeNotFound(
                new EmployeeNotFoundException(1L), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void handleEmployeeAlreadyExistsShouldReturnConflict() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/employees");

        ResponseEntity<ErrorResponse> response = handler.handleEmployeeAlreadyExists(
                new EmployeeAlreadyExistsException("Employee already exists"), request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
    }
}
