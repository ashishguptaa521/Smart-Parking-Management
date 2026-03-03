# Smart Parking Management System - API Documentation

## Overview
All APIs have been successfully implemented and integrated into the Smart Parking Management System.

## Base URL
```
/api/parking
```

## Implemented APIs

### 1. Vehicle Entry
**Endpoint:** `POST /api/parking/entry`

**Description:** Process vehicle entry and assign a parking slot

**Request Body:**
```json
{
  "licensePlate": "ABC123",
  "slotType": "REGULAR"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Vehicle entry processed successfully",
  "data": {
    "ticketId": 1,
    "licensePlate": "ABC123",
    "slotNumber": "A-101",
    "entryTime": "2026-02-22T23:00:00"
  }
}
```

---

### 2. Vehicle Exit & Fee Calculation
**Endpoint:** `POST /api/parking/exit/{ticketId}`

**Description:** Process vehicle exit and calculate parking fee

**Path Parameters:**
- `ticketId` (Long): Parking ticket ID

**Response:**
```json
{
  "success": true,
  "message": "Vehicle exit processed successfully. Parking fee calculated.",
  "data": {
    "ticketId": 1,
    "exitTime": "2026-02-22T23:30:00",
    "fee": 50.00
  }
}
```

---

### 3. Get Available Parking Slots
**Endpoint:** `GET /api/parking/available`

**Description:** Retrieve all available parking slots

**Response:**
```json
{
  "success": true,
  "message": "Available parking slots retrieved successfully",
  "data": [
    {
      "id": 1,
      "slotNumber": "A-101",
      "slotType": "REGULAR"
    },
    {
      "id": 2,
      "slotNumber": "A-102",
      "slotType": "COMPACT"
    }
  ]
}
```

---

### 4. Reserve a Parking Slot
**Endpoint:** `POST /api/parking/reserve`

**Description:** Reserve a parking slot for a specific vehicle

**Request Body:**
```json
{
  "slotId": 1,
  "licensePlate": "ABC123",
  "durationMinutes": 30
}
```

**Response:**
```json
{
  "success": true,
  "message": "Parking slot reserved successfully",
  "data": {
    "id": 1,
    "slotNumber": "A-101",
    "slotType": "REGULAR",
    "occupied": false,
    "occupiedByLicensePlate": "ABC123",
    "reservedUntil": "2026-02-22T23:30:00"
  }
}
```

---

### 5. Cancel a Reservation
**Endpoint:** `DELETE /api/parking/reserve/{id}`

**Description:** Cancel an existing reservation

**Path Parameters:**
- `id` (Long): Slot ID to cancel reservation

**Response:**
```json
{
  "success": true,
  "message": "Reservation cancelled successfully",
  "data": null
}
```

---

### 6. Search Vehicles by License Plate
**Endpoint:** `GET /api/parking/search?query={licensePlate}`

**Description:** Search for vehicles by license plate (supports partial matches)

**Query Parameters:**
- `query` (String): License plate search query

**Response:**
```json
{
  "success": true,
  "message": "Vehicles found successfully",
  "data": [
    {
      "ticketId": 1,
      "licensePlate": "ABC123",
      "slotNumber": "A-101",
      "entryTime": "2026-02-22T22:00:00",
      "exitTime": "2026-02-22T23:00:00",
      "feeCharged": 50.00
    }
  ]
}
```

---

### 7. Get Parking History for a Vehicle
**Endpoint:** `GET /api/parking/history/{licensePlate}`

**Description:** Get complete parking history for a specific vehicle (paginated)

**Path Parameters:**
- `licensePlate` (String): Vehicle license plate number

**Query Parameters (Optional):**
- `page` (int): Page number (default: 0)
- `size` (int): Page size (default: 20)
- `sort` (String): Sort field and direction (e.g., "entryTime,desc")

**Response:**
```json
{
  "success": true,
  "message": "Parking history retrieved successfully",
  "data": {
    "content": [
      {
        "ticketId": 1,
        "licensePlate": "ABC123",
        "slotNumber": "A-101",
        "entryTime": "2026-02-22T22:00:00",
        "exitTime": "2026-02-22T23:00:00",
        "feeCharged": 50.00
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20
    },
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

## Error Response Format
All APIs return errors in a consistent format:

```json
{
  "success": false,
  "message": "Error message describing what went wrong",
  "data": null
}
```

## Status Codes
- `200 OK`: Successful GET/DELETE operations
- `201 Created`: Successful POST operations
- `400 Bad Request`: Invalid input data
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server-side errors

## Implementation Details

### Files Created/Modified:

1. **Controller:**
   - `ParkingController.java` - REST controller with all 7 API endpoints

2. **Services:**
   - `ReservationServiceImpl.java` - Implementation for reservation management

3. **DTOs:**
   - `ReservationRequestDTO.java` - Updated with required fields
   - `ApiResponseDTO.java` - Updated to support generic data field

4. **Repository:**
   - `ParkingTicketRepository.java` - Added custom query methods with @Query annotations

### Features:
- ✅ Input validation using Jakarta Bean Validation
- ✅ Proper error handling via GlobalExceptionHandler
- ✅ Pagination support for history endpoint
- ✅ Search functionality with partial matching
- ✅ Caching for available slots
- ✅ Transaction management
- ✅ CORS enabled for cross-origin requests

## Testing the APIs

### Using curl:

```bash
# 1. Vehicle Entry
curl -X POST http://localhost:8080/api/parking/entry \
  -H "Content-Type: application/json" \
  -d '{"licensePlate":"ABC123","slotType":"REGULAR"}'

# 2. Vehicle Exit
curl -X POST http://localhost:8080/api/parking/exit/1

# 3. Get Available Slots
curl http://localhost:8080/api/parking/available

# 4. Reserve Slot
curl -X POST http://localhost:8080/api/parking/reserve \
  -H "Content-Type: application/json" \
  -d '{"slotId":1,"licensePlate":"ABC123","durationMinutes":30}'

# 5. Cancel Reservation
curl -X DELETE http://localhost:8080/api/parking/reserve/1

# 6. Search Vehicles
curl "http://localhost:8080/api/parking/search?query=ABC"

# 7. Get Parking History
curl "http://localhost:8080/api/parking/history/ABC123?page=0&size=10"
```

## Notes
- The system uses Spring Data JPA for database operations
- All timestamps are in ISO-8601 format
- Slot types supported: COMPACT, REGULAR, LARGE, HANDICAPPED
- Default reservation duration is 30 minutes if not specified
- Expired reservations can be cleaned up using the `releaseExpiredReservations()` service method