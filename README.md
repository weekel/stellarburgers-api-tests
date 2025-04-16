# Stellar Burgers API Tests

Automated API tests for [Stellar Burgers](https://stellarburgers.nomoreparties.site/), written in Java using JUnit 4, RestAssured, and Allure.

## What’s Covered

- User registration
- User login
- User data update (with and without authorization)
- Order creation (with valid, missing, and invalid ingredients)
- Retrieving user orders (authorized and unauthorized)

## Technologies

- Java 11+
- Maven
- JUnit 4
- RestAssured
- Allure Framework
- JavaFaker

## How to Run Tests

```bash
mvn clean test
