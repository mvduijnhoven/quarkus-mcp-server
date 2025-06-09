package io.quarkiverse.mcp.server.runtime.jsonschema.swagger15;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.swagger15.SwaggerModule;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.swagger15.config.Swagger15BuildTimeConfig; // Added
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject; // Added

@ApplicationScoped
public class Swagger15SchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Inject // Added
    Swagger15BuildTimeConfig config; // Added

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        if (config.enable) { // Modified
            builder.with(new SwaggerModule());
        }
    }
}
