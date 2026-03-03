# JWT Authentication Implementation Guide

## Overview
This document describes the JWT (JSON Web Token) authentication implementation for the Smart Parking Management System.

## Architecture

### Components

1. **JwtUtil** - Utility class for JWT token operations
2. **JwtAuthenticationFilter** - Filter to validate JWT tokens on each request
3. **JwtAuthenticationEntryPoint** - Handles authentication errors
4. **CustomUserDetailsService** - Loads user details from database
5. **SecurityConfig** - Spring Security configuration with JWT integration
6. **AuthController** - Handles login and registration

## API Endpoints

### 1. User Registration
**Endpoint:** `POST /api/auth/register`

**Description:** Register a new user

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "SecurePassword123",
  "role": "USER"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": null
}
```

**Available Roles:**
- `USER` - Regular user
- `ADMIN` - Administrator
- `OPERATOR` - Parking operator

---

### 2. User Login
**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and receive JWT token

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "SecurePassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "john_doe",
    "role": "USER"
  }
}
```

---

## Using JWT Tokens

### Making Authenticated Requests

Once you receive a JWT token from the login endpoint, include it in the `Authorization` header of all subsequent requests:

```
Authorization: Bearer <your-jwt-token>
```

### Example with curl:

```bash
# 1. Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePassword123",
    "role": "USER"
  }'

# 2. Login and get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePassword123"
  }'

# 3. Use token to access protected endpoints
curl -X GET http://localhost:8080/api/parking/available \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 4. Vehicle Entry (Protected)
curl -X POST http://localhost:8080/api/parking/entry \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "licensePlate": "ABC123",
    "slotType": "REGULAR"
  }'
```

### Example with Postman:

1. **Register/Login:**
   - Method: POST
   - URL: `http://localhost:8080/api/auth/login`
   - Body (raw JSON):
     ```json
     {
       "username": "john_doe",
       "password": "SecurePassword123"
     }
     ```

2. **Copy the token from response**

3. **Access Protected Endpoints:**
   - Method: GET/POST (as needed)
   - URL: `http://localhost:8080/api/parking/...`
   - Headers:
     - Key: `Authorization`
     - Value: `Bearer <paste-your-token-here>`

---

## Protected Endpoints

All endpoints under `/api/parking/**` require JWT authentication:

- `POST /api/parking/entry` - Vehicle entry
- `POST /api/parking/exit/{ticketId}` - Vehicle exit
- `GET /api/parking/available` - Get available slots
- `POST /api/parking/reserve` - Reserve a slot
- `DELETE /api/parking/reserve/{id}` - Cancel reservation
- `GET /api/parking/search?query={licensePlate}` - Search vehicles
- `GET /api/parking/history/{licensePlate}` - Get parking history

---

## JWT Configuration

The JWT configuration is stored in `application.properties`:

```properties
# JWT Configuration
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000           # 24 hours in milliseconds
jwt.refresh-expiration=604800000  # 7 days in milliseconds
```

### Token Expiration

- **Access Token:** 24 hours (86400000 ms)
- **Refresh Token:** 7 days (604800000 ms) - for future implementation

---

## Error Handling

### Authentication Errors

When authentication fails, the API returns a 401 Unauthorized response:

```json
{
  "success": false,
  "message": "Unauthorized: Full authentication is required to access this resource",
  "data": null
}
```

### Invalid Token

If the JWT token is invalid or expired:

```json
{
  "success": false,
  "message": "Unauthorized: JWT token is expired or invalid",
  "data": null
}
```

### Missing Token

If no token is provided for protected endpoints:

```json
{
  "success": false,
  "message": "Unauthorized: Full authentication is required to access this resource",
  "data": null
}
```

---

## Security Features

1. **Password Encryption:** Passwords are encrypted using BCrypt
2. **Stateless Sessions:** No server-side session storage
3. **Token-Based Authentication:** All requests validated via JWT
4. **CORS Enabled:** Cross-origin requests allowed
5. **CSRF Disabled:** Not needed for JWT authentication
6. **H2 Console Access:** Available at `/h2-console` (for development only)

---

## Testing JWT Implementation

### Step-by-Step Testing:

1. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Register a new user:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"username":"testuser","password":"Test123!","role":"USER"}'
   ```

3. **Login to get token:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"testuser","password":"Test123!"}'
   ```

4. **Copy the token from response**

5. **Test protected endpoint:**
   ```bash
   curl -X GET http://localhost:8080/api/parking/available \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

6. **Test without token (should fail):**
   ```bash
   curl -X GET http://localhost:8080/api/parking/available
   ```

---

## Implementation Files

### Core JWT Files:
- `src/main/java/com/smartparking/security/JwtUtil.java` - JWT utility functions
- `src/main/java/com/smartparking/security/JwtAuthenticationFilter.java` - JWT filter
- `src/main/java/com/smartparking/security/JwtAuthenticationEntryPoint.java` - Error handler
- `src/main/java/com/smartparking/security/CustomUserDetailsService.java` - User loader

### Configuration:
- `src/main/java/com/smartparking/config/SecurityConfig.java` - Security configuration

### Controllers:
- `src/main/java/com/smartparking/controller/AuthController.java` - Auth endpoints

### DTOs:
- `src/main/java/com/smartparking/dto/request/LoginRequestDTO.java`
- `src/main/java/com/smartparking/dto/request/RegisterRequestDTO.java`
- `src/main/java/com/smartparking/dto/response/AuthResponseDTO.java`

---

## Dependencies Added

```xml
<!-- JWT Dependencies -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

---

## Best Practices

1. **Keep the secret key secure** - Never commit real secrets to version control
2. **Use environment variables** - For production, use environment variables for JWT secret
3. **Token expiration** - Keep token expiration reasonable (24 hours recommended)
4. **HTTPS only** - Always use HTTPS in production
5. **Refresh tokens** - Implement refresh token mechanism for better UX
6. **Rate limiting** - Add rate limiting to prevent brute force attacks

---

## Future Enhancements

1. **Refresh Token Implementation** - Add refresh token endpoint
2. **Role-Based Access Control** - Implement @PreAuthorize for role-based endpoints
3. **Token Blacklist** - Add logout functionality with token blacklisting
4. **Multi-Factor Authentication** - Add MFA support
5. **OAuth2 Integration** - Add Google/Facebook login

---

## Troubleshooting

### Common Issues:

**Issue:** "Unauthorized" error even with valid token
- Check if token format is correct: `Bearer <token>`
- Verify token hasn't expired
- Ensure secret key matches between generation and validation

**Issue:** "Bad credentials" on login
- Verify username and password are correct
- Check if user exists in database
- Ensure password is properly encoded

**Issue:** Spring Boot startup errors
- Run `mvn clean install` to resolve dependency issues
- Check all required dependencies are in pom.xml
- Verify Spring Boot version compatibility

---

## Summary

JWT authentication is now fully implemented across all parking management APIs. Users must:
1. Register an account
2. Login to receive a JWT token
3. Include the token in Authorization header for all protected endpoints

All parking operations now require valid authentication, ensuring secure access to the system.
