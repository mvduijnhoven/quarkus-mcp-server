package io.quarkiverse.mcp.server.runtime.jsonschema.javaxvalidation;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JavaxValidationSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        builder.with(new JavaxValidationModule());
    }
}
