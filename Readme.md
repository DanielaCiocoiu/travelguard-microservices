# Travel Insurance Microservices

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.0.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.0.4" />
  <img src="https://img.shields.io/badge/Spring_Cloud-Eureka%20%26%20Gateway-6DB33F?style=for-the-badge" alt="Spring Cloud" />
  <img src="https://img.shields.io/badge/MongoDB-Auth-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB" />
  <img src="https://img.shields.io/badge/Maven-Multi-module-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
</p>

A multi-module Spring Boot microservices project for travel insurance management, built around policy processing, API gateway routing, service discovery, JWT authentication, and backend integration services.

## Overview

This repository demonstrates a hands-on microservices architecture for a travel insurance domain. It includes:

- a business service for insurance policies and insured persons
- client-side REST consumption via `rest`
- secure authentication and authorization via `auth`
- central service registration with `eureka`
- request routing through `gateway`
- email-related processing through `email`

The solution uses Java 17, Spring Boot 3.0.x, Spring Cloud, JPA, Flyway, H2, MongoDB, and JWT-based security.

## Architecture

```mermaid
flowchart LR
    Client[Client / Postman / Swagger UI] --> Gateway[Gateway\nSpring Cloud Gateway]
    Gateway --> Policy[policies\nPolicy Service]
    Gateway --> Auth[auth\nJWT Auth Service]
    Gateway --> Email[email\nEmail Service]
    Gateway --> Rest[rest\nREST Client Service]

    Policy --> DB[(H2 / Relational DB)]
    Policy --> Flyway[Flyway Migrations]
    Auth --> Mongo[(MongoDB)]
    Rest --> Policy

    EUREKA[Eureka Server] --> Policy
    EUREKA --> Auth
    EUREKA --> Email
    EUREKA --> Rest
    EUREKA --> Gateway
```

## Module Responsibilities

| Module | Responsibility | Port / Role |
| --- | --- | --- |
| `policies` | Policy and person CRUD, validation, business rules | Business logic service |
| `auth` | User registration, login, JWT generation, role-based access | Security service |
| `gateway` | API routing, request forwarding, centralized entry point | Gateway |
| `eureka` | Service discovery and registration | Registry server |
| `email` | Email processing and notification workflows | Integration service |
| `rest` | REST client for consuming APIs from other modules | Consumer service |

## Runtime Stack

- Java 17
- Maven multi-module build
- Spring Boot 3.0.4
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Security
- JWT
- JPA / Hibernate
- Hibernate Validator
- Flyway
- H2 Database
- MongoDB
- Swagger / OpenAPI
- MapStruct

## Project Structure

```text
Travel-Insurance/
├── pom.xml
├── Readme.md
├── .gitignore
├── auth/
│   ├── pom.xml
│   └── src/
├── email/
│   ├── pom.xml
│   └── src/
├── eureka/
│   ├── pom.xml
│   └── src/
├── gateway/
│   ├── pom.xml
│   └── src/
├── policies/
│   ├── pom.xml
│   └── src/
├── rest/
│   ├── pom.xml
│   └── src/
├── .idea/
└── target/
```

## Prerequisites

Before starting the services, install and verify the following:

- Java 17 or later
- Maven 3.8+
- MongoDB running locally or in Docker
- H2 available through the app configuration for local development
- IDE such as IntelliJ IDEA or VS Code

## Environment Configuration

The project is organized as a multi-service application. Typical local setup includes:

- `eureka` service registry
- `gateway` entrypoint for routed HTTP requests
- `auth` service using MongoDB for credentials and roles
- `policies` service using JPA/Hibernate + Flyway

If you are using the default local configuration, the services are expected to be started in the following order:

```bash
mvn clean install
mvn spring-boot:run -pl eureka
mvn spring-boot:run -pl auth
mvn spring-boot:run -pl policies
mvn spring-boot:run -pl gateway
mvn spring-boot:run -pl email
```

You can also run the submodules individually using their local Maven wrappers (`mvnw`) where available.

## Core Domain Model

### Policy

The main business object is `Policy` with the following fields:

- `id`
- `eligibility`
- `validity`
- `type`
- `benefitAndLimit`
- `price`
- `personList`

Enum examples:

```java
BenefitAndLimit:
- ACCIDENT_RISK
- LAISURE_ACTIVITIES
- SPORTS
- HAZARDOUS_WORK

Type:
- VISITOR_INSURANCE
- SCHENGEN_INSURANCE
- GROUP_TRAVEL_INSURANCE
- INTERNATIONAL_TRAVEL_INSURANCE
- WORLDWIDE_HEALTH_INSURANCE
- BUSINESS_TRAVEL_INSURANCE
- MULTI_TRIP_INSURANCE
- MISSIONARY_INSURANCE
- PRE_EXISTING_CONDITION_INSURANCE
```

### Person

The `Person` model typically contains:

- `id`
- `name`
- `age`
- `nationality`

Validation rules used in the project:

```java
@NotBlank
@Size(max = 20, message = "First name should have maximum length of 20 characters")
@Positive
@NotNull(message = "Age must not be null")
@Min(value = 18, message = "Age must be greater than or equal to 18")
@Max(value = 60, message = "Age must be less than or equal to 60")
```

## Persistence Design

- `policies` uses Spring Data JPA and Hibernate
- relational data is stored in H2 for local development
- Flyway is used for schema versioning and migration tracking
- a one-to-many unidirectional mapping is used between `Policy` and `Person`

## Endpoint Overview

### Policy Endpoints

