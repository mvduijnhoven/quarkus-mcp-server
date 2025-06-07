package io.quarkiverse.mcp.server.runtime.jsonschema.jackson;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JacksonSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        builder.with(new JacksonModule());
    }
}
