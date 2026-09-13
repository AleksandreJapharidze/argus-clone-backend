# Argus Clone Backend

A RESTful course-management backend inspired by **Ilia State University's Argus system**, built with Java and Spring Boot.

The project manages courses, groups, instructors, students, syllabi, lectures, scores, and course materials. It was developed as a personal project to explore the design of a larger Spring application beyond basic CRUD functionality, with particular attention to scheduling, authentication and authorization, data access, caching, and consistency between external storage and the database.

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring MVC**
- **Spring Data JPA**
- **Spring Security**
- **MySQL**
- **Spring Cache + Caffeine**
- **MapStruct**
- **JDBC / JdbcTemplate**
- **JWT authentication**
- **AWS SDK for Java (S3)**
- **Maven**

## Features

### Course Management

The application provides REST APIs for managing the main parts of a university course-management system, including:

- Courses and syllabi
- Course groups
- Students and instructors
- Lectures
- Course scores
- Course materials

The application follows a layered structure separating controllers, services, repositories, DTOs, entities, and mapping logic.

### Authentication and Authorization

Authentication is implemented using **Spring Security and JWTs**.

The security configuration includes:

- JWT-based authentication
- Role-based authorization
- Method-level authorization with `@PreAuthorize`
- BCrypt password hashing
- Protected API endpoints based on authenticated users and their roles

### Semester Lecture Scheduling

Lecture scheduling supports generating a semester's schedule from an initial week's lecture configuration.

The scheduling logic:

- Generates lectures across a **15-week semester**
- Detects scheduling conflicts and overlapping lectures
- Prevents duplicate lecture assignments
- Accounts for holidays when generating lecture dates
- Uses **JDBC batch insertion** to persist generated lectures efficiently

### Persistence and Query Optimization

The persistence layer uses **Spring Data JPA** with MySQL.

Some relationships require related entities to be loaded without producing unnecessary individual queries. The project uses `@EntityGraph` and custom JPQL queries where appropriate to control fetching and avoid N+1 query problems.

For operations where bulk persistence is useful, such as semester lecture generation, `JdbcTemplate` batching is used instead of issuing individual insert operations.

### Caching

The application uses **Spring Cache with Caffeine** for frequently accessed data.

Because cached values can depend on multiple related entities, cache invalidation is handled deliberately rather than simply clearing every cache after each modification. Related cache entries are evicted when operations could otherwise leave stale data.

### Course Material Storage

Course materials are stored using **S3-compatible object storage**, while their metadata is persisted in the relational database.

Since the database transaction and external object storage cannot participate in the same transaction, the application includes cleanup logic for partial failures. For example, if a file is successfully uploaded but the database transaction later rolls back, the application attempts to remove the uploaded object to avoid leaving an orphaned file.

Failed cleanup attempts can be recorded so that orphaned files are not silently forgotten.

## Project Structure

```text
src/main/java/com/example/argusclone/
├── config/          # Security, caching, and storage configuration
├── controllers/     # REST API controllers
├── dtos/            # Request and response DTOs
├── entities/        # JPA entities
├── exceptions/      # Application-specific exceptions
├── mappers/         # DTO/entity mapping
├── repositories/    # Data-access layer
└── services/        # Business logic
```

## Running the Project

### Requirements

- Java 25
- Maven
- MySQL
- S3-compatible object storage

## Purpose

This project was created primarily as a **learning and personal development project**.

Its goal is not to reproduce Ilia State University's Argus system exactly, but to use a university course-management system as the basis for designing and implementing a larger Java backend while exploring practical problems involving persistence, security, scheduling, caching, performance, and external file storage.