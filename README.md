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

---

## 💻 Technologies

- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Validation
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

To run the project use simple Maven commands:

mvn clean install
mvn spring-boot:run

Then API should be available at `http://localhost:8080`

---

## 🌐 Example API Request

GET /api/weather/best-location?date=2025-07-03
✅ Response 200 OK

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

---
