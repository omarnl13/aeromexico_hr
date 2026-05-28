# Employee Management Service

Microservicio REST para la gestión del ciclo de vida de empleados dentro del sistema de Recursos Humanos.

La solución permite crear, consultar, actualizar y eliminar lógicamente empleados. Fue desarrollada con enfoque de Líder Técnico Java, considerando arquitectura limpia, validaciones, manejo centralizado de errores, Swagger, logs, bitácora inicial de eventos y pruebas unitarias.

---

## Tecnologías

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- H2 Database para ejecución local
- PostgreSQL sugerido para producción
- Jakarta Validation
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Maven
- SLF4J + Logback

---

## Arquitectura

El proyecto utiliza una arquitectura limpia con enfoque hexagonal ligero.

```text
Presentation Layer
        |
Application Layer
        |
Domain Layer
        |
Infrastructure Layer
```

### Capas

- `presentation`: controladores REST, DTOs y manejo global de errores.
- `application`: servicios y casos de uso.
- `domain`: modelo de negocio, puertos y excepciones custom.
- `infrastructure`: entidades JPA, repositorios y adaptadores.
- `config`: Swagger y filtros de logging.
- `common`: constantes.

---

## Estructura

```text
src/main/java/com/aeromexico/hr/employees
├── application/service
├── common/constants
├── config
├── domain/exception
├── domain/model
├── domain/port
├── infrastructure/adapter
├── infrastructure/entity
├── infrastructure/repository
└── presentation
    ├── controller
    ├── dto
    └── handler
```

---

## Modelo de datos

Entidad principal: `Employee`.

| Campo | Tipo | Obligatorio | Descripción |
|---|---|---|---|
| id | Long | Sí | Identificador único interno |
| employeeNumber | String | Sí | Número único de empleado |
| firstName | String | Sí | Nombre |
| lastName | String | Sí | Apellido |
| email | String | Sí | Correo electrónico |
| phone | String | No | Teléfono |
| department | String | Sí | Departamento |
| position | String | Sí | Puesto |
| hireDate | LocalDate | Sí | Fecha de ingreso |
| status | EmploymentStatus | Sí | Estatus laboral |
| salary | BigDecimal | Sí | Salario |
| active | Boolean | Sí | Eliminación lógica |
| createdAt | LocalDateTime | Sí | Fecha de creación |
| updatedAt | LocalDateTime | No | Última modificación |

### EmploymentStatus

```java
ACTIVE,
INACTIVE,
SUSPENDED,
TERMINATED
```

---

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/v1/employees` | Crear empleado |
| GET | `/api/v1/employees/{id}` | Consultar empleado por ID |
| GET | `/api/v1/employees?page=0&size=10` | Consultar empleados paginados |
| PUT | `/api/v1/employees/{id}` | Actualizar empleado |
| DELETE | `/api/v1/employees/{id}` | Eliminar empleado lógicamente |

---

## Códigos HTTP

| Código | Uso |
|---|---|
| 200 OK | Consulta o actualización correcta |
| 201 Created | Empleado creado |
| 204 No Content | Eliminación lógica correcta |
| 400 Bad Request | Error de validación |
| 404 Not Found | Empleado no encontrado |
| 409 Conflict | Empleado duplicado |
| 500 Internal Server Error | Error técnico no controlado |

---

## Ejecutar localmente

### Requisitos

- Java 17
- Maven 3.8+
- Eclipse IDE con soporte Maven

### Desde terminal

```bash
mvn clean install
mvn spring-boot:run
```

### Desde Eclipse

1. Abrir Eclipse.
2. Ir a `File > Import`.
3. Seleccionar `Existing Maven Projects`.
4. Seleccionar la carpeta `employee-management-service`.
5. Esperar a que descargue dependencias Maven.
6. Ejecutar la clase `EmployeeManagementApplication` como Java Application o Spring Boot App.

---

## Swagger

Una vez levantado el servicio:

```text
http://localhost:8080/swagger-ui/index.html
```

También está disponible:

```text
http://localhost:8080/v3/api-docs
```

---

## H2 Console

```text
http://localhost:8080/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:employeesdb
User: sa
Password: vacío
```

---

## Ejemplos CURL

### Crear empleado

```bash
curl --location 'http://localhost:8080/api/v1/employees' \
--header 'Content-Type: application/json' \
--header 'x-request-id: abc-123' \
--data-raw '{
  "employeeNumber": "EMP-001",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@aeromexico.com",
  "phone": "5555555555",
  "department": "Human Resources",
  "position": "HR Analyst",
  "hireDate": "2025-06-30",
  "status": "ACTIVE",
  "salary": 35000.00
}'
```

### Consultar por ID

```bash
curl --location 'http://localhost:8080/api/v1/employees/1'
```

### Consultar paginado

```bash
curl --location 'http://localhost:8080/api/v1/employees?page=0&size=10'
```

### Actualizar empleado

```bash
curl --location --request PUT 'http://localhost:8080/api/v1/employees/1' \
--header 'Content-Type: application/json' \
--header 'x-request-id: abc-123' \
--data-raw '{
  "employeeNumber": "EMP-001",
  "firstName": "Juan Carlos",
  "lastName": "Pérez",
  "email": "juan.perez@aeromexico.com",
  "phone": "5555555555",
  "department": "Technology",
  "position": "Java Developer",
  "hireDate": "2025-06-30",
  "status": "ACTIVE",
  "salary": 45000.00
}'
```

### Eliminar empleado

```bash
curl --location --request DELETE 'http://localhost:8080/api/v1/employees/1'
```

---

## Logs y bitácora

El servicio registra:

- Método HTTP.
- URI.
- Headers recibidos.
- Código de respuesta.
- Tiempo de ejecución.
- Eventos funcionales:
  - `EMPLOYEE_CREATED`
  - `EMPLOYEE_UPDATED`
  - `EMPLOYEE_DELETED`
  - `EMPLOYEE_CONSULTED`

El header `Authorization` se enmascara en logs.

---

## Pruebas

Ejecutar:

```bash
mvn test
```

Casos cubiertos:

- Crear empleado correctamente.
- Validar empleado duplicado.
- Consultar empleado existente.
- Consultar empleado inexistente.
- Listar empleados.
- Actualizar empleado.
- Eliminar empleado lógicamente.
- Validar manejo de errores 404 y 409.

---

## Decisiones técnicas

- Se eligió Java 17 por ser LTS y compatible con Spring Boot 3.
- Se eligió una base SQL por consistencia, restricciones únicas e integridad de datos.
- Se implementó eliminación lógica para conservar trazabilidad histórica.
- Se aplicó arquitectura limpia para separar reglas de negocio de infraestructura.
- Se centralizó el manejo de errores para respuestas homogéneas.
- Se incluyó Swagger para documentar el contrato.
- Se agregó logging de headers para cumplir el requerimiento extra.
