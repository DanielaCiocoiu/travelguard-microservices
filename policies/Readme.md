Travel-insurance application

Travel_Insurance it is a REST API which exposes endpoints for each use case, using  Spring Boot,  
a microservice that handle CRUD operations for managing Travel Insurance policies. 
The project it is set up with Maven, Java 17, Spring Boot 3.1.2., Flyway Community Edition 9, Swagger.
Microservice is based on policy model which has following: id, eligibility, validity, type, benefitAndLimit, price and a list of persons. 
Type and benefits are enums:
BenefitAndLimit 
    ACCIDENT_RISK, LAISURE_ACTIVITIES, SPORTS, HAZARDOUS_WORK;
Type 
    VISITOR_INSURANCE, SCHENGEN_INSURANCE, GROUP_TRAVEL_INSURANCE,
    INTERNATIONAL_TRAVEL_INSURANCE, WORLDWIDE_HEALTH_INSURANCE,
    BUSINESS_TRAVEL_INSURANCE,MULTI_TRIP_INSURANCE,
    MISSIONARY_INSURANCE, PRE_EXISTING_CONDITION_INSURANCE;

The second model is Persons with following attribute: id; name, age, nationality.

Some attributes are using Java Bean Validation with Hibernate Validator
For name: 
@NotBlank
@Size(max = 20, message = "First name should have with maximum length of 200 characters")

Age
@Positive
@NotNull(message = "Age must not be null")
@Min(value = 18, message = "Age must be greater than or equal to 18")
@Max(value = 60, message = "Age must be less than or equal to 60")


The app uses Spring Data Jpa to store data in a relational database H2, using Flyway versioning tool which keeps track of all changes to database. 

App is based on one-to-many unidirectional mapping using JPA annotations, which means one policy can have many persons.

The application provide implementation for the following use cases:

- [Features](#features)
    - [Policy](#policy)
        - [Person](#person)
            - [RestTemplate](#personRestTemplate)
             
## Features

### Policy


| Operation |         Description          |                         Example | Request body |
|-----------|:----------------------------:|--------------------------------:|-------------:|
| GET       |       Get all policies       |     localhost:8082/api/policies |            - |
| GET       |      Get a policy by id      |     localhost:8082/api/policy/1 |            - |
| GET       |   Get all policies by type   |       localhost:8082/api/type   |            - |
| GET       | Get all policies by validity |     localhost:8082/api/validity |            - |
| POST      |       Create a policy        |       localhost:8082/api/policy |       Policy |
| PUT       |       Update a policy        |       localhost:8082/api/policy |       Policy |
| PATCH     |  Update price for a policy   | localhost:8082/api/policy/9/250 |            - |
| DELETE    |       Delete a policy        |    localhost:8082/api/policy/1/ |            - |
| GET       |   Get persons export to excel|localhost:8082/api/exportToExcel |            - |

### Person

| Operation |           Description           |                                 Example | Request body |
|-----------|:-------------------------------:|----------------------------------------:|-------------:|
| GET       |         Get all persons         |              localhost:8082/api/persons |            - |
| GET       |       Get a person by id        |             localhost:8082/api/person/1 |            - |
| POST      |         Create a person         |               localhost:8082/api/person |       Person |
| PUT       |         Update a person         |               localhost:8082/api/person |       Person |
| PATCH     |    Update name for a person     |     localhost:8082/api/persons/9/Ilinca |            - |
| DELETE    |         Delete a person         |            localhost:8082/api/person/1/ |            - |
| GET       | Get persons sort alphabetically |         localhost:8082/api/persons/sort |            - |
| GET       |       Get persons by page       |         localhost:8082/api/persons/page |            - |
| GET       |   Get persons by nationality    | localhost:8082/api/nationality/american |            - |

### RestTemplate Policy

| Operation |           Description           |                                     Example | Request body |
|-----------|:-------------------------------:|--------------------------------------------:|-------------:|
| GET       |         Get all persons         |  localhost:8082/RestTemplate/getAllPolicies |            - |
| GET       |       Get a person by id        | localhost:8082/RestTemplate/getPolicyById/1 |            - |
| POST      |         Create a person         |       localhost:8082/RestTemplate/addPolicy |       Policy |
| PUT       |         Update a person         |    localhost:8082/RestTemplate/updatePolicy |       Policy |
| DELETE    |         Delete a person         |  localhost:8082/RestTemplate/deletePolicy/1 |            - |


Spring-aspects: some advices before and after Policy service methods
Dto objects with MapStruct library that generate bean mapper classes automatically.
Error handler for both policy and person models.

Rest Template client which consumes endpoints from Travel_Insurance.

Swagger Open Api and Postman, For execute Crud operations and document requests 

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

SecurityConfig implement with Spring Boot JWT Authentication
User can signup new account, or login with username & password.
By User’s role (admin, moderator, user), we authorize the User to access resources

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(username,email,password) VALUES('john', 'john@gmail.com','12345678');
INSERT INTO users(username,email,password) VALUES('mary', 'mary@gmail.com','12345678');
INSERT INTO users(username,email,password) VALUES('susan', 'susan@gmail.com','12345678');

POST: http://localhost:8082/api/auth/signup
{
"username": "Vasia",
"email": "Vasia@gmail.com",
"password": "12345678",
"role": ["admin"]
}

{
"username": "Tom",
"password": "12345678",
"email": "Tom@gmail.com",
"role": ["moderator"]
}
{
"username": "Livia",
"password": "12345678",
"email": "Livia@gmail.com",
"role": ["user"]
}

GET: http://localhost:8082/api/test/all

GET: http://localhost:8082/api/test/user

POST: http://localhost:8082/api/auth/signin
{
"username": "Vasia",
"password": "12345678"
}
                        Response
{
"id": 1,
"username": "Vasia",
"email": "Vasia@gmail.com",
"roles": [
"ROLE_ADMIN"
],
"tokenType": "Bearer",
"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJWYXNpYSIsImlhdCI6MTY5NTI4MjIwOSwiZXhwIjoxNjk1MzY4NjA5fQ.IgyrMsIz3zgttouHP7FGVYJM3ILA_S7_NHEQzG-_MvM"
}
GET: http://localhost:8082/api/test/user with Bearer token.... 


Eureka: http://localhost:8761/
Eureka server application and it is a spring cloud managed service. 
Therefore it has the similar nature as other microservices. In microservice architecture, every microservice(spring 
cloud service) should be registered themselves with Eureka server to make them discoverable (through Eureka server).