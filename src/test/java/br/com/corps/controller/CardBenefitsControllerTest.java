package br.com.corps.controller;

import br.com.corps.model.ApiResponse;
import br.com.corps.model.Plugin;
import br.com.corps.service.S3ResourceService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@MicronautTest
public class CardBenefitsControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    private S3ResourceService s3ResourceService;

    @Inject
    private CardBenefitsController controller;

    @MockBean(S3ResourceService.class)
    S3ResourceService s3ResourceService() {
        return mock(S3ResourceService.class);
    }

    @BeforeEach
    void setUp() {
        // Setup mock data for default language
        List<Plugin> mockPlugins = new ArrayList<>();
        Plugin plugin = new Plugin();
        plugin.setFeature("mastercard-benefits");
        mockPlugins.add(plugin);
        
        // Configure mock for default language
        when(s3ResourceService.loadCardBenefits("black", "pt-BR")).thenReturn(mockPlugins);
        
        // Configure mock for English language
        when(s3ResourceService.loadCardBenefits("black", "en-US")).thenReturn(mockPlugins);
    }

    @Test
    void testGetCardBenefitsWithDefaultLanguage() {
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
        // Test controller directly
        HttpResponse<ApiResponse<List<Plugin>>> response = controller.getCardBenefits("black", "en-US");
        
        // Assertions
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("success", response.body().getStatus());
        assertEquals("en-US", response.body().getLanguage());
        assertEquals(1, response.body().getData().size());
        assertEquals("mastercard-benefits", response.body().getData().get(0).getFeature());
    }
}
