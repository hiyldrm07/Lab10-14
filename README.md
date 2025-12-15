# Halil57493 Project

This is a Spring Boot application with SQLite, Flyway migrations, and basic User management.

## Prerequisites

- Java 17
- Git

## Setup

1. **Clone the repository** (if you haven't already):
    ```bash
    git clone <repository_url>
    cd halil57493
    ```

2. **Configure Environment Variables**:
    - Copy `.env.example` to `.env`:
        ```bash
        cp .env.example .env
        ```
    - The default database URL is `jdbc:sqlite:database.db`.

3. **Build and Run**:
    - Use the Gradle wrapper:
        ```bash
        ./gradlew bootRun
        ```

## Endpoints

- **GET /hello**: Returns "Hello, user!"
- **POST /users/register**: Create a new user. Body: `{"username": "test", "email": "test@example.com", "password": "password"}`
- **POST /users/login**: Login. Params: `username`, `password`.

## Structure

- `src/main/java/com/sowa/halil57493/model`: Entity classes
- `src/main/java/com/sowa/halil57493/repository`: JPA Repositories
- `src/main/java/com/sowa/halil57493/service`: Business logic
- `src/main/java/com/sowa/halil57493/controller`: REST Controllers
- `src/main/resources/db/migration`: Flyway migrations
