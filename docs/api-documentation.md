# API Documentation

## Overview

This document provides detailed information about the API endpoints available in the SDUI (Server-Driven UI) project. The API allows client applications to request UI components based on specific features.

## Base URL

```
/sdui
```

## Endpoints

### Get Plugins by Feature

Retrieves a list of UI plugins filtered by the specified features.

**Endpoint:** `GET /sdui/plugins`

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| feature | List\<String\> | Yes | List of feature identifiers to filter plugins |

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| 200 | Plugins found |
| 400 | No plugins found |

**Response Body:**

A JSON array of Plugin objects with the following structure:

```json
[
  {
    "type": "string",
    "feature": "string",
    "modifier": {
      "padding": "string",
      "margin": "string",
      "alignment": "string"
    },
    "style": {
      "backgroundColor": "string",
      "borderRadius": "string",
      "textColor": "string",
      "shadow": boolean,
      "fontFamily": "string",
      "fontWeight": "string"
    },
    "benefits": [
      {
        "profile": "string",
        "benefits": [
          "string"
        ]
      }
    ],
    "points": {
      "total": integer,
      "lastUpdated": "string",
      "currency": "string"
    }
  }
]
```

**Example Request:**

```
GET /sdui/plugins?feature=mastercard-benefits&feature=loyalty-points
```

**Example Response:**

```json
[
  {
    "type": "card-benefits-plugin",
    "feature": "mastercard-benefits",
    "modifier": {
      "padding": "16dp",
      "margin": "12dp",
      "alignment": "center"
    },
    "style": {
      "backgroundColor": "#FFFFFF",
      "borderRadius": "8dp",
      "shadow": true,
      "textColor": "#333333",
      "fontFamily": "Roboto"
    },
    "benefits": [
      {
        "profile": "Black",
        "benefits": [
          "Acesso ilimitado a salas VIP em aeroportos (LoungeKey)",
          "Serviço de concierge 24h",
          "MasterSeguro de Viagem completo"
        ]
      }
    ]
  },
  {
    "type": "points-summary-plugin",
    "feature": "loyalty-points",
    "modifier": {
      "padding": "16dp",
      "margin": "12dp",
      "alignment": "center"
    },
    "style": {
      "backgroundColor": "#EFEFEF",
      "textColor": "#222222",
      "fontWeight": "bold",
      "borderRadius": "6dp"
    },
    "points": {
      "total": 12450,
      "lastUpdated": "2025-07-14T15:32:00Z",
      "currency": "pontos"
    }
  }
]
```

## OpenAPI Documentation

The API is documented using OpenAPI/Swagger. You can access the Swagger UI at `/swagger-ui` when the application is running to interactively explore and test the API endpoints.
