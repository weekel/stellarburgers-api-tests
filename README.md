# 🔌 Stellar Burgers API Tests

This project contains **automated API tests** for the Stellar Burgers backend.  
It verifies the functionality and reliability of RESTful endpoints responsible for user authentication, order creation, and ingredient management.

---

## 📁 Project Structure

- `src/main/java/site/nomoreparties/stellarburgers/client` – API client classes using Rest Assured
- `src/main/java/site/nomoreparties/stellarburgers/model` – Data models (POJOs)
- `src/main/java/site/nomoreparties/stellarburgers/assertations` – Assertion helpers
- `src/test/java/site/nomoreparties/stellarburgers/` – Test classes

---

## ✅ Technologies Used

- **Java 11**
- **JUnit 4** — test framework
- **Rest Assured** — for HTTP request testing
- **Allure** — for advanced test reporting
- **Faker** — for generating test data
- **Maven** — for dependency and build management

---

## 🚀 How to Run Tests

Make sure you have **Java 11+** and **Maven** installed. Then run:

```bash
mvn clean test
```

To generate the Allure report:

```bash
mvn allure:report
```

To serve the report locally:

```bash
mvn allure:serve
```

---

## 📦 Features Covered

- User registration and login
- Token-based authentication
- Creating and retrieving orders
- Verifying ingredient lists
- Negative test cases for invalid requests

---

## 📄 License

This is a training project and is not intended for production use.
