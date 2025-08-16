# Sistema de Tradução Baseado em Chaves

Este documento descreve o sistema de tradução baseado em chaves implementado no projeto SDUI.

## Visão Geral

O sistema de tradução baseado em chaves permite separar o conteúdo textual dos objetos JSON, armazenando-os em arquivos de tradução específicos para cada idioma. Isso facilita a manutenção e adição de novos idiomas sem precisar duplicar toda a estrutura dos objetos.

## Estrutura de Arquivos

### Arquivos de Tradução

Os arquivos de tradução estão organizados por segmento de cartão e idioma:

```
/resources/i18n/
  /black/
    en-US.json
    pt-BR.json
    es-ES.json
  /gold/
    en-US.json
    pt-BR.json
    es-ES.json
  /platinum/
    en-US.json
    pt-BR.json
    es-ES.json
```

### Formato dos Arquivos de Tradução

Cada arquivo de tradução contém pares de chave-valor, onde a chave é um identificador único e o valor é o texto traduzido:

```json
{
  "benefits_card_title": "Black Card Benefits",
  "unlimited_access_vip_lounges": "Unlimited access to airport VIP lounges",
  "travel_insurance": "Travel insurance up to $1,000,000"
}
```

## Arquivos JSON com Chaves de Tradução

Os arquivos JSON que usam chaves de tradução têm a seguinte estrutura:

```json
{
  "type": "exclusive-area",
  "feature": "black-card",
  "children": [
    {
      "type": "benefits-section",
      "feature": "black-benefits",
      "benefits": [
        {
          "text": "key:unlimited_access_vip_lounges",
          "icon": "https://cdn.mastercard.com/content/assets/icons/airport.svg"
        },
        {
          "text": "key:travel_insurance",
          "icon": "https://cdn.mastercard.com/content/assets/icons/insurance.svg"
        }
      ],
      "title": "key:benefits_card_title"
    }
  ]
}
```

Note que os valores de texto que precisam ser traduzidos são prefixados com `key:` seguido pelo identificador da tradução.

## Serviço de Tradução

O `TranslationService` é responsável por carregar os arquivos de tradução e substituir as chaves pelos textos traduzidos:

```java
@Singleton
public class TranslationService {
    // ...
    
    public List<Plugin> translatePlugins(List<Plugin> plugins, String cardType, String language) {
        String normalizedLanguage = languageConfig.normalizeLanguage(language);
        Map<String, String> translations = getTranslations(cardType, normalizedLanguage);
        
        return plugins.stream()
                .map(plugin -> translatePlugin(plugin, translations))
                .collect(Collectors.toList());
    }
    
    // ...
}
```

## Como Usar

### 1. Definir Chaves de Tradução

Adicione novas chaves e traduções nos arquivos de tradução:

```json
// i18n/black/pt-BR.json
{
  "new_benefit_key": "Novo benefício em português"
}

// i18n/black/en-US.json
{
  "new_benefit_key": "New benefit in English"
}
```

### 2. Usar Chaves nos Arquivos JSON

Use a chave no arquivo JSON prefixando com `key:`:

```json
{
  "text": "key:new_benefit_key",
  "icon": "https://cdn.mastercard.com/content/assets/icons/new.svg"
}
```

### 3. Processar Traduções no Controller

O controller usa o serviço de tradução para processar as chaves:

```java
@Get("/{profile}/benefits")
public HttpResponse<ApiResponse<List<Plugin>>> getCardBenefits(
        @PathVariable String profile,
        @Header(name = "Accept-Language", defaultValue = "pt-BR") String acceptLanguage) {
    
    String normalizedLanguage = languageConfig.normalizeLanguage(acceptLanguage);
    List<Plugin> plugins = s3ResourceService.loadCardBenefits(profile, normalizedLanguage);
    
    // Aplicar traduções usando o serviço de tradução baseado em chaves
    List<Plugin> translatedPlugins = translationService.translatePlugins(plugins, profile, normalizedLanguage);
    
    return HttpResponse.ok(
        ApiResponse.success(translatedPlugins, normalizedLanguage)
    );
}
```

## Fallback para Idioma Padrão

Se uma chave não for encontrada no idioma solicitado ou se o idioma não for suportado, o sistema usará o idioma padrão (pt-BR).

## Vantagens

1. **Manutenção simplificada**: Todas as traduções estão centralizadas em arquivos específicos para cada idioma.
2. **Facilidade para adicionar novos idiomas**: Basta criar novos arquivos de tradução sem modificar a estrutura dos objetos JSON.
3. **Reutilização de traduções**: A mesma chave pode ser usada em múltiplos lugares.
4. **Consistência**: Garante que o mesmo texto seja traduzido da mesma forma em toda a aplicação.
