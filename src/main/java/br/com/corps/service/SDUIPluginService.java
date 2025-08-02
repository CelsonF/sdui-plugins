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

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
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

        ClassPathResourceLoader loader = resolver.getLoader(ClassPathResourceLoader.class).orElseThrow();
        try (InputStream in = loader.getResourceAsStream(jsonPath).orElseThrow()) {
            PluginContainer container = mapper.readValue(in, PluginContainer.class);
            this.plugins = container.getChildren();
        }
    }

    public List<Plugin> getPluginsByFeature(List<String> feature) {
        return plugins.stream().filter(p -> feature.contains(p.getFeature()))
                .collect(Collectors.toList());
    }

}
