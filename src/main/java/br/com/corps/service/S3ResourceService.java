package br.com.corps.service;

import br.com.corps.config.LanguageConfig;
import br.com.corps.model.Plugin;
import br.com.corps.model.PluginContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service to load resources from simulated S3 bucket structure
 */
@Singleton
@Slf4j
public class S3ResourceService {

    private static final String S3_BASE_PATH = "s3/exclusive-area/";
    
    private final ObjectMapper mapper;
    private final ResourceResolver resolver;
    private final ClassPathResourceLoader loader;
    private final LanguageConfig languageConfig;

    @Inject
    public S3ResourceService(ObjectMapper mapper, ResourceResolver resolver, LanguageConfig languageConfig) {
        this.mapper = mapper;
        this.resolver = resolver;
        this.loader = resolver.getLoader(ClassPathResourceLoader.class).orElseThrow();
        this.languageConfig = languageConfig;
    }

    /**
     * Load card benefits for a specific card profile and language
     * 
     * @param cardProfile the card profile (black, gold, platinum)
     * @param language the language code (pt-BR, en-US, es-ES)
     * @return list of plugins containing the benefits
     */
    public List<Plugin> loadCardBenefits(String cardProfile, String language) {
        String normalizedLanguage = languageConfig.normalizeLanguage(language);
        String resourcePath = buildResourcePath(cardProfile.toLowerCase(), normalizedLanguage);
        
        try {
            return loadPluginsFromResource(resourcePath);
        } catch (Exception e) {
            log.error("Error loading card benefits for profile {} and language {}: {}", 
                    cardProfile, normalizedLanguage, e.getMessage());
            
            // If requested language fails, try with default language
            if (!normalizedLanguage.equals(languageConfig.getDefaultLanguage())) {
                log.info("Falling back to default language: {}", languageConfig.getDefaultLanguage());
                String defaultResourcePath = buildResourcePath(cardProfile.toLowerCase(), languageConfig.getDefaultLanguage());
                try {
                    return loadPluginsFromResource(defaultResourcePath);
                } catch (Exception ex) {
                    log.error("Error loading card benefits with default language: {}", ex.getMessage());
                }
            }
        }
        
        return Collections.emptyList();
    }
    
    private List<Plugin> loadPluginsFromResource(String resourcePath) throws IOException {
        Optional<InputStream> resourceStream = loader.getResourceAsStream(resourcePath);
        
        if (resourceStream.isPresent()) {
            try (InputStream is = resourceStream.get()) {
                PluginContainer container = mapper.readValue(is, PluginContainer.class);
                return container.getChildren();
            }
        } else {
            log.warn("Resource not found: {}", resourcePath);
            return Collections.emptyList();
        }
    }
    
    private String buildResourcePath(String cardProfile, String language) {
        return S3_BASE_PATH + cardProfile + "/home/" + language + ".json";
    }
}
