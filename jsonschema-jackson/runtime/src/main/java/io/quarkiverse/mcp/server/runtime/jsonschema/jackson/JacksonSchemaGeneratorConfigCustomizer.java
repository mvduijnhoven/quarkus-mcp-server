package io.quarkiverse.mcp.server.runtime.jsonschema.jackson;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.jackson.config.JacksonBuildTimeConfig; // Added import
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject; // Added import
import java.util.ArrayList; // Added import
import java.util.List; // Added import

@ApplicationScoped
public class JacksonSchemaGeneratorConfigCustomizer implements SchemaGeneratorConfigCustomizer {

    @Inject // Added inject annotation
    JacksonBuildTimeConfig config;

    @Override
    public void customize(SchemaGeneratorConfigBuilder builder) {
        List<JacksonOption> options = new ArrayList<>();
        if (config.flattenedEnumsFromJsonvalue) {
            options.add(JacksonOption.FLATTENED_ENUMS_FROM_JSONVALUE);
        }
        if (config.flattenedEnumsFromJsonproperty) { // Corrected typo here from plan (Jonproperty -> Jsonproperty)
            options.add(JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY);
        }
        if (config.includeOnlyJsonpropertyAnnotatedMethods) {
            options.add(JacksonOption.INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS);
        }
        if (config.jsonidentityReferenceAlwaysAsId) {
            options.add(JacksonOption.JSONIDENTITY_REFERENCE_ALWAYS_AS_ID);
        }
        if (config.alwaysRefSubtypes) {
            options.add(JacksonOption.ALWAYS_REF_SUBTYPES);
        }
        if (config.inlineTransformedSubtypes) {
            options.add(JacksonOption.INLINE_TRANSFORMED_SUBTYPES);
        }
        if (config.respectJsonpropertyRequired) {
            options.add(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
        }
        if (config.respectJsonpropertyOrder) {
            options.add(JacksonOption.RESPECT_JSONPROPERTY_ORDER);
        }
        if (config.ignorePropertyNamingStrategy) {
            options.add(JacksonOption.IGNORE_PROPERTY_NAMING_STRATEGY);
        }
        if (config.skipSubtypeLookup) {
            options.add(JacksonOption.SKIP_SUBTYPE_LOOKUP);
        }
        if (config.ignoreTypeInfoTransform) {
            options.add(JacksonOption.IGNORE_TYPE_INFO_TRANSFORM);
        }
        builder.with(new JacksonModule(options.toArray(new JacksonOption[0])));
    }
}
