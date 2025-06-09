package io.quarkiverse.mcp.server.runtime.jsonschema.jakartavalidation;

import jakarta.enterprise.context.ApplicationScoped;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;

import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;

@ApplicationScoped
public class JakartaValidationSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        builder.with(new JakartaValidationModule(
                JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
                JakartaValidationOption.NOT_NULLABLE_METHOD_IS_REQUIRED,
                JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS));
    }
}
