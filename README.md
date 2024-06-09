# NutriTrack

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

## Technologies Used


## API Documentation

### Authentication API

**Endpoint: `/api/auth/register`**
- **Method:** `POST`
- **Description:** Register a new user.
- **Request Body:**
    ```json
    {
        "username": "string",
        "email": "string",
        "password": "string",
        "firstName": "string",
        "lastName": "string"
    }
    ```

**Endpoint: `/api/auth/authenticate`**
- **Method:** `POST`
- **Description:** Authenticate a user and return a JWT token.
- **Request Body:**
    ```json
    {
        "username": "string",
        "password": "string"
    }
    ```
- **Response Body:**
    ```json
    {
        "token": "string"
    }
    ```

### User API

**Endpoint: `/api/users`**
- **Method:** `GET`
- **Description:** Get all users.
- **[Response Body:](#user-response-body)**

**Endpoint: `/api/users/{id}`**
- **Method:** `GET`
- **Description:** Get user by ID.
- **[Response Body:](#user-response-body)**

**Endpoint: `/api/users/search?keyword={keyword}`**
- **Method:** `GET`
- **Description:** Search users by keyword.
- **[Response Body:](#user-response-body)**

**Endpoint: `/api/users/me`**
- **Method:** `GET`
- **Description:** Get the current user.
- **[Response Body:](#user-response-body)**

#### **User Response Body**
```json
{
     "id": 0,
     "username": "string",
     "email": "string",
     "firstName": "string",
     "lastName": "string"
}
```

**Endpoint: `/api/users/{id}`**
- **Method:** `DELETE`
- **Description:** Delete user by ID.
    
### Food API

**Endpoint: `/api/foods`**
- **Method:** `POST`
- **Description:** Create a new food item.
- **Request Body:**
```json
    {
        "title": "string",
        "brand": "string",
        "category": "string",
        "nutrition": {
            "calories": 0.0,
            "protein": 0.0,
            "carbs": 0.0,
            "fat": 0.0
        },
        "sustainability": {
            "co2perKg": 0.0,
            "dietType": "OMNIVORE|VEGETARIAN|VEGAN"
        },
        "portions": [
            {
                "label": "String",
                "quantity": 0.0
            }
        ],
        "liquid": false
    }
```

**Endpoint: `/api/foods/user`**
- **Method:** `GET`
- **Description:** Get all food items of the current user.
- **[Response Body:](#food-response-body)**

**Endpoint: `/api/foods/{id}`**
- **Method:** `GET`
- **Description:** Get details of a food item by ID.
- **[Response Body:](#food-response-body)**

#### **Food Response Body**
```json
    {
        "id": 0,
        "title": "string",
        "brand": "string",
        "category": "string",
        "nutrition": {
            "calories": 0.0,
            "protein": 0.0,
            "carbs": 0.0,
            "fat": 0.0
        },
        "sustainability": {
            "co2perKg": 0.0,
            "dietType": "OMNIVORE|VEGETARIAN|VEGAN"
        },
        "portions": [
            {
                "id": 0,
                "label": "string",
                "quantity": 0.0
            }
        ],
        "liquid": false
    }
```

**Endpoint: `/api/foods/{id}`**
- **Method:** `PUT`
- **Description:** Update details of a specific food item by ID. Removes portions which are not present in the request body. Adds new portion if portion ID in json body is missing.  
- **Request Body:**
  
```json
    {
        "title": "string",
        "brand": "string",
        "category": "string",
        "nutrition": {
            "calories": 0.0,
            "protein": 0.0,
            "carbs": 0.0,
            "fat": 0.0
        },
        "sustainability": {
            "co2perKg": 0.0,
            "dietType": "OMNIVORE|VEGETARIAN|VEGAN"
        },
        "portions": [
            {
                "id": 0,
                "label": "String",
                "quantity": 0.0
            },
            {
                "label": "String",
                "quantity": 0.0
            }
        ],
        "liquid": false
    }
```

**Endpoint: `/api/foods/user/simple`**
- **Method:** `GET`
- **Description:** Get all simplified food items of the current user.
- **[Response Body:](#simple-food-response-body)**

**Endpoint: `/api/foods/user/simple/search?title={title}`**
- **Method:** `GET`
- **Description:** Search for simplified food items of the current user.
- **[Response Body:](#simple-food-response-body)**

#### **Simple Food Response Body**
```json
    {
        "id": 0,
        "title": "string",
        "brand": "string",
        "category": "string",
    }
```

**Endpoint: `/api/foods/{id}`**
- **Method:** `DELETE`
- **Description:** Deactivate a food item by ID.

**Endpoint: `/api/foods/{id}/hard-delete`**
- **Method:** `DELETE`
- **Description:** Delete a food item by ID.

### Tracking API

**Endpoint: `/api/tracking`**
- **Method:** `POST`
- **Description:** Track a food item for the current user. If `portionId = null`, the tracking quantity will be directly used as the weight, meaning if `portionId = null` and `quantity = 260`, the weight of the tracked item would be 260 g or ml, depending on the type.
- **Request Body:**
```json
    {
        "foodId": 0,
        "portionId": 0,
        "quantity": 0.0
    }
```
- **Response Body:**
```json
    {
        "id": 1,
        "food": {
            "id": 0,
            "title": "string",
            "brand": "string",
            "category": "string",
            "isLiquid": false
        },
        "portion": {
            "id": 1,
            "label": "string",
            "quantity": 0.0
        },
        "quantity": 0.0,
        "timestamp": "current-date-string"
    }
```

**Endpoint: `/api/tracking/{id}`**
- **Method:** `PUT`
- **Description:** Update a tracking entry.
- **Request Body:**
```json
    {
        "portionId": 0,
        "quantity": 0.0
    }
```

**Endpoint: `/api/tracking/user`**
- **Method:** `GET`
- **Description:** Get all tracking entries.
- **Response Body:**
```json
    {
        "id": 1,
        "food": {
            "id": 1,
            "title": "string",
            "brand": "string",
            "category": "string",
            "isLiquid": false
        },
        "portion": {
            "id": 1,
            "label": "string",
            "quantity": 0.0
        },
        "quantity": 0.0,
        "timestamp": "date-string"
    }
```

**Endpoint: `/api/tracking/user/date?date={YYYY-MM-DD}`**
- **Method:** `GET`
- **Description:** Get tracking entries and stat summary on a specific day.
- **Response Body:**
```json
    {
       "trackings": [
           {
              "id": 1,
              "food": {
                   "id": 1,
                   "title": "string",
                   "brand": "string",
                   "category": "string",
                   "isLiquid": false
               },
               "portion": {
                    "id": 1,
                    "label": "string",
                    "quantity": 0.0
                },
                "quantity": 0.0,
                "timestamp": [YYYY, MM, DD, HH, MM, SS, NS]
            }
        ],
        "summary": {
            "totalCalories": 0.0,
            "totalProtein": 0.0,
            "totalCarbs": 0.0,
            "totalFat": 0.0,
            "totalCo2": 0.0,
            "totalVeganMeals": 0,
            "totalVegetarianMeals": 0,
            "dailyPoints": 0
        }
    }
```

**Endpoint: `/api/tracking/{id}`**
- **Method:** `DELETE`
- **Description:** Delete tracking entry by ID.

### User Stats API

**Endpoint: `/api/user/total`**
- **Method:** `GET`
- **Description:** Get total stat summary of the current user.
- **[Response Body:](#stats-response-body)**

**Endpoint: `/api/user/date?date={YYYY-MM-DD}`**
- **Method:** `GET`
- **Description:** Get stat summary of the current user on a specific day.
- **[Response Body:](#stats-response-body)**

#### **Stats Response Body**
```json
    {
        "totalCalories": 0.0,
        "totalProtein": 0.0,
        "totalCarbs": 0.0,
        "totalFat": 0.0,
        "totalCo2": 0.0,
        "totalVeganMeals": 0,
        "totalVegetarianMeals": 0,
        "totalPoints": 0
    }
```
