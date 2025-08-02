# Service Layer Documentation

## Overview

This document provides detailed information about the service layer in the SDUI (Server-Driven UI) project. The service layer contains the business logic for loading and filtering UI components.

## Service Classes

### SDUIPluginService

The `SDUIPluginService` class is responsible for loading plugin data from a JSON configuration file and filtering plugins based on requested features.

**Package:** `br.com.corps.service`

**Dependencies:**
- `ObjectMapper`: Used for JSON deserialization
- `ResourceResolver`: Used to load resources from the classpath
- `Configuration`: Provides configuration properties

**Methods:**

#### init()

```java
@PostConstruct
@SneakyThrows
void init()
```

This method is automatically called after the service is instantiated. It loads the plugin data from the JSON configuration file specified in the application configuration.

**Implementation Details:**
1. Gets the JSON file path from the configuration
2. Uses the resource loader to load the file from the classpath
3. Deserializes the JSON into a `PluginContainer` object
4. Extracts the list of plugins from the container

**Annotations:**
- `@PostConstruct`: Indicates that this method should be executed after dependency injection is complete
- `@SneakyThrows`: Lombok annotation that handles exceptions without requiring explicit try-catch blocks

#### getPluginsByFeature()

```java
public List<Plugin> getPluginsByFeature(List<String> feature)
```

This method filters the loaded plugins based on the requested features.

**Parameters:**
- `feature`: A list of feature identifiers to filter by

**Returns:**
- A list of `Plugin` objects that match the requested features

**Implementation Details:**
1. Filters the list of plugins to include only those with a feature that matches one of the requested features
2. Returns the filtered list

## Business Logic Flow

1. **Initialization**:
   - The service is instantiated by the Micronaut dependency injection framework
   - The `init()` method is automatically called to load the plugin data
   - The JSON configuration file is loaded and deserialized
   - The list of plugins is stored in memory

2. **Plugin Retrieval**:
   - The controller receives a request for plugins with specific features
   - The controller calls the `getPluginsByFeature()` method with the requested features
   - The service filters the in-memory list of plugins based on the requested features
   - The filtered list is returned to the controller
   - The controller returns the filtered list as a JSON response

## Configuration

The service uses the `Configuration` class to get the path to the JSON configuration file. This allows the file path to be configured in the application properties.

**Example Configuration:**

```yaml
sdui:
  jsonPath: "sdui-plugins.json"
```

## Error Handling

The service uses Lombok's `@SneakyThrows` annotation to handle exceptions during JSON deserialization. If the JSON file cannot be loaded or parsed, an exception will be thrown and propagated to the caller.
