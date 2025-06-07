package io.quarkiverse.mcp.server.runtime.jsonschema.swagger2;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.swagger2.Swagger2Module;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Swagger2SchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        builder.with(new Swagger2Module());
    }
}
