package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Benefit {
    private String text;
    private String icon;
}
