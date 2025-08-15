package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Benefit {
    private String text;
    private String icon;
}
