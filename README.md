# 🚀 Instant Payment API

An instant payment API built with **Spring Boot 3**, **PostgreSQL**, **Kafka**, and **Jib** featuring:
- ✅ Balance checks before transactions
- ✅ Optimistic locking to prevent double spending
- ✅ Kafka notifications for transaction updates
- ✅ Image built with Jib
- ✅ Docker Compose setup for easy deployment

## 📌 Features
- **RESTful API** with endpoints for processing payments
- **Optimistic Locking** to ensure concurrency control
- **Kafka Events** for real-time notifications
- **Docker Support** with PostgreSQL & Kafka containers

## 🔧 Setup & Installation

### 1️⃣ Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven or Gradle

### 2️⃣ Clone the Repository

git clone https://github.com/Aszoka/instant-payment-api.git

### ️3️⃣ Build image to a Docker daemon

run the gradle task jibDockerBuild

### 4️ Run with Docker Compose

docker-compose up --build
This starts:

Spring Boot API at http://localhost:8080

PostgreSQL at localhost:5432

Kafka at localhost:9092

### 5️⃣ Access API Documentation
Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

## 🚀 Future Enhancements
✅ Add retry mechanism for failed payments

✅ Add Partitioning to the tables: 
    For example range partitioning on created date

✅ Implement a microservices architecture: For example, all account-related functionalities could be handled by a separate service.


## 🚀 Infrastructure key components and workflow ideas

✅ Create a new repo for Kubernetes configurations

✅ Use Helm for the configurations

✅ ArgoCD for deployment 

✅ GCR (Google Container Registry) for storing images

✅ GKE (Google Kubernetes Engine) for multiple microservice instances

    Each microservice (Payment API, Kafka Producer, Kafka Consumer) runs multiple instances for high availability (HA).

    Load balancers ensure proper traffic distribution.

    Multi-zone GKE clusters for high availability, in multiple Regions if needed

✅ AlloyDB for PostgreSQL:

    High-performance database for transactional and analytical workloads.


✅ Redis for Distributed Locks:

    Prevents race conditions and ensures correct processing order in a distributed environment.

    Useful for optimistic concurrency control in payment transactions.

✅ Use Secret Manager for secrets, keys, certifications