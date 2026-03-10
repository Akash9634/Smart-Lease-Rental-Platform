# SmartLease Backend

## Overview Built for the Modern Rental Market
SmartLease is a comprehensive, next-generation property rental platform designed to streamline the leasing experience for both landlords and tenants. The SmartLease Backend is a robust Spring Boot application that serves as the core API framework, coordinating all data flow, security, and business logic for the platform.

### What it Does 
The SmartLease platform simplifies the property rental lifecycle by providing:
- **Centralized Property Discovery**: Tenants can browse, filter, and view detailed property listings.
- **Simplified Leasing Workflow**: Landlords can easily list properties, manage availability, and handle tenant applications.
- **Integrated Payments**: Secure processing of rent, deposits, or application fees directly through the platform.
- **Smart Enhancements**: Automated features, such as AI-generated property descriptions and seamless media management for high-quality property photos.

### Why it's Helpful
- **For Tenants**: Offers a unified, intuitive interface to find homes, communicate with landlords in real-time, and handle lease payments securely without switching between different apps.
- **For Landlords/Property Managers**: Reduces the administrative burden of managing listings, collecting payments, and communicating with prospective or current tenants.
- **For Developers**: Provides a modern, secure, and scalable REST API using enterprise-grade technologies (Spring Boot, PostgreSQL, JWT, WebSockets), allowing seamless integration with diverse frontends (like React/Vite) or mobile applications.

## Core Features Flow

## Features
- **Secure Authentication & Authorization**: Implemented using Spring Security and JSON Web Tokens (JWT).
- **Property Management**: Complete CRUD operations and business logic mapping to relational entities.
- **Media Management**: Integrated with Cloudinary for seamless image and file uploads.
- **Payment Processing**: Payment gateway integration utilizing Razorpay.
- **AI-Powered Capabilities**: Leveraging Google's Gemini AI to generate intelligent content or descriptions.
- **Real-Time Communication**: WebSocket support for instant notifications and interactive features.
- **Interactive API Documentation**: Automated Swagger UI generation with Springdoc OpenAPI.

## Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.5.10
- **Database**: PostgreSQL
- **Security**: Spring Security & JWT
- **ORM**: Spring Data JPA & Hibernate
- **Build Tool**: Maven
- **Utilities**: Lombok

## Prerequisites
- **Java 21** or higher
- **PostgreSQL** Server running locally or remotely
- **Maven** (optional if using the provided `mvnw` wrapper)

## Environment Configuration
The application relies on several environment variables for sensitive configurations. You can set these in your environment or substitute the placeholders in `src/main/resources/application.properties`.

Default database configuration expects a local DB named `smartlease_db` with `postgres` as username.

Required environment variables for third-party services:
```properties
# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret

# Razorpay Configuration
RZP_TEST_KEY=your_razorpay_key_id
RZP_SECRET=your_razorpay_secret

# Google Gemini AI Configuration
GEMINI_KEY=your_gemini_api_key
```

## Running the Application

1. **Clone the repository / navigate to the directory**:
   ```bash
   cd smartlease-backend
   ```

2. **Database Setup**:
   Ensure your PostgreSQL service is running and create a database named `smartlease_db`:
   ```sql
   CREATE DATABASE smartlease_db;
   ```

3. **Build the Project**:
   Using the Maven wrapper:
   ```bash
   ./mvnw clean install
   ```
   *(On Windows, use `mvnw.cmd clean install`)*

4. **Run the Application**:
   Using the Maven wrapper to run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Documentation
Once the server is running (defaults to port `8080`), you can access the interactive OpenAPI/Swagger UI to explore and test the endpoints:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **API Docs (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
