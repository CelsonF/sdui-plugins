package br.com.corps.service;

import br.com.corps.config.LanguageConfig;
import br.com.corps.model.Benefit;
import br.com.corps.model.BenefitGroup;
import br.com.corps.model.Plugin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class S3ResourceServiceTest {

    @Inject
    private S3ResourceService s3ResourceService;
    
    @Inject
    private ObjectMapper objectMapper;
    
    @Inject
    private ResourceResolver resourceResolver;
    
    @Inject
    private LanguageConfig languageConfig;

    @Test
    void testLoadCardBenefitsWithDefaultLanguage() {
        // Test with default language (pt-BR)
        List<Plugin> plugins = s3ResourceService.loadCardBenefits("black", "pt-BR");
        
        // Verify results
        assertFalse(plugins.isEmpty(), "Should return non-empty list of plugins");
        assertEquals("mastercard-benefits", plugins.get(0).getFeature());
        assertEquals("Benefícios Mastercard Black", plugins.get(0).getTitle());
        
        // Verify benefit groups and benefits
        List<BenefitGroup> benefitGroups = plugins.get(0).getBenefitGroups();
        assertFalse(benefitGroups.isEmpty(), "Should have benefit groups");
        
        // Check first benefit group
        BenefitGroup firstGroup = benefitGroups.get(0);
        assertEquals("Viagem", firstGroup.getTitle());
        
        // Check benefits have text and icon
        List<Benefit> benefits = firstGroup.getBenefits();
        assertFalse(benefits.isEmpty(), "Should have benefits");
        assertNotNull(benefits.get(0).getText(), "Benefit should have text");
        assertNotNull(benefits.get(0).getIcon(), "Benefit should have icon URL");
    }
    
    @ParameterizedTest
    @CsvSource({
        "black,en-US,Mastercard Black Benefits,Travel",
        "black,es-ES,Beneficios Mastercard Black,Viaje",
        "gold,en-US,Mastercard Gold Benefits,Travel",
        "gold,es-ES,Beneficios Mastercard Gold,Viaje",
        "platinum,en-US,Mastercard Platinum Benefits,Travel",
        "platinum,es-ES,Beneficios Mastercard Platinum,Viaje"
    })
    void testLoadCardBenefitsWithDifferentLanguages(String cardProfile, String language, 
                                                   String expectedTitle, String expectedGroupTitle) {
        // Test with specified language
        List<Plugin> plugins = s3ResourceService.loadCardBenefits(cardProfile, language);
        
        // Verify results
        assertFalse(plugins.isEmpty(), "Should return non-empty list of plugins");
        assertEquals("mastercard-benefits", plugins.get(0).getFeature());
        assertEquals(expectedTitle, plugins.get(0).getTitle());
        
        // Verify first benefit group title
        List<BenefitGroup> benefitGroups = plugins.get(0).getBenefitGroups();
        assertFalse(benefitGroups.isEmpty(), "Should have benefit groups");
        assertEquals(expectedGroupTitle, benefitGroups.get(0).getTitle());
    }
    
    @Test
    void testLoadCardBenefitsWithUnsupportedLanguage() {
        // Test with unsupported language (should fall back to default)
        List<Plugin> plugins = s3ResourceService.loadCardBenefits("black", "fr-FR");
        
        // Verify results (should be same as default language)
        assertFalse(plugins.isEmpty(), "Should return non-empty list of plugins");
        assertEquals("mastercard-benefits", plugins.get(0).getFeature());
        assertEquals("Benefícios Mastercard Black", plugins.get(0).getTitle());
    }
    
    @Test
    void testLoadCardBenefitsWithInvalidProfile() {
        // Test with invalid card profile
        List<Plugin> plugins = s3ResourceService.loadCardBenefits("invalid-profile", "pt-BR");
        
        // Verify results
        assertTrue(plugins.isEmpty(), "Should return empty list for invalid profile");
    }
}
