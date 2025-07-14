package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.List;

@Introspected
@Data
public class Plugin {

    private String type;
    private String feature;
    private Modifier modifier;
    private Style style;
    private List<BenefitGroup> benefits;
    private Points points;

}