| Method | Description | Example |
| --- | --- | --- |
| `GET` | Get all policies | `localhost:8082/api/policies` |
| `GET` | Get policy by id | `localhost:8082/api/policy/1` |
| `GET` | Get policies by type | `localhost:8082/api/type` |
| `GET` | Get policies by validity | `localhost:8082/api/validity` |
| `POST` | Create policy | `localhost:8082/api/policy` |
| `PUT` | Update policy | `localhost:8082/api/policy` |
| `PATCH` | Update price | `localhost:8082/api/policy/9/250` |
| `DELETE` | Delete policy | `localhost:8082/api/policy/1` |
| `GET` | Export persons to Excel | `localhost:8082/api/exportToExcel` |

### Person Endpoints

| Method | Description | Example |
| --- | --- | --- |
| `GET` | Get all persons | `localhost:8082/api/persons` |
| `GET` | Get person by id | `localhost:8082/api/person/1` |
| `POST` | Create person | `localhost:8082/api/person` |
| `PUT` | Update person | `localhost:8082/api/person` |
| `PATCH` | Update person name | `localhost:8082/api/persons/9/Ilinca` |
| `DELETE` | Delete person | `localhost:8082/api/person/1` |
| `GET` | Sort persons alphabetically | `localhost:8082/api/persons/sort` |
| `GET` | Get persons by page | `localhost:8082/api/persons/page` |
| `GET` | Get persons by nationality | `localhost:8082/api/nationality/american` |

### RestTemplate Consumer Endpoints

| Method | Description | Example |
| --- | --- | --- |
| `GET` | Get all policies | `localhost:8082/RestTemplate/getAllPolicies` |
| `GET` | Get policy by id | `localhost:8082/RestTemplate/getPolicyById/1` |
| `POST` | Create policy | `localhost:8082/RestTemplate/addPolicy` |
| `PUT` | Update policy | `localhost:8082/RestTemplate/updatePolicy` |
| `DELETE` | Delete policy | `localhost:8082/RestTemplate/deletePolicy/1` |

## Example Request Body

```json
{
  "id": 21,
  "eligibility": 1,
  "validity": true,
  "type": "VISITOR_INSURANCE",
  "benefitAndLimit": "ACCIDENT_RISK",
  "price": 50.0,
  "personList": [
    {
      "id": 21,
      "name": "Ana Ion",
      "age": 23,
      "nationality": "romanian"
    }
  ]
}
```

## Security Model

The application implements JWT-based authentication and a role-based access model.

### Supported Roles

- `ROLE_USER`
- `ROLE_MODERATOR`
- `ROLE_ADMIN`

### Auth Service Behavior

- user sign-up
- user sign-in
- JWT token generation
- role-based endpoint authorization
- password validation / confirmation flow

The `auth` module uses MongoDB to store user data and roles.

### MongoDB Seed Examples

```javascript
db.roles.insertMany([
  { name: "ROLE_USER" },
  { name: "ROLE_MODERATOR" },
  { name: "ROLE_ADMIN" }
])

db.users.insertMany([
  { username: "Vasia", email: "Vasia@gmail.com", password: "12345678" },
  { username: "Ina", email: "Ina@gmail.com", password: "12345678" },
  { username: "Maia", email: "Maia@gmail.com", password: "12345678" }
])
```

### Sign Up Request

```http
POST http://localhost:8082/api/auth/signup
Content-Type: application/json
```

```json
{
  "username": "Irina",
  "email": "Irina@gmail.com",
  "password": "Ma@@word123&*",
  "confirmPassword": "Ma@@word123&*",
  "role": ["admin"]
}
```

### Sign In Request

```http
POST http://localhost:8082/api/auth/signin
Content-Type: application/json
```

```json
{
  "username": "Irina",
  "password": "Ma@@word123&*"
}
```

### Token Response Example

```json
{
  "id": "65532ff56d3b647f09d4a131",
  "username": "Irina",
  "email": "Irina@gmail.com",
  "roles": [
    "ROLE_ADMIN"
  ],
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJpc0FkbWluIjp0cnVlLCJzdWIiOiJJcmluYSIsImlhdCI6MTY5OTk1MzUwMCwiZXhwIjoxNjk5OTU1MzAwfQ.QdSD97piiyTBTJDp1hxKhT7TcZ0k2vpiOf58byz8T7Y",
  "tokenType": "Bearer"
}
```

### Protected Endpoint Usage

```http
GET http://localhost:8082/api/test/all
GET http://localhost:8082/api/test/user
Authorization: Bearer <token>
```

## Development Notes

- Use `MapStruct` for DTO-to-entity mapping
- Use Spring AOP for cross-cutting concerns and method-level interception
- Use global exception handlers for clean domain error responses
- Swagger/OpenAPI is available for API exploration and testing
- The project is structured to demonstrate microservice communication and modular separation of concerns

## Screenshot Section

Below is a placeholder section for adding screenshots relevant to the project.

### Architecture Overview

```text
Add screenshot here: docs/images/architecture-overview.png
```

![Architecture Overview](docs/images/architecture-overview.png)

### Swagger UI

```text
Add screenshot here: docs/images/swagger-ui.png
```

![Swagger UI](docs/images/swagger-ui.png)

### Eureka Dashboard

```text
Add screenshot here: docs/images/eureka-dashboard.png
```

![Eureka Dashboard](docs/images/eureka-dashboard.png)

### Auth Flow / API Testing

```text
Add screenshot here: docs/images/auth-flow.png
```

![Auth Flow](docs/images/auth-flow.png)

## Recommended Next Steps

- add Docker Compose for local orchestration
- configure centralized configuration server
- add integration tests for `policies` and `auth`
- add CI/CD pipeline with Maven and test reports
- add observability with Actuator, Prometheus, and Grafana

## License

This project is intended for learning, training, and demonstration purposes.


