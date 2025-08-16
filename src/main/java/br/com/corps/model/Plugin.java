package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Introspected
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plugin {

    private String type;
    private String feature;
    private Modifier modifier;
    private Style style;
    private List<BenefitGroup> benefits;
    private Points points;
    private List<Plugin> children;

}
