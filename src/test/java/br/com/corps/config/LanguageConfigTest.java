package br.com.corps.config;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class LanguageConfigTest {

    @Inject
    private LanguageConfig languageConfig;

    @Test
    void testDefaultLanguage() {
        assertEquals("pt-BR", languageConfig.getDefaultLanguage());
    }

    @Test
    void testSupportedLanguages() {
        assertTrue(languageConfig.isSupported("pt-BR"));
        assertTrue(languageConfig.isSupported("en-US"));
        assertTrue(languageConfig.isSupported("es-ES"));
        assertFalse(languageConfig.isSupported("fr-FR"));
        assertFalse(languageConfig.isSupported("de-DE"));
    }

    @ParameterizedTest
    @CsvSource({
        "pt,pt-BR",
        "pt-BR,pt-BR",
        "en,en-US",
        "en-US,en-US",
        "es,es-ES",
        "es-ES,es-ES",
        "fr-FR,pt-BR",
        "de-DE,pt-BR"
    })
    void testNormalizeLanguage(String input, String expected) {
        assertEquals(expected, languageConfig.normalizeLanguage(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNormalizeLanguageWithNullOrEmpty(String input) {
        assertEquals("pt-BR", languageConfig.normalizeLanguage(input));
    }

    @Test
    void testNormalizeLanguageWithUnsupportedLanguage() {
        assertEquals("pt-BR", languageConfig.normalizeLanguage("fr-FR"));
    }

    @Test
    void testNormalizeLanguageWithMixedCase() {
        assertEquals("pt-BR", languageConfig.normalizeLanguage("PT-br"));
        assertEquals("en-US", languageConfig.normalizeLanguage("EN"));
        assertEquals("es-ES", languageConfig.normalizeLanguage("Es"));
    }
}
