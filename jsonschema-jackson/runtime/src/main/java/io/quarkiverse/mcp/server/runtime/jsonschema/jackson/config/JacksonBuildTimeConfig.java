package io.quarkiverse.mcp.server.runtime.jsonschema.jackson.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.mcp.server.jsonschema.jackson")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface JacksonBuildTimeConfig {

    /**
     * Option: RESPECT_JSONPROPERTY_REQUIRED
     * Enable if JsonProperty.required() should be respected.
     */
    @WithDefault("true")
    boolean respectJsonpropertyRequired();

    /**
     * Option: FLATTENED_ENUMS_FROM_JSONVALUE
     * Enable if Enums that are annotated with JsonValue should be represented as a flat list of their possible scalar values.
     */
    @WithDefault("true")
    boolean flattenedEnumsFromJsonvalue();

    /**
     * Option: FLATTENED_ENUMS_FROM_JSONPROPERTY
     * Enable if Enums that are annotated with JsonProperty should be represented as a flat list of their possible scalar
     * values.
     */
    @WithDefault("true")
    boolean flattenedEnumsFromJsonproperty();

    /**
     * Option: INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS
     * Enable if only methods that are annotated with JsonProperty should be included.
     */
    @WithDefault("true")
    boolean includeOnlyJsonpropertyAnnotatedMethods();

    /**
     * Option: JSONIDENTITY_REFERENCE_ALWAYS_AS_ID
     * Enable if JsonIdentityReference(alwaysAsId = true) should be respected.
     */
    @WithDefault("true")
    boolean jsonidentityReferenceAlwaysAsId();

    /**
     * Option: ALWAYS_REF_SUBTYPES
     * Enable if subtypes should always be represented as $ref tags.
     */
    @WithDefault("true")
    boolean alwaysRefSubtypes();

    /**
     * Option: INLINE_TRANSFORMED_SUBTYPES
     * Enable if subtypes that are transformed during serialization should be inlined.
     */
    @WithDefault("true")
    boolean inlineTransformedSubtypes();

    /**
     * Option: RESPECT_JSONPROPERTY_ORDER
     * Enable if JsonProperty.order() should be respected.
     */
    @WithDefault("true")
    boolean respectJsonpropertyOrder();

    /**
     * Option: IGNORE_PROPERTY_NAMING_STRATEGY
     * Enable if any property naming strategy should be ignored.
     */
    @WithDefault("false")
    boolean ignorePropertyNamingStrategy();

    /**
     * Option: SKIP_SUBTYPE_LOOKUP
     * Enable if subtype lookup should be skipped.
     */
    @WithDefault("false")
    boolean skipSubtypeLookup();

    /**
     * Option: IGNORE_TYPE_INFO_TRANSFORM
     * Enable if type info transform should be ignored.
     */
    @WithDefault("false")
    boolean ignoreTypeInfoTransform();
}
