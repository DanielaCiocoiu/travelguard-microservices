Travel-insurance application

- [Features](#features)
    - [Policy](#policy)
      - [Person](#person)
        - [RestTemplate](#personRestTemplate)

## Features

### Policy

| Operation |         Description          |                         Example | Request body |
|-----------|:----------------------------:|--------------------------------:|--:|
| GET       |       Get all policies       |     localhost:8080/api/policies |            - |
| GET       |      Get a policy by id      |     localhost:8080/api/policy/1 |            - |
| GET       |   Get all policies by type   |       localhost:8080/api/type   |            - |
| GET       | Get all policies by validity |     localhost:8080/api/validity |            - |
| POST      |       Create a policy        |       localhost:8080/api/policy |       Policy |
| PUT       |       Update a policy        |       localhost:8080/api/policy |       Policy |
| PATCH     |  Update price for a policy   | localhost:8080/api/policy/9/250 |            - |
| DELETE    |       Delete a policy        |    localhost:8080/api/policy/1/ |            - |
| GET       |   Get persons export to excel|localhost:8080/api/exportToExcel |              |

### Person

| Operation |           Description           |                                 Example | Request body |
|-----------|:-------------------------------:|----------------------------------------:|-------------:|
| GET       |         Get all persons         |              localhost:8080/api/persons |            - |
| GET       |       Get a person by id        |             localhost:8080/api/person/1 |            - |
| POST      |         Create a person         |               localhost:8080/api/person |       Person |
| PUT       |         Update a person         |               localhost:8080/api/person |       Person |
| PATCH     |    Update name for a person     |     localhost:8080/api/persons/9/Ilinca |            - |
| DELETE    |         Delete a person         |            localhost:8080/api/person/1/ |            - |
| GET       | Get persons sort alphabetically |         localhost:8080/api/persons/sort |            - |
| GET       |       Get persons by page       |         localhost:8080/api/persons/page |            - |
| GET       |   Get persons by nationality    | localhost:8080/api/nationality/american |            - |

### RestTemplate Policy

| Operation |           Description           |                                     Example | Request body |
|-----------|:-------------------------------:|--------------------------------------------:|-------------:|
| GET       |         Get all persons         |  localhost:8080/RestTemplate/getAllPolicies |            - |
| GET       |       Get a person by id        | localhost:8081/RestTemplate/getPolicyById/1 |            - |
| POST      |         Create a person         |       localhost:8080/RestTemplate/addPolicy |       Policy |
| PUT       |         Update a person         |    localhost:8080/RestTemplate/updatePolicy |       Policy |
| DELETE    |         Delete a person         |  localhost:8080/RestTemplate/deletePolicy/1 |            - |

