package br.com.corps.controller;

import br.com.corps.model.Plugin;
import br.com.corps.service.SDUIPluginService;
import br.com.corps.service.TranslationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Controller("/sdui")
@Tag(name = "SDUI", description = "Server-Driven UI API for multilingual plugin content")
@RequiredArgsConstructor
public class SDUIController {

    private final SDUIPluginService pluginService;
    private final TranslationService translationService;

    @Get("/plugins")
    @Operation(
        summary = "Get plugins by feature with query parameter language selection",
        description = "Returns a list of plugins filtered by feature with optional language translation via query parameter"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Plugins found and returned successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plugin.class))
    )
    @ApiResponse(responseCode = "404", description = "No plugins found for the specified features")
    public HttpResponse<List<Plugin>> getPlugins(
            @Parameter(description = "List of feature names to filter plugins by") 
            @QueryValue List<String> feature,
            
            @Parameter(description = "Language code (e.g., pt-BR, en-US, es-ES) for content translation")
            @QueryValue(defaultValue = "pt-BR") Optional<String> lang) {
        
        List<Plugin> plugins = pluginService.getPluginsByFeature(feature);
        if (plugins.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        // Apply translations if language is specified
        if (lang.isPresent()) {
            plugins = translationService.translatePlugins(plugins, "black", lang.get());
        }
        
        return HttpResponse.ok(plugins);
    }

    @Get("/{lang}/plugins")
    @Operation(
        summary = "Get plugins by feature with path parameter language selection",
        description = "Returns a list of plugins filtered by feature with language translation via path parameter"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Plugins found and returned successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plugin.class))
    )
    @ApiResponse(responseCode = "404", description = "No plugins found for the specified features")
    public HttpResponse<List<Plugin>> getPluginsWithPathLang(
            @PathVariable 
            @Parameter(description = "Language code (e.g., pt-BR, en-US, es-ES) for content translation") 
            String lang,
            
            @Parameter(description = "List of feature names to filter plugins by") 
            @QueryValue List<String> feature) {
        
        List<Plugin> plugins = pluginService.getPluginsByFeature(feature);
        if (plugins.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        // Apply translations based on the path language parameter
        plugins = translationService.translatePlugins(plugins, "black", lang);
        
        return HttpResponse.ok(plugins);
    }

    @Get("/plugins/all")
    @Operation(
        summary = "Get all available plugins with query parameter language selection",
        description = "Returns a list of all available plugins with optional language translation via query parameter"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Plugins found and returned successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plugin.class))
    )
    @ApiResponse(responseCode = "404", description = "No plugins found")
    public HttpResponse<List<Plugin>> getAllPlugins(
            @Parameter(description = "Language code (e.g., pt-BR, en-US, es-ES) for content translation")
            @QueryValue(defaultValue = "pt-BR") Optional<String> lang) {
        
        List<Plugin> plugins = pluginService.getAllPlugins();
        if (plugins.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        // Apply translations if language is specified
        if (lang.isPresent()) {
            plugins = translationService.translatePlugins(plugins, "black", lang.get());
        }
        
        return HttpResponse.ok(plugins);
    }

    @Get("/{lang}/plugins/all")
    @Operation(
        summary = "Get all available plugins with path parameter language selection",
        description = "Returns a list of all available plugins with language translation via path parameter"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Plugins found and returned successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plugin.class))
    )
    @ApiResponse(responseCode = "404", description = "No plugins found")
    public HttpResponse<List<Plugin>> getAllPluginsWithPathLang(
            @PathVariable 
            @Parameter(description = "Language code (e.g., pt-BR, en-US, es-ES) for content translation") 
            String lang) {
        
        List<Plugin> plugins = pluginService.getAllPlugins();
        if (plugins.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        // Apply translations based on the path language parameter
        plugins = translationService.translatePlugins(plugins, "black", lang);
        
        return HttpResponse.ok(plugins);
    }
}
