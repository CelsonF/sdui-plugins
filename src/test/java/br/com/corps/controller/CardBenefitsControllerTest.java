package br.com.corps.controller;

import br.com.corps.config.LanguageConfig;
import br.com.corps.model.ApiResponse;
import br.com.corps.model.Plugin;
import br.com.corps.service.S3ResourceService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@MicronautTest
public class CardBenefitsControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Mock
    private S3ResourceService s3ResourceService;

    @Inject
    private LanguageConfig languageConfig;

    private CardBenefitsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CardBenefitsController(s3ResourceService, languageConfig);
    }

    @Test
    void testGetCardBenefitsWithDefaultLanguage() {
        // Setup mock response
        List<Plugin> mockPlugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        plugin.setFeature("mastercard-benefits");
        mockPlugins.add(plugin);
        
        when(s3ResourceService.loadCardBenefits("black", "pt-BR")).thenReturn(mockPlugins);
        
        // Test controller directly
        HttpResponse<ApiResponse<List<Plugin>>> response = controller.getCardBenefits("black", "pt-BR");
        
        // Assertions
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("success", response.body().getStatus());
        assertEquals("pt-BR", response.body().getLanguage());
        assertEquals(1, response.body().getData().size());
        assertEquals("mastercard-benefits", response.body().getData().get(0).getFeature());
    }
    
    @Test
    void testGetCardBenefitsWithEnglishLanguage() {
        // Setup mock response
        List<Plugin> mockPlugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        plugin.setFeature("mastercard-benefits");
        mockPlugins.add(plugin);
        
        when(s3ResourceService.loadCardBenefits("black", "en-US")).thenReturn(mockPlugins);
        
        // Test controller directly
        HttpResponse<ApiResponse<List<Plugin>>> response = controller.getCardBenefits("black", "en-US");
        
        // Assertions
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("success", response.body().getStatus());
        assertEquals("en-US", response.body().getLanguage());
        assertEquals(1, response.body().getData().size());
    }
    
    @Test
    void testGetCardBenefitsWithSpanishLanguage() {
        // Setup mock response
        List<Plugin> mockPlugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        plugin.setFeature("mastercard-benefits");
        mockPlugins.add(plugin);
        
        when(s3ResourceService.loadCardBenefits("black", "es-ES")).thenReturn(mockPlugins);
        
        // Test controller directly
        HttpResponse<ApiResponse<List<Plugin>>> response = controller.getCardBenefits("black", "es-ES");
        
        // Assertions
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("success", response.body().getStatus());
        assertEquals("es-ES", response.body().getLanguage());
    }
    
    @Test
    void testGetCardBenefitsWithUnsupportedLanguage() {
        // Setup mock response
        List<Plugin> mockPlugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        plugin.setFeature("mastercard-benefits");
        mockPlugins.add(plugin);
        
        // When unsupported language is provided, default language should be used
        when(s3ResourceService.loadCardBenefits("black", "pt-BR")).thenReturn(mockPlugins);
        
        // Test controller directly with unsupported language
        HttpResponse<ApiResponse<List<Plugin>>> response = controller.getCardBenefits("black", "fr-FR");
        
        // Assertions
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("success", response.body().getStatus());
        assertEquals("pt-BR", response.body().getLanguage());
    }
    
    @Test
    void testGetCardBenefitsNotFound() {
        // Setup mock response for empty result
        when(s3ResourceService.loadCardBenefits(anyString(), anyString())).thenReturn(new ArrayList<>());
        
        // Test controller directly
        HttpResponse<ApiResponse<List<Plugin>>> response = controller.getCardBenefits("invalid-profile", "pt-BR");
        
        // Assertions
        assertEquals(404, response.code());
        assertNotNull(response.body());
        assertEquals("error", response.body().getStatus());
        assertTrue(response.body().getMessage().contains("No benefits found"));
    }
}
