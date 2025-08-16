package br.com.corps.service;

import br.com.corps.Configuration;
import br.com.corps.model.Plugin;
import br.com.corps.model.PluginContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class SDUIPluginService {

    private final ObjectMapper mapper;
    private final ResourceResolver resolver;
    private final Configuration config;

    private List<Plugin> plugins;

    @PostConstruct
    @SneakyThrows
    void init() {
        String jsonPath = config.getJsonPath();
        log.info("Loading plugins from: {}", jsonPath);

        ClassPathResourceLoader loader = resolver.getLoader(ClassPathResourceLoader.class).orElseThrow();
        try (InputStream in = loader.getResourceAsStream(jsonPath).orElseThrow()) {
            PluginContainer container = mapper.readValue(in, PluginContainer.class);
            this.plugins = container.getChildren();
            log.info("Loaded {} plugins", this.plugins.size());
        } catch (Exception e) {
            log.error("Failed to load plugins: {}", e.getMessage(), e);
            this.plugins = Collections.emptyList();
        }
    }

    /**
     * Get plugins filtered by feature
     * 
     * @param features List of features to filter by
     * @return List of plugins matching the features
     */
    public List<Plugin> getPluginsByFeature(List<String> features) {
        if (plugins == null || plugins.isEmpty()) {
            return Collections.emptyList();
        }

        return plugins.stream()
                .filter(plugin -> features.contains(plugin.getFeature()))
                .collect(Collectors.toList());
    }

    /**
     * Get all available plugins
     * 
     * @return List of all plugins
     */
    public List<Plugin> getAllPlugins() {
        if (plugins == null || plugins.isEmpty()) {
            return Collections.emptyList();
        }
        return plugins;
    }
}
