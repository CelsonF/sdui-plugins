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
public class BenefitGroup {

    private String profile;
    private List<Benefit> benefits;
}
