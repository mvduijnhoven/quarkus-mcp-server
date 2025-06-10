package io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationOption;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation.config.JavaxValidationBuildTimeConfig;
import jakarta.enterprise.context.ApplicationScoped; // Assuming Quarkus uses jakarta.enterprise
import jakarta.inject.Inject; // Assuming Quarkus uses jakarta.inject
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JavaxValidationSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Inject
    JavaxValidationBuildTimeConfig config;

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        List<JavaxValidationOption> options = new ArrayList<>();
        if (config.notNullableFieldIsRequired) {
            options.add(JavaxValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED);
        }
        if (config.notNullableMethodIsRequired) {
            options.add(JavaxValidationOption.NOT_NULLABLE_METHOD_IS_REQUIRED);
        }
        if (config.includePatternProperties) {
            options.add(JavaxValidationOption.INCLUDE_PATTERN_PROPERTIES);
        }

        if (!options.isEmpty()) {
            builder.with(new JavaxValidationModule(options.toArray(new JavaxValidationOption[0])));
        } else {
            builder.with(new JavaxValidationModule());
        }
    }
}
