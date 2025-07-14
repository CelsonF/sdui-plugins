package br.com.corps;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("sdui")
public class Configuration {
    private String jsonPath;
}
