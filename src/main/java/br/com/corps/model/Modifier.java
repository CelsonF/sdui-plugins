package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class Modifier {

    private String padding;
    private String margin;
    private String alignment;
}
