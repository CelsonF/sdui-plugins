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
        
        // Verify benefit groups and benefits
        List<BenefitGroup> benefitGroups = plugins.get(0).getBenefits();
        assertFalse(benefitGroups.isEmpty(), "Should have benefit groups");
        
        // Check first benefit group
        BenefitGroup firstGroup = benefitGroups.get(0);
        assertEquals("Black", firstGroup.getProfile());
        
        // Check benefits in the group
        List<Benefit> benefits = firstGroup.getBenefits();
        assertFalse(benefits.isEmpty(), "Should have benefits in the group");
    }

    @ParameterizedTest
    @CsvSource({
        "black,en-US",
        "black,pt-BR",
        "platinum,en-US",
        "platinum,pt-BR"
    })
    void testLoadCardBenefitsWithDifferentLanguages(String cardType, String language) {
        // Test with different languages
        List<Plugin> plugins = s3ResourceService.loadCardBenefits(cardType, language);
        
        // Verify results
        assertFalse(plugins.isEmpty(), "Should return non-empty list of plugins");
        
        // Verify benefit groups and benefits
        List<BenefitGroup> benefitGroups = plugins.get(0).getBenefits();
        assertFalse(benefitGroups.isEmpty(), "Should have benefit groups");
        
        // Check benefits in the first group
        BenefitGroup firstGroup = benefitGroups.get(0);
        List<Benefit> benefits = firstGroup.getBenefits();
        assertFalse(benefits.isEmpty(), "Should have benefits in the group");
    }

    @Test
    void testLoadCardBenefitsWithInvalidCard() {
        // Test with invalid card type
        List<Plugin> plugins = s3ResourceService.loadCardBenefits("invalid-card", "pt-BR");
        
        // Should return empty list for invalid card
        assertTrue(plugins.isEmpty(), "Should return empty list for invalid card");
    }
}
