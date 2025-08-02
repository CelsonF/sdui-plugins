# SDUI Architecture Overview

## Introduction

This document provides a high-level overview of the Server-Driven UI (SDUI) implementation in this project. The SDUI pattern allows the server to define UI components, layouts, and content that client applications can render dynamically.

## System Architecture

The SDUI system is built using the Micronaut framework and follows a clean architecture approach with the following components:

### Core Components

1. **Controller Layer**
   - `SDUIController`: Exposes REST API endpoints for clients to request UI components by feature
   - Handles HTTP requests and responses
   - Maps requests to appropriate service methods

2. **Service Layer**
   - `SDUIPluginService`: Core business logic for retrieving and filtering plugins
   - Loads plugin data from a JSON configuration file
   - Filters plugins based on requested features

3. **Model Layer**
   - `Plugin`: Main data model representing a UI component with styling and content
   - `BenefitGroup`: Represents a group of benefits for a specific profile
   - `Points`: Contains loyalty points information
   - `Modifier`: Defines layout properties like padding, margin, and alignment
   - `Style`: Defines visual styling properties like colors, fonts, and borders

4. **Configuration**
   - `Configuration`: Provides configurable properties for the application
   - Currently supports configuring the JSON path for plugin definitions

## Data Flow

1. Client applications request UI components by feature via the `/sdui/plugins` endpoint
2. The controller receives the request and delegates to the service layer
3. The service filters the pre-loaded plugins based on the requested features
4. The filtered plugins are returned to the client as a JSON response
5. Client applications render the UI components based on the received data

## Server-Driven UI Pattern

The SDUI pattern implemented in this project offers several advantages:

1. **Flexibility**: UI components can be modified on the server without requiring client updates
2. **Consistency**: Ensures consistent UI across different client platforms
3. **Dynamic Updates**: New features can be rolled out by updating server configurations
4. **Reduced Client Complexity**: Moves UI logic to the server, simplifying client implementations

## Technology Stack

- **Framework**: Micronaut
- **Language**: Java
- **Documentation**: OpenAPI/Swagger
- **Build Tool**: Maven
- **Libraries**:
  - Lombok: Reduces boilerplate code
  - Jackson: JSON serialization/deserialization
  - Swagger: API documentation

## Deployment

The application is packaged as a standalone JAR file and can be deployed to any environment that supports Java.
