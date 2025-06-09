package io.quarkiverse.mcp.server.runtime.jsonschema.jakarta;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.jakarta.config.JakartaValidationBuildTimeConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JakartaValidationSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Inject
    JakartaValidationBuildTimeConfig config;

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        List<JakartaValidationOption> options = new ArrayList<>();
        if (config.notNullableFieldIsRequired) {
            options.add(JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED);
        }
        if (config.notNullableMethodIsRequired) {
            options.add(JakartaValidationOption.NOT_NULLABLE_METHOD_IS_REQUIRED);
        }
        if (config.includePatternProperties) {
            options.add(JakartaValidationOption.INCLUDE_PATTERN_PROPERTIES);
        }
        if (config.includeValidationMessages) {
            options.add(JakartaValidationOption.INCLUDE_VALIDATION_MESSAGES);
        }

        if (!options.isEmpty()) {
            builder.with(new JakartaValidationModule(options.toArray(new JakartaValidationOption[0])));
        } else {
            // If no specific options are enabled, still add the module with defaults
            // or decide if the module should only be added if options are present.
            // For now, let's add it with defaults if no options are true.
            // The alternative is to only add it if options.size() > 0
            builder.with(new JakartaValidationModule());
        }
    }
}
