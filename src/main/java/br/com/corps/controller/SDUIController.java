package br.com.corps.controller;

import br.com.corps.model.Plugin;
import br.com.corps.service.SDUIPluginService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller("/sdui")
@Tag(name = "SDUI")
@RequiredArgsConstructor
public class SDUIController {

    private final SDUIPluginService service;

    @Get("/plugins")
    @Operation(summary = "List of Plugins per feature")
    @ApiResponse(responseCode = "200", description = "Plugins found")
    @ApiResponse(responseCode = "400", description = "None plugins found")
    public HttpResponse<List<Plugin>> getPlugins(@QueryValue List<String> feature) {
        List<Plugin> plugins = service.getPluginsByFeature(feature);
        return plugins.isEmpty() ? HttpResponse.notFound() : HttpResponse.ok(plugins);
    }

}
