package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.List;

@Introspected
@Data
public class PluginContainer {

    private String type;
    private String feature;
    private List<Plugin> children;

}
