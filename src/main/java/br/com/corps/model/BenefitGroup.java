package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.List;

@Introspected
@Data
public class BenefitGroup {

    private String profile;
    private List<String> benefits;
}
