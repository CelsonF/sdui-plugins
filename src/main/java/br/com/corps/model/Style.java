package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Style {

    private String backgroundColor;
    private String borderRadius;
    private String textColor;
    private Boolean shadow;
    private String fontFamily;
    private String fontWeight;

}
