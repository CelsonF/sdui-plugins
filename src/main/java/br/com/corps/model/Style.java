package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class Style {

    private String backgroundColor;
    private String borderRadius;
    private String textColor;
    private Boolean shadow;
    private String fontFamily;
    private String fontWeight;

}
