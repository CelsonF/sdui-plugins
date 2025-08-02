# SDUI Project Documentation

## Introduction

This documentation provides comprehensive information about the Server-Driven UI (SDUI) project. The SDUI pattern allows the server to define UI components, layouts, and content that client applications can render dynamically.

## Table of Contents

1. [Architecture Overview](architecture-overview.md)
2. [API Documentation](api-documentation.md)
3. [Model Documentation](model-documentation.md)
4. [Class Diagrams](class-diagram.md)
5. [Service Documentation](service-documentation.md)
6. [Setup and Installation](setup-instructions.md)
7. [Usage Examples and Tutorials](usage-examples.md)

## Project Overview

The SDUI project is a Java-based implementation of the Server-Driven UI pattern using the Micronaut framework. It provides a flexible and extensible way to define UI components on the server and deliver them to client applications.

### Key Features

- **Dynamic UI Components**: Define UI components on the server that can be rendered by client applications
- **Feature-Based Filtering**: Request specific UI components based on feature identifiers
- **Flexible Styling**: Define visual styling and layout properties for each component
- **JSON Configuration**: Use a simple JSON format to define UI components
- **RESTful API**: Access UI components through a simple RESTful API

### Technology Stack

- **Framework**: Micronaut
- **Language**: Java
- **Documentation**: OpenAPI/Swagger
- **Build Tool**: Maven
- **Libraries**:
  - Lombok: Reduces boilerplate code
  - Jackson: JSON serialization/deserialization
  - Swagger: API documentation

## Getting Started

For setup and installation instructions, see the [Setup and Installation](setup-instructions.md) guide.

For usage examples and tutorials, see the [Usage Examples and Tutorials](usage-examples.md) document.

## Architecture

The SDUI system follows a clean architecture approach with the following components:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic for loading and filtering plugins
- **Model Layer**: Defines the data structure for UI components

For more details, see the [Architecture Overview](architecture-overview.md) document.

## API Reference

The SDUI API provides endpoints for retrieving UI components based on feature identifiers.

For detailed API documentation, see the [API Documentation](api-documentation.md) document.

## Data Models

The SDUI system uses several data models to represent UI components and their properties.

For detailed model documentation, see the [Model Documentation](model-documentation.md) document.

## Class Diagrams

For visual representations of the class relationships, see the [Class Diagrams](class-diagram.md) document.

## Service Layer

The service layer contains the business logic for loading and filtering UI components.

For detailed service documentation, see the [Service Documentation](service-documentation.md) document.
