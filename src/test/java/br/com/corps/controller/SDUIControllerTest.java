package br.com.corps.controller;

import br.com.corps.model.Benefit;
import br.com.corps.model.BenefitGroup;
import br.com.corps.model.Plugin;
import br.com.corps.service.SDUIPluginService;
import br.com.corps.service.TranslationService;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MicronautTest
public class SDUIControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    private SDUIPluginService pluginService;

    @Inject
    private TranslationService translationService;

    @MockBean(SDUIPluginService.class)
    SDUIPluginService pluginService() {
        return mock(SDUIPluginService.class);
    }

    @MockBean(TranslationService.class)
    TranslationService translationService() {
        return mock(TranslationService.class);
    }

    @Test
    void testGetPluginsWithQueryLanguage() {
        // Setup test data
        List<Plugin> mockPlugins = createTestPlugins();
        List<Plugin> translatedPlugins = createTranslatedPlugins("en-US");
        
        // Configure mocks
        when(pluginService.getPluginsByFeature(eq(Arrays.asList("black-card")))).thenReturn(mockPlugins);
        when(translationService.translatePlugins(eq(mockPlugins), eq("black"), eq("en-US")))
            .thenReturn(translatedPlugins);
        
        // Execute test - use direct controller test instead of HTTP client
        SDUIController controller = new SDUIController(pluginService, translationService);
        HttpResponse<List<Plugin>> response = controller.getPlugins(Arrays.asList("black-card"), java.util.Optional.of("en-US"));
        
        // Verify response
        assertNotNull(response);
        assertEquals(200, response.code());
        
        List<Plugin> result = response.body();
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify the plugin was translated
        Plugin plugin = result.get(0);
        assertEquals("benefits-section", plugin.getType());
        
        List<BenefitGroup> benefitGroups = plugin.getBenefits();
        assertNotNull(benefitGroups);
        assertEquals(1, benefitGroups.size());
        
        List<Benefit> benefits = benefitGroups.get(0).getBenefits();
        assertNotNull(benefits);
        assertEquals(1, benefits.size());
        assertTrue(benefits.get(0).getText().contains("Unlimited access to airport VIP lounges"), 
                "Expected text to contain 'Unlimited access to airport VIP lounges'");
        
        // Verify service interactions
        verify(pluginService).getPluginsByFeature(Arrays.asList("black-card"));
        verify(translationService).translatePlugins(mockPlugins, "black", "en-US");
    }

    @Test
    void testGetPluginsWithPathLanguage() {
        // Setup test data
        List<Plugin> mockPlugins = createTestPlugins();
        List<Plugin> translatedPlugins = createTranslatedPlugins("pt-BR");
        
        // Configure mocks
        when(pluginService.getPluginsByFeature(eq(Arrays.asList("black-card")))).thenReturn(mockPlugins);
        when(translationService.translatePlugins(eq(mockPlugins), eq("black"), eq("pt-BR")))
            .thenReturn(translatedPlugins);
        
        // Execute test - use direct controller test instead of HTTP client
        SDUIController controller = new SDUIController(pluginService, translationService);
        HttpResponse<List<Plugin>> response = controller.getPluginsWithPathLang("pt-BR", Arrays.asList("black-card"));
        
        // Verify response
        assertNotNull(response);
        assertEquals(200, response.code());
        
        List<Plugin> result = response.body();
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify service interactions
        verify(pluginService).getPluginsByFeature(Arrays.asList("black-card"));
        verify(translationService).translatePlugins(mockPlugins, "black", "pt-BR");
    }

    @Test
    void testGetPluginsNotFound() {
        // Configure mock to return empty list
        when(pluginService.getPluginsByFeature(anyList())).thenReturn(Collections.emptyList());
        
        // Execute test - use direct controller test instead of HTTP client
        SDUIController controller = new SDUIController(pluginService, translationService);
        HttpResponse<List<Plugin>> response = controller.getPlugins(Arrays.asList("invalid-feature"), java.util.Optional.of("en-US"));
        
        // Verify response
        assertNotNull(response);
        assertEquals(404, response.code());
    }

    @Test
    void testGetAllPlugins() {
        // Setup test data
        List<Plugin> mockPlugins = createTestPlugins();
        List<Plugin> translatedPlugins = createTranslatedPlugins("en-US");
        
        // Configure mocks
        when(pluginService.getAllPlugins()).thenReturn(mockPlugins);
        when(translationService.translatePlugins(eq(mockPlugins), eq("black"), eq("en-US")))
            .thenReturn(translatedPlugins);
        
        // Execute test - use direct controller test instead of HTTP client
        SDUIController controller = new SDUIController(pluginService, translationService);
        HttpResponse<List<Plugin>> response = controller.getAllPlugins(java.util.Optional.of("en-US"));
        
        // Verify response
        assertNotNull(response);
        assertEquals(200, response.code());
        
        List<Plugin> result = response.body();
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify service interactions
        verify(pluginService).getAllPlugins();
        verify(translationService).translatePlugins(mockPlugins, "black", "en-US");
    }

    @Test
    void testGetAllPluginsWithPathLang() {
        // Setup test data
        List<Plugin> mockPlugins = createTestPlugins();
        List<Plugin> translatedPlugins = createTranslatedPlugins("pt-BR");
        
        // Configure mocks
        when(pluginService.getAllPlugins()).thenReturn(mockPlugins);
        when(translationService.translatePlugins(eq(mockPlugins), eq("black"), eq("pt-BR")))
            .thenReturn(translatedPlugins);
        
        // Execute test - use direct controller test instead of HTTP client
        SDUIController controller = new SDUIController(pluginService, translationService);
        HttpResponse<List<Plugin>> response = controller.getAllPluginsWithPathLang("pt-BR");
        
        // Verify response
        assertNotNull(response);
        assertEquals(200, response.code());
        
        List<Plugin> result = response.body();
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify service interactions
        verify(pluginService).getAllPlugins();
        verify(translationService).translatePlugins(mockPlugins, "black", "pt-BR");
    }

    @Test
    void testGetAllPluginsNotFound() {
        // Configure mock to return empty list
        when(pluginService.getAllPlugins()).thenReturn(Collections.emptyList());
        
        // Execute test - use direct controller test instead of HTTP client
        SDUIController controller = new SDUIController(pluginService, translationService);
        HttpResponse<List<Plugin>> response = controller.getAllPlugins(java.util.Optional.of("en-US"));
        
        // Verify response
        assertNotNull(response);
        assertEquals(404, response.code());
    }

    /**
     * Create test plugins with translation keys
     */
    private List<Plugin> createTestPlugins() {
        List<Plugin> plugins = new ArrayList<>();
        
        // Create a plugin with benefit groups and benefits
        Plugin plugin = new Plugin();
        plugin.setType("benefits-section");
        plugin.setFeature("black-card");
        
        // Create benefit group
        BenefitGroup benefitGroup = new BenefitGroup();
        benefitGroup.setProfile("Black");
        
        // Create benefits with translation keys
        Benefit benefit1 = new Benefit();
        benefit1.setText("key:airport_lounge_access");
        benefit1.setIcon("https://cdn.mastercard.com/content/assets/icons/airport.svg");
        
        Benefit benefit2 = new Benefit();
        benefit2.setText("key:travel_insurance");
        benefit2.setIcon("https://cdn.mastercard.com/content/assets/icons/insurance.svg");
        
        // Add benefits to group
        benefitGroup.setBenefits(Arrays.asList(benefit1, benefit2));
        
        // Add group to plugin
        plugin.setBenefits(Arrays.asList(benefitGroup));
        
        // Add plugin to list
        plugins.add(plugin);
        
        return plugins;
    }
    
    /**
     * Create translated plugins for testing
     */
    private List<Plugin> createTranslatedPlugins(String language) {
        List<Plugin> plugins = new ArrayList<>();
        
        // Create a plugin with benefit groups and benefits
        Plugin plugin = new Plugin();
        plugin.setType("benefits-section");
        plugin.setFeature("black-card");
        
        // Create benefit group
        BenefitGroup benefitGroup = new BenefitGroup();
        benefitGroup.setProfile("Black");
        
        // Create translated benefits
        Benefit benefit = new Benefit();
        if ("en-US".equals(language)) {
            benefit.setText("Unlimited access to airport VIP lounges (LoungeKey)");
        } else {
            benefit.setText("Acesso ilimitado a salas VIP de aeroportos (LoungeKey)");
        }
        benefit.setIcon("https://cdn.mastercard.com/content/assets/icons/airport.svg");
        
        // Add benefits to group
        benefitGroup.setBenefits(Arrays.asList(benefit));
        
        // Add group to plugin
        plugin.setBenefits(Arrays.asList(benefitGroup));
        
        // Add plugin to list
        plugins.add(plugin);
        
        return plugins;
    }
}
