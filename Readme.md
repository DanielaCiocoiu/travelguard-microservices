Travel-insurance application

- [Features](#features)
    - [Person](#person)
    - [Policy](#policy)
    - [RestTemplate](#personRestTemplate)

## Features

### Person

| Operation |           Description           |                                 Example | Request body |
|-----------|:-------------------------------:|----------------------------------------:|-------------:|
| GET       |         Get all persons         |              localhost:8080/api/persons |            - |
| GET       |       Get a person by id        |             localhost:8081/api/person/1 |            - |
| POST      |         Create a person         |               localhost:8081/api/person |       Person |
| PUT       |         Update a person         |               localhost:8081/api/person |       Person |
| PATCH     |    Update name for a person     |     localhost:8080/api/persons/9/Ilinca |            - |
| DELETE    |         Delete a person         |            localhost:8081/api/person/1/ |            - |
| GET       | Get persons sort alphabetically |         localhost:8081/api/persons/sort |            - |
| GET       |       Get persons by page       |         localhost:8081/api/persons/page |            - |
| GET       |   Get persons by nationality    | localhost:8081/api/nationality/american |            - |
| GET       |   Get persons export to excel   |        localhost:8081/api/exportToExcel |            - |

### Policy

| Operation |         Description          |                         Example | Request body |
|-----------|:----------------------------:|--------------------------------:|-------------:|
| GET       |       Get all policies       |     localhost:8080/api/policies |            - |
| GET       |      Get a policy by id      |     localhost:8081/api/policy/1 |            - |
| GET       |   Get all policies by type   |       localhost:8080/api/type   |            - |
| GET       | Get all policies by validity |     localhost:8080/api/validity |            - |
| POST      |       Create a policy        |       localhost:8081/api/policy |       Policy |
| PUT       |       Update a policy        |       localhost:8081/api/policy |       Policy |
| PATCH     |  Update price for a policy   | localhost:8080/api/policy/9/250 |            - |
| DELETE    |       Delete a policy        |    localhost:8081/api/policy/1/ |            - |

### RestTemplate Person

| Operation |           Description           |                                      Example | Request body |
|-----------|:-------------------------------:|---------------------------------------------:|-------------:|
| GET       |         Get all persons         |    localhost:8080/RestTemplate/getAllPersons |            - |
| GET       |       Get a person by id        |  localhost:8081/RestTemplate/getPersonById/1 |            - |
| POST      |         Create a person         |        localhost:8080/RestTemplate/addPerson |       Person |
| PUT       |         Update a person         |     localhost:8080/RestTemplate/updatePerson |       Person |
| DELETE    |         Delete a person         |   localhost:8080/RestTemplate/deletePerson/1 |            - |

