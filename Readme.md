# TravelGuard Microservices

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.0.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.0.4" />
  <img src="https://img.shields.io/badge/Spring_Cloud-Eureka%20%26%20Gateway-6DB33F?style=for-the-badge" alt="Spring Cloud" />
  <img src="https://img.shields.io/badge/MongoDB-Auth-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB" />
  <img src="https://img.shields.io/badge/Maven-Multi-module-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
</p>

A multi-module Java microservices platform for managing travel insurance policies, user authentication, email processing, and service-to-service communication in a Spring Cloud ecosystem.

## Overview

TravelGuard is a portfolio-ready microservices project designed to showcase real-world Java backend engineering practices. It includes:

- policy management and validation for travel insurance entities
- API gateway routing and request orchestration
- service discovery and registration through Eureka
- JWT-based authentication and role authorization
- asynchronous/integration workflows for email operations
- REST client consumption for internal service communication

The platform is built with Java 17, Spring Boot 3.0.x, Spring Cloud, JPA, Flyway, H2, MongoDB, and JWT-based security.

## Architecture

```mermaid
flowchart LR
    Client[Client / Postman / Swagger UI] --> Gateway[Gateway\nSpring Cloud Gateway]
    Gateway --> Policy[Policy Service\npolicies]
    Gateway --> Auth[Auth Service\nauth]
    Gateway --> Email[Email Service\nemail]
    Gateway --> Rest[REST Consumer\nrest]

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

| Module | Responsibility | Notes |
| --- | --- | --- |
| `policies` | Business logic for policies and insured persons | Main domain service |
| `auth` | Signup, signin, JWT generation, role-based access | MongoDB-backed |
| `gateway` | Request routing and API entry point | Spring Cloud Gateway |
| `eureka` | Service registration and discovery | Spring Cloud Netflix Eureka |
| `email` | Notification and email integration logic | External integration |
| `rest` | REST client for internal API calls | Client-side integration |

## Runtime Stack

- Java 17
- Maven multi-module build
- Spring Boot 3.0.4
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
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
travelguard-microservices/
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

Before starting the services, make sure you have:

- Java 17+
- Maven 3.8+
- MongoDB running locally or via Docker
- H2 available through application configuration for local development
- IDE such as IntelliJ IDEA or VS Code

## Quick Start

From the project root:

```bash
mvn clean install
```

Start the services in the recommended order:

```bash
mvn spring-boot:run -pl eureka
mvn spring-boot:run -pl auth
mvn spring-boot:run -pl policies
mvn spring-boot:run -pl gateway
mvn spring-boot:run -pl email
```

## Core Domain Model

### Policy

The central domain object is `Policy`, containing:

- `id`
- `eligibility`
- `validity`
- `type`
- `benefitAndLimit`
- `price`
- `personList`

Example enums:

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

`Person` contains:

- `id`
- `name`
- `age`
- `nationality`

Validation annotations used in the project:

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
- local persistence is backed by H2
- Flyway manages database schemas and versioning
- a one-to-many unidirectional mapping is used between `Policy` and `Person`

## API Overview

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

### REST Client Endpoints

| Method | Description | Example |
| --- | --- | --- |
| `GET` | Get all policies | `localhost:8082/RestTemplate/getAllPolicies` |
| `GET` | Get policy by id | `localhost:8082/RestTemplate/getPolicyById/1` |
| `POST` | Create policy | `localhost:8082/RestTemplate/addPolicy` |
| `PUT` | Update policy | `localhost:8082/RestTemplate/updatePolicy` |
| `DELETE` | Delete policy | `localhost:8082/RestTemplate/deletePolicy/1` |

## Example Policy Payload

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

The project implements JWT-based authentication and authorization with role-based access control.

### Supported Roles

- `ROLE_USER`
- `ROLE_MODERATOR`
- `ROLE_ADMIN`

### Auth Flow

- sign up user
- sign in user
- validate credentials
- generate JWT token
- authorize protected endpoints based on roles

MongoDB is used for persistence of users and roles.

### Create Role Example

```javascript
db.roles.insertMany([
  { name: "ROLE_USER" },
  { name: "ROLE_MODERATOR" },
  { name: "ROLE_ADMIN" }
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

### Protected Endpoint Example

```http
GET http://localhost:8082/api/test/all
GET http://localhost:8082/api/test/user
Authorization: Bearer <token>
```

## Development Notes

- Spring AOP is used for cross-cutting concerns around service methods
- DTO mappings are handled through MapStruct
- Global exception handlers are implemented for clean API responses
- Swagger / OpenAPI is available for live API exploration
- The solution demonstrates modular microservice design with discovery, routing, and security separation

## Screenshot Section

This is the recommended area to add visuals for your portfolio.

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

### Auth Flow

```text
Add screenshot here: docs/images/auth-flow.png
```

![Auth Flow](docs/images/auth-flow.png)

## Recommended Enhancements

- add Docker Compose for local environment startup
- add a centralized configuration server
- add integration tests for auth and policy services
- add CI/CD pipeline with Maven and test reporting
- add Actuator + Prometheus + Grafana for observability

## License

This project is intended for learning, demonstration, and portfolio use.


