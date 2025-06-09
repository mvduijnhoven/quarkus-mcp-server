package io.quarkiverse.mcp.server.runtime.jsonschema.jackson;

import jakarta.enterprise.context.ApplicationScoped;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;

@ApplicationScoped
public class JacksonSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        builder.with(new JacksonModule(
                JacksonOption.FLATTENED_ENUMS_FROM_JSONVALUE,
                JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY,
                JacksonOption.INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS,
                JacksonOption.JSONIDENTITY_REFERENCE_ALWAYS_AS_ID,
                JacksonOption.ALWAYS_REF_SUBTYPES,
                JacksonOption.INLINE_TRANSFORMED_SUBTYPES,
                JacksonOption.RESPECT_JSONPROPERTY_REQUIRED));
    }
}
