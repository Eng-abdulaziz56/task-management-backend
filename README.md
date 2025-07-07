# Task Management System

A Spring Boot application for managing tasks with user authentication, email notifications, and RabbitMQ messaging.

## 🚀 Quick Start


### Run with Docker (Recommended)
```bash
# Clone the repository
git clone <your-repo-url>
cd management

# Start all services
docker-compose up -d --build


## 📋 Services

- **App**: Spring Boot application (port 8080)
- **PostgreSQL**: Database (port 5432)
- **RabbitMQ**: Message broker (port 5672, management UI: 15672)

## 🚀 CI/CD Pipeline

✅ **CI/CD Pipeline Ready** - Automated testing and building on pull requests to main branch.

**What's included:**
- Automated tests with H2 database
- Docker build and testing
- Pull request validation

**Deployment:** Server deployment commented out (no server available)

