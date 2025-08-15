package br.com.corps.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration properties for language settings
 */
@ConfigurationProperties("app.language")
@Context
@Data
public class LanguageConfig {

    /**
     * Default language to use when no language is specified
     */
    private String defaultLanguage = "pt-BR";

    /**
     * List of supported languages
     */
    private List<String> supportedLanguages = Arrays.asList("pt-BR", "en-US", "es-ES");

    /**
     * Check if a language is supported
     *
     * @param language the language code to check
     * @return true if the language is supported, false otherwise
     */
    public boolean isSupported(String language) {
        return language != null && supportedLanguages.contains(language);
    }

    /**
     * Get the default language
     *
     * @return the default language code
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Normalize a language code
     *
     * @param language the language code to normalize
     * @return the normalized language code
     */
    public String normalizeLanguage(String language) {
        if (language == null || language.isEmpty()) {
            return defaultLanguage;
        }

        // Handle common variations
        if (language.equalsIgnoreCase("en")) {
            return "en-US";
        } else if (language.equalsIgnoreCase("es")) {
            return "es-ES";
        } else if (language.equalsIgnoreCase("pt")) {
            return "pt-BR";
        }

        // Check if the language is supported
        if (isSupported(language)) {
            return language;
        }

        return defaultLanguage;
    }
}
