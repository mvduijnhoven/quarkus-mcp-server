package io.quarkiverse.mcp.server.test.config;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomTestSchemaModule implements SchemaGeneratorConfigCustomizer {

    public static boolean invoked = false;

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        invoked = true;
        // Example customization: enable a specific Jakarta Validation option
        builder.forJakartaValidation().setOption(JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED);
        System.out.println("CustomTestSchemaModule was invoked");
    }
}
