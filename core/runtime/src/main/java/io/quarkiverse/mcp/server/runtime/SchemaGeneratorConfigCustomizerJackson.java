package io.quarkiverse.mcp.server.runtime;

import java.util.ArrayList;

import jakarta.enterprise.context.ApplicationScoped;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import io.quarkiverse.mcp.server.runtime.config.McpServerSchemaGeneratorJacksonRuntimeConfig;

@ApplicationScoped
public class SchemaGeneratorConfigCustomizerJackson implements SchemaGeneratorConfigCustomizer {

    private final McpServerSchemaGeneratorJacksonRuntimeConfig config;

    public SchemaGeneratorConfigCustomizerJackson(McpServerSchemaGeneratorJacksonRuntimeConfig config) {
        this.config = config;
    }

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        if (config.enabled()) {
            var configuredOptions = getConfiguredOptions();
            var jacksonModule = new JacksonModule(configuredOptions);
            builder.with(jacksonModule);
        }
    }

    private JacksonOption[] getConfiguredOptions() {
        var configuredOptions = new ArrayList<JacksonOption>();
        if (config.respectJsonpropertyRequired()) {
            configuredOptions.add(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
        }
        return configuredOptions.toArray(JacksonOption[]::new);
    }
}
