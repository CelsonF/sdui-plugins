package br.com.corps.controller;

import br.com.corps.config.LanguageConfig;
import br.com.corps.model.Plugin;
import br.com.corps.service.S3ResourceService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller for card benefits endpoints
 */
@Controller("/cards")
@Tag(name = "Card Benefits")
@Slf4j
public class CardBenefitsController {

    private final S3ResourceService s3ResourceService;
    private final LanguageConfig languageConfig;

    @Inject
    public CardBenefitsController(S3ResourceService s3ResourceService, LanguageConfig languageConfig) {
        this.s3ResourceService = s3ResourceService;
        this.languageConfig = languageConfig;
    }

    /**
     * Get benefits for a specific card profile
     *
     * @param profile the card profile (black, gold, platinum)
     * @param acceptLanguage the preferred language from Accept-Language header
     * @return the card benefits in the requested language
     */
    @Get("/{profile}/benefits")
    @Operation(summary = "Get benefits for a specific card profile")
    public HttpResponse<br.com.corps.model.ApiResponse<List<Plugin>>> getCardBenefits(
            @PathVariable @Parameter(description = "Card profile (black, gold, platinum)") String profile,
            @Header(name = "Accept-Language", defaultValue = "pt-BR") String acceptLanguage) {
        
        log.info("Requested benefits for profile: {} with language: {}", profile, acceptLanguage);
        
        String normalizedLanguage = languageConfig.normalizeLanguage(acceptLanguage);
        List<Plugin> plugins = s3ResourceService.loadCardBenefits(profile, normalizedLanguage);
        
        if (plugins.isEmpty()) {
            return HttpResponse.notFound(
                br.com.corps.model.ApiResponse.error("No benefits found for profile: " + profile)
            );
        }
        
        return HttpResponse.ok(
            br.com.corps.model.ApiResponse.success(plugins, normalizedLanguage)
        );
    }
}
