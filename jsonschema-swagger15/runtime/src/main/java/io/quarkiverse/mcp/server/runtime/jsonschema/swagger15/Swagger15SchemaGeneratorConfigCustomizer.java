package io.quarkiverse.mcp.server.runtime.jsonschema.swagger15;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.swagger15.SwaggerModule;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Swagger15SchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        builder.with(new SwaggerModule());
    }
}
