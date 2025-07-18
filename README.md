# 🌊 Windsurfers Weather API

**Windsurfers Weather** is a Spring Boot-based backend application that fetches weather data from the Weatherbit API and determines the best location for windsurfing based on predefined global spots.

---

## 🧭 Project Description

The application retrieves weather forecasts in 16 days range for five predefined global locations and calculates a **surfing score** based on:

- Wind speed
- Temperature

The location with the best surfing conditions is returned for the selected date.

---

## ⚙️ Features

- [x] Fetching forecast data from the Weatherbit API
- [x] Surfing score calculation based on weather conditions
- [x] Exception handling (e.g., invalid API key, missing data)
- [x] Unit tests for the service and API layer
- [x] Integration tests for the controller and API layer
- [x] Validation using `@Valid` and Jakarta Bean Validation
- [x] Profile-based test configurations (`dev`, `test`)
- [x] **Kafka integration** – REST endpoint that publishes messages to Kafka topics
- [x] **Mock controller for best location** in the `dev` profile – always returns Jastarnia
- [x] **Database integration with Liquibase** – schema and data versioning
---

## 💻 Technologies

- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Validation
- Kafka (Spring for Apache Kafka)
- Liquibase
- H2 / PostgreSQL (configurable)
- JUnit 5
- Mockito
- Maven
- Lombok

---

## ▶️ Running the project

Application uses Weatherbit API Key which you can edit in `application.yml`:

```
weatherbit:
  api:
    url: https://api.weatherbit.io/v2.0/forecast/daily
    key: YOUR_API_KEY_HERE
```
## 🐳 Start Docker infrastructure
The project uses Docker Compose to start required services:

- PostgreSQL database
- Apache Kafka
- Apache Zookeeper (Kafka dependency)

 Run:
```
docker-compose up -d
```
Once the infrastructure is up, you can run the application with:
```
mvn clean install
mvn spring-boot:run
```
Then API should be available at `http://localhost:8080`

---

## 🔁 Active Profiles

You can launch the app with different profiles using:
```
-Dspring.profiles.active=dev
```

**Dev** profile	enables a mock controller that always returns Jastarnia as the best location. Used for frontend integration and local testing.

**Test** profile loads in-memory test configuration and H2 database.

**Default** fetches real weather data from Weatherbit API.

---

## 🧪 Kafka Integration

The application exposes a REST endpoint to send custom messages to Apache Kafka.
```
{
  "login": "surfer42",
  "message": "Waves are perfect today!"
}
```
📦 Expected Response:
```
Message sent to Kafka.
```
Kafka configuration is defined in application.yml and supports sending simple payloads to the configured topic.

---

## 🌐 Example API Request

GET /api/weather/best-location?date=2025-07-03

✅ Response 200 OK

```
{
  "location": {
    "name": "Fortaleza",
    "country": "Brazil",
    "lat": -3.7318,
    "lon": -38.5266
  },
  "data": {
    "temperature": 26.4,
    "windSpeed": 10.1,
    "windDirection": 114.0
  },
  "score": 56.7
}
```
---

## 🧩 Liquibase Support
This project uses Liquibase for schema migrations.

On application startup, Liquibase automatically applies schema definitions and inserts initial data (e.g., list of predefined surf locations).

Files are located in:
```
src/main/resources/db/changelog/
```
---
