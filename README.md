# customer-review-service
>  This app is responsible for creating customer reviews

[![codecov](https://codecov.io/gh/karimbkb/customer-review/branch/master/graph/badge.svg?token=IQPV5TS926)](https://codecov.io/gh/karimbkb/customer-review)

[![Build Status](https://app.travis-ci.com/karimbkb/customer-review.svg?branch=master)](https://app.travis-ci.com/karimbkb/customer-review)

## Contents

- [Setup](#setup)
- [Dependencies](#dependencies)
- [Endpoints](#endpoints)
- [Swagger](#swagger)
- [Unit Tests](#unit-tests)
- [Code Coverage](#code-coverage)
- [Static Code Analyzer](#static-code-analyzer)

## Setup

Go into the root directory of the application and run

```
cd customer-review-service
./dev/setup.sh
```

Start the application via you IDE or run the jar file and you can access the api at  `http://localhost:8080/`

## Dependencies

- Java 11
- Gradle 7.1
- JUnit 5
- Spring Boot 2.5
- TestContainers
- JUnit
- Consul

## Endpoints

| Action            | Endpoint                                                            | Type     | Example                                                                       | Payload                                   |
|-------------------|---------------------------------------------------------------------|----------|-------------------------------------------------------------------------------|-------------------------------------------|
| Get a review      | `/api/v1/review/{id}`                         | `GET`    | `/api/v1/review/08915530-12d5-4d4e-9edf-5339c523ed29`             | -                                         |
| Remove a review | `/api/v1/review/{id}`                              | `DELETE` | `/api/v1/review/08915530-12d5-4d4e-9edf-5339c523ed29`               | -                                         |
| Create a review | `/api/v1/review/`                               | `POST`   | `/api/v1/review/`                | `{ "storeId": 1, "productId": "93ae5569-93c7-4e2d-8415-ad1f0254f635" }`                                  |
| Update a review | `/api/v1/review/{id}`                               | `PATCH`  | `/v1/review/08915530-12d5-4d4e-9edf-5339c523ed29`               | `[ { "op": "replace", "path": "/storeId", "value": 4 }, { "op": "replace", "path": "/productId", "value": "225925f1-f16e-4572-9a3a-0c76182819d4" } ] ` |

## Consul

Consul can be accessed locally via http://localhost:8500/

## Unit Tests

To execute Unit Tests run:

```
./gradlew test
```

## Code Coverage

To check if the code coverage ratio was reached run this command:

**INFO: You have to run `./gradlew test` first!**

```
./gradlew jacocoTestCoverageVerification
```

To create a code coverage report run this command:

```
./gradlew jacocoTestReport
```

## Static Code Analyzer

Spotbugs was used in this project. (https://spotbugs.github.io/)

```
./gradlew spotbugsMain
```
