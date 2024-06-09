# NutriTrack Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Environment Setup](#environment-setup)
3. [Technologies Used](#technologies-used)
4. [API Documentation](#api-documentation)
    - [Authentication API](#authentication-api)
    - [User API](#user-api)
    - [Food API](#food-api)
    - [Tracking API](#tracking-api)
    - [User Stats API](#user-stats-api)
5. [Entities and DTOs](#entities-and-dtos)

---

## Project Overview
**NutriTrack** is a web application designed to help users track their food consumption and nutritional information. Users can register, log in, add food items, track their daily food intake, and view their nutritional statistics.

## Environment Setup
To set up the development environment for NutriTrack, follow these steps:

1. **Clone the Repository:**
    ```sh
    git clone <repository_url>
    cd nutritrack
    ```

2. **Build and Run with Docker:**
    Ensure you have Docker installed. Use the `docker-compose.yml` file to set up the application:
    ```sh
    docker-compose up --build
    ```

3. **Build and Run without Docker:**
    Ensure you have Java and Maven installed. Run the following commands:
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

## Technologies Used
- **Spring Boot** for application framework
- **Spring Security** for authentication and authorization
- **JWT** for secure token-based authentication
- **Hibernate** for ORM
- **PostgreSQL** as the database
- **Docker** for containerization
- **Swagger** for API documentation
- **Lombok** to reduce boilerplate code

## API Documentation

### Authentication API

**Endpoint: `/api/auth/register`**
- **Method:** `POST`
- **Description:** Register a new user.
- **Request Body:**
    ```json
    {
        "username": "your-username",
        "password": "your-password"
    }
    ```
- **Response Body:**
    ```json
    {
        "id": 1,
        "username": "your-username"
    }
    ```

**Endpoint: `/api/auth/authenticate`**
- **Method:** `POST`
- **Description:** Authenticate a user and return a JWT token.
- **Request Body:**
    ```json
    {
        "username": "your-username",
        "password": "your-password"
    }
    ```
- **Response Body:**
    ```json
    {
        "token": "your-jwt-token"
    }
    ```

### User API

**Endpoint: `/api/user`**
- **Method:** `GET`
- **Description:** Retrieve user details.
- **Response Body:**
    ```json
    {
        "id": 1,
        "username": "your-username",
        "email": "your-email"
    }
    ```

### Food API

**Endpoint: `/api/food`**
- **Method:** `POST`
- **Description:** Create a new food item.
- **Request Body:**
    ```json
    {
        "title": "Apple",
        "brand": "Fresh Fruits",
        "category": "Fruit",
        "nutrition": {
            "calories": 52,
            "protein": 0.3,
            "carbs": 14,
            "fat": 0.2
        },
        "sustainability": {
            "co2perKg": 0.1,
            "dietType": "VEGAN"
        },
        "portions": [
            {
                "label": "Whole",
                "quantity": 182
            }
        ],
        "liquid": false
    }
    ```
- **Response Body:**
    ```json
    {
        "id": 1,
        "title": "Apple",
        "brand": "Fresh Fruits",
        "category": "Fruit",
        "nutrition": {
            "calories": 52,
            "protein": 0.3,
            "carbs": 14,
            "fat": 0.2
        },
        "sustainability": {
            "co2perKg": 0.1,
            "dietType": "VEGAN"
        },
        "portions": [
            {
                "id": 1,
                "label": "Whole",
                "quantity": 182
            }
        ],
        "liquid": false
    }
    ```

**Endpoint: `/api/food/{id}`**
- **Method:** `GET`
- **Description:** Get details of a specific food item by ID.
- **Response Body:**
    ```json
    {
        "id": 1,
        "title": "Apple",
        "brand": "Fresh Fruits",
        "category": "Fruit",
        "nutrition": {
            "calories": 52,
            "protein": 0.3,
            "carbs": 14,
            "fat": 0.2
        },
        "sustainability": {
            "co2perKg": 0.1,
            "dietType": "VEGAN"
        },
        "portions": [
            {
                "id": 1,
                "label": "Whole",
                "quantity": 182
            }
        ],
        "liquid": false
    }
    ```

**Endpoint: `/api/food/{id}`**
- **Method:** `PUT`
- **Description:** Update details of a specific food item by ID.
- **Request Body:**
    ```json
    {
        "title": "Apple",
        "brand": "Fresh Fruits",
        "category": "Fruit",
        "nutrition": {
            "calories": 52,
            "protein": 0.3,
            "carbs": 14,
            "fat": 0.2
        },
        "sustainability": {
            "co2perKg": 0.1,
            "dietType": "VEGAN"
        },
        "portions": [
            {
                "label": "Whole",
                "quantity": 182
            }
        ],
        "liquid": false
    }
    ```
- **Response Body:**
    ```json
    {
        "id": 1,
        "title": "Apple",
        "brand": "Fresh Fruits",
        "category": "Fruit",
        "nutrition": {
            "calories": 52,
            "protein": 0.3,
            "carbs": 14,
            "fat": 0.2
        },
        "sustainability": {
            "co2perKg": 0.1,
            "dietType": "VEGAN"
        },
        "portions": [
            {
                "id": 1,
                "label": "Whole",
                "quantity": 182
            }
        ],
        "liquid": false
    }
    ```

**Endpoint: `/api/food/{id}`**
- **Method:** `DELETE`
- **Description:** Delete a specific food item by ID.

### Tracking API

**Endpoint: `/api/tracking`**
- **Method:** `POST`
- **Description:** Track a food item.
- **Request Body:**
    ```json
    {
        "foodId": 1,
        "quantity": 1,
        "portionId": 1
    }
    ```
- **Response Body:**
    ```json
    {
        "id": 1,
        "food": {
            "id": 1,
            "title": "Apple",
            "brand": "Fresh Fruits",
            "category": "Fruit",
            "isLiquid": false
        },
        "portion": {
            "id": 1,
            "label": "Whole",
            "quantity": 182
        },
        "quantity": 1,
        "timestamp": "2024-06-09T08:10:35.186885"
    }
    ```

**Endpoint: `/api/tracking/{id}`**
- **Method:** `GET`
- **Description:** Get details of a specific tracking entry by ID.
- **Response Body:**
    ```json
    {
        "id": 1,
        "food": {
            "id": 1,
            "title": "Apple",
            "brand": "Fresh Fruits",
            "category": "Fruit",
            "isLiquid": false
        },
        "portion": {
            "id": 1,
            "label": "Whole",
            "quantity": 182
        },
        "quantity": 1,
        "timestamp": "2024-06-09T08:10:35.186885"
    }
    ```
