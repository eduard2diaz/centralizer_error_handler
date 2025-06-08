# Error Handler

A Spring Boot-compatible library for capturing, processing, and forwarding errors in a centralized and configurable way. It allows microservices to automatically catch and format exceptions and either send them to an external REST endpoint or publish them to a Kafka topic — based on runtime configuration.

---

## 📦 Features

- ✅ Global exception handling via `@ControllerAdvice`
- ✅ Structured error response using a custom `ApiError` wrapper
- ✅ Pluggable error processors (`Kafka`, `REST`, or others)
- ✅ Uses Spring profiles for activation
- ✅ Optional Kafka dependency
- ✅ Environment-driven configuration
- ✅ Designed for reuse across multiple microservices

---

## 🚀 Getting Started

### 1. Add the dependency

If published to a Maven repository:

```xml
<dependency>
  <groupId>com.cloud</groupId>
  <artifactId>error_handler</artifactId>
  <version>1.0.0-alpha</version>
</dependency>
```

### 2. Enable one of the profiles
Choose one of the following Spring profiles:
* error-endpoint → Sends errors to an external HTTP endpoint
* error-kafka → Publishes errors to a Kafka topic

```properties
spring.profiles.active=error-endpoint
```

or 

```properties
spring.profiles.active=error-kafka
```

## ⚙️ Configuration
🛠️ For REST-based error forwarding

```properties
spring.profiles.active=error-endpoint
error.rest.endpoint.url=http://external-service/api/errors
```

This will send an HTTP POST request to the given endpoint with the error payload. In case
of a Spring Boot app, it should look like

```java
package com.cloud.external_error_handler;
import org.springframework.web.bind.annotation.*;
import response.ApiError;

@RestController
@RequestMapping("api")
public class ErrController {

    @PostMapping("/errors")
    public void errors(@RequestBody ApiError error) {
        // process the request
    }
}
```

🛠️ For Kafka-based error publishing

```properties
spring.profiles.active=error-kafka
error.kafka.topic=centralized-errors # Use the topic of your choice
spring.kafka.bootstrap-servers=${KAFKA_SERVERS:localhost:9092}
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.properties.spring.json.add.type.headers=false # (optional, to prevent unnecessary metadata)
```

## 🧩 How It Works

### Centralized Exception Handling
The library defines a global exception handler using @ControllerAdvice. This captures common exceptions like IOException, IllegalArgumentException, and Exception.

### Error Representation
Errors are converted into an ApiError object and wrapped into a generic Response<T>:

```json
{
	"success": false,
	"apiError": {
		"status": 500,
		"timestamp": "07-06-2025 01:37:38",
		"message": "Some error",
		"debugMessage": "Exception details",
		"subErrors": []
	},
	"data": null
}
```

### Error Dispatching
The library uses an interface called ErrorProcessorService, which is implemented by:
* KafkaProcessorService (active only in profile error-kafka)
* RestProcessorService (active only in profile error-endpoint)

Each implementation sends the error to the appropriate destination.


## 🧰 Optional Kafka Dependency
To avoid forcing Kafka dependencies on consumers, Kafka is marked as optional in the library’s pom.xml:

```xml
<dependency>
	<groupId>org.springframework.kafka</groupId>
	<artifactId>spring-kafka</artifactId>
	<optional>true</optional>
</dependency>
```

## 📃 License
MIT License – Use freely, improve collaboratively.
