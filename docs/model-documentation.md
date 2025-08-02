# Model Documentation

## Overview

This document provides detailed information about the data models used in the SDUI (Server-Driven UI) project. These models define the structure of UI components and their properties.

## Model Classes

### Plugin

The `Plugin` class is the main data model representing a UI component with styling and content.

**Package:** `br.com.corps.model`

**Properties:**

| Property | Type | Description |
|----------|------|-------------|
| type | String | The type of UI component (e.g., "card-benefits-plugin", "points-summary-plugin") |
| feature | String | The feature identifier used for filtering (e.g., "mastercard-benefits", "loyalty-points") |
| modifier | Modifier | Layout properties like padding, margin, and alignment |
| style | Style | Visual styling properties like colors, fonts, and borders |
| benefits | List\<BenefitGroup\> | List of benefit groups for different profiles (used in card-benefits-plugin) |
| points | Points | Points information (used in points-summary-plugin) |

### BenefitGroup

The `BenefitGroup` class represents a group of benefits for a specific profile.

**Package:** `br.com.corps.model`

**Properties:**

| Property | Type | Description |
|----------|------|-------------|
| profile | String | The profile name (e.g., "Black", "Gold", "Platinum") |
| benefits | List\<String\> | List of benefit descriptions for the profile |

### Points

The `Points` class contains loyalty points information.

**Package:** `br.com.corps.model`

**Properties:**

| Property | Type | Description |
|----------|------|-------------|
| total | Integer | The total number of points |
| lastUpdated | String | The timestamp when points were last updated (ISO 8601 format) |
| currency | String | The points currency name (e.g., "pontos") |

### Modifier

The `Modifier` class defines layout properties for UI components.

**Package:** `br.com.corps.model`

**Properties:**

| Property | Type | Description |
|----------|------|-------------|
| padding | String | The internal spacing of the component (e.g., "16dp") |
| margin | String | The external spacing around the component (e.g., "12dp") |
| alignment | String | The alignment of the component (e.g., "center") |

### Style

The `Style` class defines visual styling properties for UI components.

**Package:** `br.com.corps.model`

**Properties:**

| Property | Type | Description |
|----------|------|-------------|
| backgroundColor | String | The background color in hex format (e.g., "#FFFFFF") |
| borderRadius | String | The corner radius of the component (e.g., "8dp") |
| textColor | String | The text color in hex format (e.g., "#333333") |
| shadow | Boolean | Whether the component has a shadow effect |
| fontFamily | String | The font family name (e.g., "Roboto") |
| fontWeight | String | The font weight (e.g., "bold") |

### PluginContainer

The `PluginContainer` class is used internally to deserialize the JSON configuration file.

**Package:** `br.com.corps.model`

**Properties:**

| Property | Type | Description |
|----------|------|-------------|
| type | String | The container type (e.g., "container") |
| feature | String | The container feature identifier (e.g., "mastercard-dashboard") |
| children | List\<Plugin\> | List of child plugins |

## Annotations

The model classes use the following annotations:

- `@Introspected`: Micronaut annotation for bean introspection
- `@Data`: Lombok annotation that generates getters, setters, equals, hashCode, and toString methods
