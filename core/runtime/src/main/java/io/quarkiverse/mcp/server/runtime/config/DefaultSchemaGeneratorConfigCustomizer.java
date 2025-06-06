package io.quarkiverse.mcp.server.runtime.config;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        // Attempt to load Jackson module
        try {
            Class.forName("com.github.victools.jsonschema.module.jackson.JacksonModule");
            builder.with(new JacksonModule());
            Log.debug("JacksonModule loaded for JSON Schema generation.");
        } catch (ClassNotFoundException e) {
            Log.debug("JacksonModule not found on classpath. Skipping.");
        }

        // Attempt to load Jakarta Validation module
        try {
            Class.forName("com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule");
            builder.with(new JakartaValidationModule());
            Log.debug("JakartaValidationModule loaded for JSON Schema generation.");
        } catch (ClassNotFoundException e) {
            Log.debug("JakartaValidationModule not found on classpath. Skipping.");
        }
    }
}
