package br.com.corps.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration properties for language settings
 */
@ConfigurationProperties("app.language")
@Context
@Getter
@NoArgsConstructor
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
     * Normalize a language code to a supported format
     *
     * @param language the language code to normalize
     * @return the normalized language code, or the default language if not supported
     */
    public String normalizeLanguage(String language) {
        if (language == null) {
            return defaultLanguage;
        }
        
        // Check if the language is already in the correct format
        if (isSupported(language)) {
            return language;
        }
        
        // Try to match with supported languages
        for (String supported : supportedLanguages) {
            if (supported.toLowerCase().startsWith(language.toLowerCase())) {
                return supported;
            }
        }
        
        // Return default language if no match found
        return defaultLanguage;
    }
}
