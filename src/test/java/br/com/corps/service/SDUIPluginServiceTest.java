package br.com.corps.service;

import br.com.corps.Configuration;
import br.com.corps.model.Plugin;
import br.com.corps.model.PluginContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@MicronautTest
public class SDUIPluginServiceTest {

    @Inject
    private ObjectMapper objectMapper;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private ClassPathResourceLoader classPathResourceLoader;

    @Mock
    private Configuration config;

    private SDUIPluginService pluginService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Configure mocks
        when(config.getJsonPath()).thenReturn("s3/plugins.json");
        when(resourceResolver.getLoader(ClassPathResourceLoader.class)).thenReturn(Optional.of(classPathResourceLoader));
        
        // Create test JSON content
        String jsonContent = createTestPluginsJson();
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());
        when(classPathResourceLoader.getResourceAsStream(anyString())).thenReturn(Optional.of(inputStream));
        
        // Initialize service
        pluginService = new SDUIPluginService(objectMapper, resourceResolver, config);
        pluginService.init();
    }

    @Test
    void testGetPluginsByFeature() {
        // Test getting plugins by feature
        List<Plugin> plugins = pluginService.getPluginsByFeature(Collections.singletonList("black-card"));
        
        // Verify results
        assertNotNull(plugins);
        assertEquals(1, plugins.size());
        assertEquals("black-card", plugins.get(0).getFeature());
    }
    
    @Test
    void testGetPluginsByMultipleFeatures() {
        // Test getting plugins by multiple features
        List<Plugin> plugins = pluginService.getPluginsByFeature(Arrays.asList("black-card", "gold-card"));
        
        // Verify results
        assertNotNull(plugins);
        assertEquals(2, plugins.size());
        
        // Verify features are correct
        List<String> features = Arrays.asList(
            plugins.get(0).getFeature(),
            plugins.get(1).getFeature()
        );
        assertTrue(features.contains("black-card"));
        assertTrue(features.contains("gold-card"));
    }
    
    @Test
    void testGetPluginsByNonExistentFeature() {
        // Test getting plugins by non-existent feature
        List<Plugin> plugins = pluginService.getPluginsByFeature(Collections.singletonList("non-existent"));
        
        // Verify results
        assertNotNull(plugins);
        assertTrue(plugins.isEmpty());
    }
    
    @Test
    void testGetPluginsByEmptyFeatureList() {
        // Test getting plugins with empty feature list
        List<Plugin> plugins = pluginService.getPluginsByFeature(Collections.emptyList());
        
        // Verify results
        assertNotNull(plugins);
        assertTrue(plugins.isEmpty());
    }
    
    @Test
    void testGetAllPlugins() {
        // Test getting all plugins
        List<Plugin> plugins = pluginService.getAllPlugins();
        
        // Verify results
        assertNotNull(plugins);
        assertEquals(3, plugins.size());
        
        // Verify features are correct
        List<String> features = Arrays.asList(
            plugins.get(0).getFeature(),
            plugins.get(1).getFeature(),
            plugins.get(2).getFeature()
        );
        assertTrue(features.contains("black-card"));
        assertTrue(features.contains("gold-card"));
        assertTrue(features.contains("platinum-card"));
    }
    
    @Test
    void testInitializationFailure() throws Exception {
        // Setup for failure scenario
        when(classPathResourceLoader.getResourceAsStream(anyString())).thenReturn(Optional.empty());
        
        // Create new service instance
        SDUIPluginService failingService = new SDUIPluginService(objectMapper, resourceResolver, config);
        failingService.init();
        
        // Verify that the service handles the failure gracefully
        List<Plugin> plugins = failingService.getAllPlugins();
        assertNotNull(plugins);
        assertTrue(plugins.isEmpty());
    }
    
    /**
     * Creates test JSON content for plugins
     */
    private String createTestPluginsJson() {
        return "{\n" +
               "  \"children\": [\n" +
               "    {\n" +
               "      \"type\": \"card\",\n" +
               "      \"feature\": \"black-card\",\n" +
               "      \"benefits\": [\n" +
               "        {\n" +
               "          \"text\": \"key:unlimited_access_vip_lounges\",\n" +
               "          \"icon\": \"https://cdn.mastercard.com/content/assets/icons/airport.svg\"\n" +
               "        }\n" +
               "      ]\n" +
               "    },\n" +
               "    {\n" +
               "      \"type\": \"card\",\n" +
               "      \"feature\": \"gold-card\",\n" +
               "      \"benefits\": [\n" +
               "        {\n" +
               "          \"text\": \"key:travel_insurance\",\n" +
               "          \"icon\": \"https://cdn.mastercard.com/content/assets/icons/insurance.svg\"\n" +
               "        }\n" +
               "      ]\n" +
               "    },\n" +
               "    {\n" +
               "      \"type\": \"card\",\n" +
               "      \"feature\": \"platinum-card\",\n" +
               "      \"benefits\": [\n" +
               "        {\n" +
               "          \"text\": \"key:concierge_service\",\n" +
               "          \"icon\": \"https://cdn.mastercard.com/content/assets/icons/concierge.svg\"\n" +
               "        }\n" +
               "      ]\n" +
               "    }\n" +
               "  ]\n" +
               "}";
    }
}
