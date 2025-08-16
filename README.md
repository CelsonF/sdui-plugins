## Micronaut 4.9.1 Documentation

- [User Guide](https://docs.micronaut.io/4.9.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.9.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.9.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

# SDUI - Server-Driven UI API

API para fornecer dados de interface de usuário controlados pelo servidor (Server-Driven UI) com suporte completo a múltiplos idiomas.

## Visão Geral

O SDUI (Server-Driven UI) é uma API que fornece dados estruturados para construção dinâmica de interfaces de usuário em aplicativos móveis e web. A API suporta:

- Conteúdo multilíngue (pt-BR, en-US, es-ES)
- Estrutura hierárquica de plugins
- Benefícios específicos por tipo de cartão (black, gold, platinum)
- Documentação OpenAPI/Swagger integrada

## Requisitos

- Java 17+
- Maven 3.8+

## Tecnologias

- Micronaut 4.9.1
- Lombok
- Jackson
- JUnit 5
- OpenAPI/Swagger

## Endpoints da API

### Obter Plugins por Feature

#### Via Query Parameter

```
GET /sdui/plugins?feature={feature}&lang={lang}
```

**Parâmetros:**
- `feature` - Lista de features para filtrar (obrigatório)
- `lang` - Código do idioma (opcional, padrão: pt-BR)

**Exemplo:**
```
GET /sdui/plugins?feature=black-card&lang=en-US
```

#### Via Path Parameter

```
GET /sdui/{lang}/plugins?feature={feature}
```

**Parâmetros:**
- `lang` - Código do idioma (obrigatório)
- `feature` - Lista de features para filtrar (obrigatório)

**Exemplo:**
```
GET /sdui/en-US/plugins?feature=black-card
```

### Obter Todos os Plugins

#### Via Query Parameter

```
GET /sdui/plugins/all?lang={lang}
```

**Parâmetros:**
- `lang` - Código do idioma (opcional, padrão: pt-BR)

**Exemplo:**
```
GET /sdui/plugins/all?lang=es-ES
```

#### Via Path Parameter

```
GET /sdui/{lang}/plugins/all
```

**Parâmetros:**
- `lang` - Código do idioma (obrigatório)

**Exemplo:**
```
GET /sdui/es-ES/plugins/all
```

## Estrutura de Resposta

```json
[
  {
    "type": "card",
    "feature": "black-card",
    "benefits": [
      {
        "text": "Acesso ilimitado a salas VIP em aeroportos",
        "icon": "https://cdn.mastercard.com/content/assets/icons/airport.svg"
      },
      {
        "text": "Seguro viagem de até R$5.000.000",
        "icon": "https://cdn.mastercard.com/content/assets/icons/insurance.svg"
      }
    ],
    "children": [
      {
        "type": "benefits-section",
        "feature": "black-benefits",
        "title": "Benefícios do Cartão Black"
      }
    ]
  }
]
```

## Idiomas Suportados

- Português (Brasil): `pt-BR` (idioma padrão)
- Inglês (Estados Unidos): `en-US`
- Espanhol (Espanha): `es-ES`

## Sistema de Tradução

O sistema de tradução é baseado em chaves. Textos que precisam ser traduzidos são marcados com o prefixo `key:` seguido pelo identificador da tradução. Por exemplo:

```json
{
  "text": "key:unlimited_access_vip_lounges",
  "icon": "https://cdn.mastercard.com/content/assets/icons/airport.svg"
}
```

Os arquivos de tradução estão organizados por tipo de cartão e idioma:

```
src/main/resources/i18n/{card-type}/{language}.json
```

## Documentação da API

A documentação completa da API está disponível via Swagger UI:

```
http://localhost:8080/swagger-ui/
```

## Executando o Projeto

### Compilação

```bash
mvn clean package
```

### Execução

```bash
java -jar target/sdui-0.1.jar
```

## Testes

```bash
mvn test
```

## Estrutura do Projeto

```
sdui/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/corps/
│   │   │       ├── config/         # Configurações
│   │   │       ├── controller/     # Controladores REST
│   │   │       ├── model/          # Modelos de dados
│   │   │       └── service/        # Serviços
│   │   └── resources/
│   │       ├── i18n/               # Arquivos de tradução
│   │       └── s3/                 # Dados de plugins
│   └── test/
│       └── java/
│           └── br/com/corps/
│               ├── controller/     # Testes de controladores
│               └── service/        # Testes de serviços
└── docs/                           # Documentação
```

## Notas de Compatibilidade

Este projeto foi otimizado para Java 17 e pode apresentar problemas de compatibilidade com Java 21 devido a incompatibilidades com o Micronaut.

---

## Documentação Micronaut

- [Guia do Usuário](https://docs.micronaut.io/4.9.1/guide/index.html)
- [Referência da API](https://docs.micronaut.io/4.9.1/api/index.html)
- [Referência de Configuração](https://docs.micronaut.io/4.9.1/guide/configurationreference.html)
- [Guias Micronaut](https://guides.micronaut.io/index.html)

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
