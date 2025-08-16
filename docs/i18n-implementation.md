# Implementação de Internacionalização (i18n) no SDUI

Este documento descreve a implementação do sistema de internacionalização (i18n) no projeto SDUI, que permite a tradução de conteúdo para múltiplos idiomas.

## Visão Geral

O sistema de internacionalização do SDUI foi projetado para fornecer uma experiência multilíngue completa, permitindo que o conteúdo dos plugins seja exibido em diferentes idiomas. A implementação suporta:

- Tradução baseada em chaves (key-based translation)
- Suporte para múltiplos idiomas (pt-BR, en-US, es-ES)
- Seleção de idioma via parâmetro de consulta ou parâmetro de caminho
- Fallback para idioma padrão quando uma tradução não está disponível

## Idiomas Suportados

Atualmente, o sistema suporta os seguintes idiomas:

- Português (Brasil): `pt-BR` (idioma padrão)
- Inglês (Estados Unidos): `en-US`
- Espanhol (Espanha): `es-ES`

## Estrutura de Arquivos

### Arquivos de Tradução

Os arquivos de tradução são organizados da seguinte forma:

```
src/main/resources/
├── i18n/
│   ├── black/
│   │   ├── pt-BR.json
│   │   ├── en-US.json
│   │   └── es-ES.json
│   ├── gold/
│   │   ├── pt-BR.json
│   │   ├── en-US.json
│   │   └── es-ES.json
│   └── platinum/
│       ├── pt-BR.json
│       ├── en-US.json
│       └── es-ES.json
└── s3/
    └── exclusive-area/
        ├── black/
        │   └── home/
        │       ├── pt-BR.json
        │       ├── en-US.json
        │       ├── es-ES.json
        │       └── key-based/
        │           ├── pt-BR.json
        │           ├── en-US.json
        │           └── es-ES.json
        ├── gold/
        └── platinum/
```

### Formato dos Arquivos de Tradução

Os arquivos de tradução são arquivos JSON que contêm pares de chave-valor, onde a chave é o identificador da string e o valor é a tradução para o idioma específico.

Exemplo (`i18n/black/pt-BR.json`):
```json
{
  "benefits_card_title": "Benefícios do Cartão Black",
  "unlimited_access_vip_lounges": "Acesso ilimitado a salas VIP em aeroportos (LoungeKey)",
  "travel_insurance": "Seguro viagem de até R$5.000.000",
  "concierge_service": "Serviço de concierge 24 horas",
  "mastercard_rewards": "Programa Mastercard Rewards com benefícios exclusivos",
  "cashback_purchases": "2% de cashback em todas as compras",
  "emergency_assistance": "Assistência global de emergência"
}
```

## Componentes Principais

### LanguageConfig

A classe `LanguageConfig` gerencia a configuração de idiomas, incluindo:

- Lista de idiomas suportados
- Idioma padrão
- Normalização de códigos de idioma

### TranslationService

O serviço de tradução é responsável por:

1. Carregar as traduções dos arquivos JSON
2. Traduzir objetos `Plugin` e seus componentes aninhados
3. Gerenciar cache de traduções para melhor desempenho
4. Fornecer fallback para o idioma padrão quando necessário

O serviço identifica textos que precisam ser traduzidos pelo prefixo `key:`. Por exemplo, `key:benefits_card_title` será substituído pela tradução correspondente no idioma selecionado.

### SDUIController

O controlador REST expõe endpoints que suportam seleção de idioma de duas maneiras:

1. **Via parâmetro de consulta**:
   ```
   GET /sdui/plugins?feature=black-card&lang=en-US
   ```

2. **Via parâmetro de caminho**:
   ```
   GET /sdui/en-US/plugins?feature=black-card
   ```

## Como Usar

### Adicionar Novas Traduções

Para adicionar uma nova tradução:

1. Identifique o tipo de cartão (black, gold, platinum)
2. Adicione a chave e o valor traduzido no arquivo JSON correspondente ao idioma

### Adicionar Suporte para um Novo Idioma

Para adicionar suporte para um novo idioma:

1. Crie arquivos JSON para o novo idioma em cada diretório de tipo de cartão
2. Atualize a classe `LanguageConfig` para incluir o novo idioma na lista de idiomas suportados

### Usar Traduções em Plugins

Para usar traduções em um plugin:

1. Defina o texto com o prefixo `key:` seguido pelo identificador da tradução
   ```json
   {
     "text": "key:benefits_card_title",
     "icon": "https://example.com/icon.svg"
   }
   ```

2. O serviço de tradução substituirá automaticamente a chave pela tradução correspondente no idioma selecionado

## Documentação da API

A API REST é documentada usando OpenAPI/Swagger e pode ser acessada em:

```
http://localhost:8080/swagger-ui/
```

A documentação inclui todos os endpoints disponíveis, parâmetros necessários e exemplos de uso.

## Testes

O sistema de internacionalização é coberto por testes unitários que verificam:

- Tradução correta para diferentes idiomas
- Fallback para o idioma padrão quando uma tradução não está disponível
- Comportamento correto dos endpoints REST com diferentes parâmetros de idioma

## Considerações de Desempenho

- As traduções são carregadas sob demanda e armazenadas em cache para melhorar o desempenho
- O serviço de tradução usa clonagem de objetos para evitar efeitos colaterais
- A estrutura hierárquica dos plugins é preservada durante a tradução
