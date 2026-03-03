Project Overview: Developed a full-stack Smart Parking Management System using Spring Boot to automate parking operations, slot allocation, and fee management for parking facilities.

Key Functionalities:

• Automated Parking Operations: Implemented RESTful APIs for vehicle entry/exit processing with real-time slot assignment and availability tracking

• Dynamic Fee Calculation: Built a pricing engine that calculates parking fees based on duration and slot type (Regular, Compact, Large, Handicapped)

• Reservation Management: Created a slot reservation system allowing users to reserve parking spots in advance with automatic expiration handling

• Vehicle Tracking: Developed comprehensive vehicle search and parking history features with pagination support for efficient data retrieval

• Slot Management: Implemented intelligent slot allocation system with support for multiple slot types and real-time availability updates

Technical Stack:

Backend: Spring Boot 4.0.2, Java 17, Spring Security, Spring Data JPA
Database: H2 Database with JPA/Hibernate ORM
Architecture: RESTful API, MVC pattern, Layered architecture (Controller-Service-Repository)
Features: Exception handling, input validation, caching, transaction management
Testing: JUnit 5, Mockito, REST Assured for comprehensive unit and integration testing
Tools: Maven, Lombok, MapStruct for DTO mapping
Technical Highlights:

Designed and implemented 7 production-ready REST APIs with comprehensive error handling
Applied security best practices using Spring Security for authentication and authorization
Implemented caching strategies to optimize performance for frequently accessed data
Created custom JPA queries for complex data retrieval and search operations
Developed complete API documentation and testing documentation
Impact: Streamlined parking facility operations by automating manual processes, reducing wait times, and providing real-time parking availability information.
