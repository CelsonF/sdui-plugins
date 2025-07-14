package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class Points {

    private Integer total;
    private String lastUpdated;
    private String currency;

}
