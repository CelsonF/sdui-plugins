package br.com.corps.service;

import br.com.corps.config.LanguageConfig;
import br.com.corps.model.Plugin;
import br.com.corps.model.Benefit;
import br.com.corps.model.BenefitGroup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for handling translations using key-based approach
 */
@Singleton
@Context
@Slf4j
@RequiredArgsConstructor
public class TranslationService {

    private static final String KEY_PREFIX = "key:";
    private static final int KEY_PREFIX_LENGTH = KEY_PREFIX.length();
    
    private final LanguageConfig languageConfig;
    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, Map<String, String>>> translationsCache = new ConcurrentHashMap<>();

    /**
     * Translate a list of plugins using key-based translations
     *
     * @param plugins List of plugins to translate
     * @param cardType Card type (black, gold, platinum)
     * @param language Target language
     * @return Translated list of plugins
     */
    public List<Plugin> translatePlugins(List<Plugin> plugins, String cardType, String language) {
        if (plugins == null || plugins.isEmpty()) {
            return Collections.emptyList();
        }
        
        String normalizedLanguage = languageConfig.normalizeLanguage(language);
        Map<String, String> translations = getTranslations(cardType, normalizedLanguage);

        return plugins.stream()
                .map(plugin -> translatePlugin(plugin, translations))
                .collect(Collectors.toList());
    }

    /**
     * Translate a single plugin using key-based translations
     *
     * @param plugin Plugin to translate
     * @param translations Translation map
     * @return Translated plugin
     */
    private Plugin translatePlugin(Plugin plugin, Map<String, String> translations) {
        if (plugin == null) {
            return null;
        }
        
        // Clone the plugin using Lombok's builder pattern
        Plugin translatedPlugin = Plugin.builder()
            .type(plugin.getType())
            .feature(plugin.getFeature())
            .modifier(plugin.getModifier())
            .style(plugin.getStyle())
            .points(plugin.getPoints())
            .build();
        
        // Translate benefit groups if they exist
        if (plugin.getBenefits() != null) {
            List<BenefitGroup> translatedBenefitGroups = plugin.getBenefits().stream()
                    .map(benefitGroup -> translateBenefitGroup(benefitGroup, translations))
                    .collect(Collectors.toList());
            translatedPlugin.setBenefits(translatedBenefitGroups);
        }
        
        // Translate children plugins recursively if they exist
        if (plugin.getChildren() != null && !plugin.getChildren().isEmpty()) {
            List<Plugin> translatedChildren = plugin.getChildren().stream()
                    .map(child -> translatePlugin(child, translations))
                    .collect(Collectors.toList());
            translatedPlugin.setChildren(translatedChildren);
        }
        
        return translatedPlugin;
    }

    /**
     * Translate a benefit group using key-based translations
     *
     * @param benefitGroup BenefitGroup to translate
     * @param translations Translation map
     * @return Translated benefit group
     */
    private BenefitGroup translateBenefitGroup(BenefitGroup benefitGroup, Map<String, String> translations) {
        if (benefitGroup == null) {
            return null;
        }
        
        // Clone the benefit group using Lombok's builder pattern
        BenefitGroup translatedGroup = BenefitGroup.builder()
            .profile(benefitGroup.getProfile())
            .build();
        
        if (benefitGroup.getBenefits() != null) {
            List<Benefit> translatedBenefits = benefitGroup.getBenefits().stream()
                    .map(benefit -> translateBenefit(benefit, translations))
                    .collect(Collectors.toList());
            translatedGroup.setBenefits(translatedBenefits);
        }
        
        return translatedGroup;
    }

    /**
     * Translate a benefit using key-based translations
     *
     * @param benefit Benefit to translate
     * @param translations Translation map
     * @return Translated benefit
     */
    private Benefit translateBenefit(Benefit benefit, Map<String, String> translations) {
        if (benefit == null) {
            return null;
        }
        
        String translatedText = benefit.getText();
        
        // If the text is a translation key, replace it with the translation
        if (benefit.getText() != null && benefit.getText().startsWith(KEY_PREFIX)) {
            String key = benefit.getText().substring(KEY_PREFIX_LENGTH);
            translatedText = translations.getOrDefault(key, benefit.getText());
        }
        
        // Create a new benefit with the translated text using Lombok's constructor
        return new Benefit(translatedText, benefit.getIcon());
    }

    /**
     * Get translations for a specific card type and language
     *
     * @param cardType Card type (black, gold, platinum)
     * @param language Target language
     * @return Map of translations
     */
    private Map<String, String> getTranslations(String cardType, String language) {
        // Use default card type if null
        String cardTypeKey = cardType != null ? cardType : "black";
        // Use default language if null
        String languageKey = language != null ? language : languageConfig.getDefaultLanguage();
        
        // Initialize cache for card type if not exists
        translationsCache.computeIfAbsent(cardTypeKey, k -> new ConcurrentHashMap<>());
        
        // Get or load translations for language
        Map<String, Map<String, String>> cardTypeCache = translationsCache.get(cardTypeKey);
        if (cardTypeCache.containsKey(languageKey)) {
            return cardTypeCache.get(languageKey);
        } else {
            Map<String, String> translations = loadTranslations(languageKey);
            cardTypeCache.put(languageKey, translations);
            return translations;
        }
    }

    /**
     * Load translations from JSON file
     *
     * @param language Target language
     * @return Map of translations
     */
    @SneakyThrows
    private Map<String, String> loadTranslations(String language) {
        String normalizedLanguage = languageConfig.normalizeLanguage(language);
        String defaultLanguage = languageConfig.getDefaultLanguage();
        
        // Try to load translations for the requested language
        Map<String, String> translations = loadTranslationsFromFile(normalizedLanguage);
        
        // If translations are empty and the requested language is not the default,
        // fall back to the default language
        if ((translations == null || translations.isEmpty()) && !normalizedLanguage.equals(defaultLanguage)) {
            log.warn("Translations not found for language: {}. Falling back to default language: {}", 
                    normalizedLanguage, defaultLanguage);
            translations = loadTranslationsFromFile(defaultLanguage);
        }
        
        return translations != null ? translations : Collections.emptyMap();
    }

    /**
     * Load translations from a JSON file for a specific language
     *
     * @param language Language code
     * @return Map of translations
     */
    private Map<String, String> loadTranslationsFromFile(String language) {
        String path = String.format("i18n/black/%s.json", language);
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                log.warn("Translation file not found: {}", path);
                return Collections.emptyMap();
            }
            
            return objectMapper.readValue(is, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            log.error("Error loading translations from {}: {}", path, e.getMessage(), e);
            return Collections.emptyMap();
        }
    }
}
