package io.quarkiverse.mcp.server.runtime.jsonschema.swagger2;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.swagger2.Swagger2Module;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.swagger2.config.Swagger2BuildTimeConfig; // Added
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject; // Added

@ApplicationScoped
public class Swagger2SchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Inject // Added
    Swagger2BuildTimeConfig config; // Added

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        if (config.enable) { // Modified
            builder.with(new Swagger2Module());
        }
    }
}
