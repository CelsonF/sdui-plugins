## Micronaut 4.9.1 Documentation

- [User Guide](https://docs.micronaut.io/4.9.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.9.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.9.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

# SDUI Plugins API

## Card Benefits API

The Card Benefits API provides access to card-specific benefits with multilingual support.

### Endpoints

#### Get Card Benefits

```
GET /cards/{profile}/benefits
```

Retrieves benefits for a specific card profile (black, gold, platinum) in the requested language.

**Path Parameters:**
- `profile` - Card profile (black, gold, platinum)

**Headers:**
- `Accept-Language` - Preferred language code (default: pt-BR)

**Supported Languages:**
- Portuguese (pt-BR) - Default
- English (en-US)
- Spanish (es-ES)

**Example Request:**
```
GET /cards/black/benefits
Accept-Language: en-US
```

**Example Response:**
```json
{
  "status": "success",
  "message": "Data retrieved successfully",
  "language": "en-US",
  "data": [
    {
      "feature": "mastercard-benefits",
      "title": "Mastercard Black Benefits",
      "benefitGroups": [
        {
          "title": "Travel",
          "benefits": [
            {
              "text": "Access to VIP lounges",
              "icon": "https://mastercard.com/assets/icons/airport-lounge.png"
            },
            {
              "text": "Travel insurance",
              "icon": "https://mastercard.com/assets/icons/insurance.png"
            }
          ]
        }
      ]
    }
  ]
}
```

### Language Fallback

If an unsupported language is requested, the API will automatically fall back to the default language (pt-BR).

---

- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)
## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)


## Feature lombok documentation

- [Micronaut Project Lombok documentation](https://docs.micronaut.io/latest/guide/index.html#lombok)

- [https://projectlombok.org/features/all](https://projectlombok.org/features/all)


## Feature http-server-jdk documentation

- [Micronaut Built-In Java HTTP Server Runtime documentation](https://micronaut-projects.github.io/micronaut-servlet/latest/guide/#httpServer)

- [https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)
