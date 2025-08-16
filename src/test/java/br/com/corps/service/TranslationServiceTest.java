package br.com.corps.service;

import br.com.corps.config.LanguageConfig;
import br.com.corps.model.Benefit;
import br.com.corps.model.BenefitGroup;
import br.com.corps.model.Plugin;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MicronautTest
public class TranslationServiceTest {

    @Inject
    private ObjectMapper objectMapper;

    @Mock
    private LanguageConfig languageConfig;

    private TranslationService translationService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Configure language config mock
        when(languageConfig.normalizeLanguage("en")).thenReturn("en-US");
        when(languageConfig.normalizeLanguage("en-US")).thenReturn("en-US");
        when(languageConfig.normalizeLanguage("pt")).thenReturn("pt-BR");
        when(languageConfig.normalizeLanguage("pt-BR")).thenReturn("pt-BR");
        when(languageConfig.normalizeLanguage("es")).thenReturn("es-ES");
        when(languageConfig.normalizeLanguage("es-ES")).thenReturn("es-ES");
        when(languageConfig.getDefaultLanguage()).thenReturn("pt-BR");
        
        // Create test translation files
        createTestTranslationFiles();
        
        translationService = new TranslationService(languageConfig, objectMapper);
    }

    private void createTestTranslationFiles() throws IOException {
        // Create i18n directory structure
        Path i18nDir = tempDir.resolve("i18n");
        Path blackDir = i18nDir.resolve("black");
        Path goldDir = i18nDir.resolve("gold");
        
        Files.createDirectories(blackDir);
        Files.createDirectories(goldDir);
        
        // Create English translations for Black card
        String enUsContent = "{\n" +
                "  \"benefits_card_title\": \"Black Card Benefits\",\n" +
                "  \"unlimited_access_vip_lounges\": \"Unlimited access to airport VIP lounges\",\n" +
                "  \"travel_insurance\": \"Travel insurance up to $1,000,000\"\n" +
                "}";
        Files.writeString(blackDir.resolve("en-US.json"), enUsContent);
        
        // Create Portuguese translations for Black card
        String ptBrContent = "{\n" +
                "  \"benefits_card_title\": \"Benefícios do Cartão Black\",\n" +
                "  \"unlimited_access_vip_lounges\": \"Acesso ilimitado a salas VIP em aeroportos\",\n" +
                "  \"travel_insurance\": \"Seguro viagem de até R$5.000.000\"\n" +
                "}";
        Files.writeString(blackDir.resolve("pt-BR.json"), ptBrContent);
    }

    @Test
    void testTranslatePlugins() {
        // Create test plugins with translation keys
        List<Plugin> plugins = createTestPlugins();
        
        // Translate plugins to English
        List<Plugin> translatedPlugins = translationService.translatePlugins(plugins, "black", "en-US");
        
        // Verify translations
        assertNotNull(translatedPlugins);
        assertEquals(1, translatedPlugins.size());
        
        Plugin translatedPlugin = translatedPlugins.get(0);
        List<BenefitGroup> benefitGroups = translatedPlugin.getBenefits();
        assertNotNull(benefitGroups);
        assertEquals(1, benefitGroups.size());
        
        List<Benefit> benefits = benefitGroups.get(0).getBenefits();
        assertNotNull(benefits);
        assertEquals(2, benefits.size());
        
        // Check first benefit translation
        String expectedText = benefits.get(0).getText();
        assertTrue(expectedText.contains("Unlimited access to airport VIP lounges"), 
                "Expected text to contain 'Unlimited access to airport VIP lounges', but was: " + expectedText);
        assertEquals("https://cdn.mastercard.com/content/assets/icons/airport.svg", benefits.get(0).getIcon());
        
        // Check second benefit translation
        String expectedText2 = benefits.get(1).getText();
        assertTrue(expectedText2.contains("Travel insurance"), 
                "Expected text to contain 'Travel insurance', but was: " + expectedText2);
        assertEquals("https://cdn.mastercard.com/content/assets/icons/insurance.svg", benefits.get(1).getIcon());
    }

    @Test
    void testTranslatePluginsWithFallback() {
        // Create test plugins with translation keys
        List<Plugin> plugins = createTestPlugins();
        
        // Test with unsupported language - should fall back to default
        List<Plugin> translatedPlugins = translationService.translatePlugins(plugins, "black", "fr-FR");
        
        // Verify translations (should be in Portuguese - default language)
        assertNotNull(translatedPlugins);
        assertEquals(1, translatedPlugins.size());
        
        Plugin translatedPlugin = translatedPlugins.get(0);
        List<BenefitGroup> benefitGroups = translatedPlugin.getBenefits();
        assertNotNull(benefitGroups);
        assertEquals(1, benefitGroups.size());
        
        List<Benefit> benefits = benefitGroups.get(0).getBenefits();
        assertNotNull(benefits);
        assertEquals(2, benefits.size());
        
        // Check first benefit translation (should be in Portuguese)
        assertTrue(benefits.get(0).getText().contains("Acesso ilimitado a salas VIP em aeroportos"), 
                "Expected text to contain 'Acesso ilimitado a salas VIP em aeroportos'");
        assertEquals("https://cdn.mastercard.com/content/assets/icons/airport.svg", benefits.get(0).getIcon());
    }
    
    @Test
    void testTranslateBenefitGroup() {
        // Setup
        BenefitGroup benefitGroup = new BenefitGroup();
        Benefit benefit = new Benefit();
        benefit.setText("key:unlimited_access_vip_lounges");
        benefit.setIcon("https://cdn.mastercard.com/content/assets/icons/airport.svg");
        benefitGroup.setBenefits(Arrays.asList(benefit));
        
        // Get translations map
        Map<String, String> translations = getTranslations("black", "en-US");
        
        // Use reflection to access private method
        BenefitGroup translatedGroup = translateBenefitGroupWithReflection(benefitGroup, translations);
        
        // Verify translation
        assertNotNull(translatedGroup);
        List<Benefit> benefits = translatedGroup.getBenefits();
        assertNotNull(benefits);
        assertEquals(1, benefits.size());
        assertEquals("Unlimited access to airport VIP lounges", benefits.get(0).getText());
    }
    
    @Test
    void testTranslateBenefitGroupWithMissingKey() {
        // Setup
        BenefitGroup benefitGroup = new BenefitGroup();
        Benefit benefit = new Benefit();
        benefit.setText("key:non_existent_key");
        benefit.setIcon("https://cdn.mastercard.com/content/assets/icons/generic.svg");
        benefitGroup.setBenefits(Arrays.asList(benefit));
        
        // Get translations map
        Map<String, String> translations = getTranslations("black", "en-US");
        
        // Use reflection to access private method
        BenefitGroup translatedGroup = translateBenefitGroupWithReflection(benefitGroup, translations);
        
        // Verify that missing key is preserved as is
        assertNotNull(translatedGroup);
        List<Benefit> benefits = translatedGroup.getBenefits();
        assertNotNull(benefits);
        assertEquals(1, benefits.size());
        assertEquals("key:non_existent_key", benefits.get(0).getText());
    }
    
    @Test
    void testTranslateBenefitGroupWithFallback() {
        // Setup
        BenefitGroup benefitGroup = new BenefitGroup();
        Benefit benefit = new Benefit();
        benefit.setText("key:unlimited_access_vip_lounges");
        benefit.setIcon("https://cdn.mastercard.com/content/assets/icons/airport.svg");
        benefitGroup.setBenefits(Arrays.asList(benefit));
        
        // Get translations map for default language (pt-BR)
        Map<String, String> translations = getTranslations("black", "pt-BR");
        
        // Use reflection to access private method
        BenefitGroup translatedGroup = translateBenefitGroupWithReflection(benefitGroup, translations);
        
        // Verify translation (should be in Portuguese - default language)
        assertNotNull(translatedGroup);
        List<Benefit> benefits = translatedGroup.getBenefits();
        assertNotNull(benefits);
        assertEquals(1, benefits.size());
        assertTrue(benefits.get(0).getText().contains("Acesso ilimitado a salas VIP em aeroportos"), 
                "Expected text to contain 'Acesso ilimitado a salas VIP em aeroportos'");
    }

    @Test
    void testTranslatePluginsWithMissingKey() {
        // Setup
        List<Plugin> plugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        
        BenefitGroup benefitGroup = new BenefitGroup();
        Benefit benefit = new Benefit();
        benefit.setText("key:non_existent_key");
        benefit.setIcon("https://cdn.mastercard.com/content/assets/icons/generic.svg");
        
        benefitGroup.setBenefits(Arrays.asList(benefit));
        plugin.setBenefits(Arrays.asList(benefitGroup));
        plugins.add(plugin);
        
        // Translate plugins
        List<Plugin> translatedPlugins = translationService.translatePlugins(plugins, "black", "en-US");
        
        // Verify that missing key is preserved as is
        assertNotNull(translatedPlugins);
        assertEquals(1, translatedPlugins.size());
        
        Plugin translatedPlugin = translatedPlugins.get(0);
        List<BenefitGroup> benefitGroups = translatedPlugin.getBenefits();
        
        // The key should be preserved since it doesn't exist in translations
        assertEquals("key:non_existent_key", benefitGroups.get(0).getBenefits().get(0).getText());
    }

    @Test
    void testTranslatePluginsWithNonKeyText() {
        // Create test plugins with regular text (not keys)
        List<Plugin> plugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        plugin.setType("benefits-section");
        plugin.setFeature("black-benefits");
        
        BenefitGroup benefitGroup = new BenefitGroup();
        Benefit benefit = new Benefit();
        benefit.setText("Regular text without key prefix");
        benefit.setIcon("https://cdn.mastercard.com/content/assets/icons/generic.svg");
        
        benefitGroup.setBenefits(Arrays.asList(benefit));
        plugin.setBenefits(Arrays.asList(benefitGroup));
        plugins.add(plugin);
        
        // Translate plugins
        List<Plugin> translatedPlugins = translationService.translatePlugins(plugins, "black", "en-US");
        
        // Verify that regular text is preserved as is
        assertNotNull(translatedPlugins);
        assertEquals(1, translatedPlugins.size());
        
        Plugin translatedPlugin = translatedPlugins.get(0);
        List<BenefitGroup> benefitGroups = translatedPlugin.getBenefits();
        
        // Regular text should be preserved
        assertEquals("Regular text without key prefix", benefitGroups.get(0).getBenefits().get(0).getText());
    }

    private List<Plugin> createTestPlugins() {
        List<Plugin> plugins = new ArrayList<>();
        
        Plugin plugin = new Plugin();
        plugin.setType("benefits-section");
        plugin.setFeature("black-benefits");
        
        BenefitGroup benefitGroup = new BenefitGroup();
        Benefit benefit1 = new Benefit();
        benefit1.setText("key:unlimited_access_vip_lounges");
        benefit1.setIcon("https://cdn.mastercard.com/content/assets/icons/airport.svg");
        
        Benefit benefit2 = new Benefit();
        benefit2.setText("key:travel_insurance");
        benefit2.setIcon("https://cdn.mastercard.com/content/assets/icons/insurance.svg");
        
        benefitGroup.setBenefits(Arrays.asList(benefit1, benefit2));
        plugin.setBenefits(Arrays.asList(benefitGroup));
        plugins.add(plugin);
        
        return plugins;
    }

    /**
     * Helper method to get translations map for testing
     */
    private Map<String, String> getTranslations(String cardType, String language) {
        try {
            // Load translations from test files
            Path i18nDir = tempDir.resolve("i18n");
            Path cardDir = i18nDir.resolve(cardType);
            Path translationFile = cardDir.resolve(language + ".json");
            
            if (Files.exists(translationFile)) {
                String content = Files.readString(translationFile);
                return objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});
            } else {
                // Fallback to default language
                String defaultLanguage = languageConfig.getDefaultLanguage();
                Path defaultFile = cardDir.resolve(defaultLanguage + ".json");
                if (Files.exists(defaultFile)) {
                    String content = Files.readString(defaultFile);
                    return objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
    
    /**
     * Helper method to use reflection to call private translateBenefitGroup method
     */
    private BenefitGroup translateBenefitGroupWithReflection(BenefitGroup benefitGroup, Map<String, String> translations) {
        try {
            java.lang.reflect.Method method = TranslationService.class.getDeclaredMethod(
                "translateBenefitGroup", BenefitGroup.class, Map.class);
            method.setAccessible(true);
            return (BenefitGroup) method.invoke(translationService, benefitGroup, translations);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
