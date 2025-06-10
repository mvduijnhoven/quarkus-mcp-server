package io.quarkiverse.mcp.server.runtime.jsonschema.jackson.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigRoot(name = "mcp.server.jsonschema.jackson", phase = ConfigPhase.BUILD_TIME)
public class JacksonBuildTimeConfig {

    /**
     * Option: RESPECT_JSONPROPERTY_REQUIRED
     * Enable if JsonProperty.required() should be respected.
     */
    @ConfigItem(defaultValue = "true")
    public boolean respectJsonpropertyRequired;

    /**
     * Option: FLATTENED_ENUMS_FROM_JSONVALUE
     * Enable if Enums that are annotated with JsonValue should be represented as a flat list of their possible scalar values.
     */
    @ConfigItem(defaultValue = "true")
    public boolean flattenedEnumsFromJsonvalue;

    /**
     * Option: FLATTENED_ENUMS_FROM_JSONPROPERTY
     * Enable if Enums that are annotated with JsonProperty should be represented as a flat list of their possible scalar values.
     */
    @ConfigItem(defaultValue = "true")
    public boolean flattenedEnumsFromJsonproperty;

    /**
     * Option: INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS
     * Enable if only methods that are annotated with JsonProperty should be included.
     */
    @ConfigItem(defaultValue = "true")
    public boolean includeOnlyJsonpropertyAnnotatedMethods;

    /**
     * Option: JSONIDENTITY_REFERENCE_ALWAYS_AS_ID
     * Enable if JsonIdentityReference(alwaysAsId = true) should be respected.
     */
    @ConfigItem(defaultValue = "true")
    public boolean jsonidentityReferenceAlwaysAsId;

    /**
     * Option: ALWAYS_REF_SUBTYPES
     * Enable if subtypes should always be represented as $ref tags.
     */
    @ConfigItem(defaultValue = "true")
    public boolean alwaysRefSubtypes;

    /**
     * Option: INLINE_TRANSFORMED_SUBTYPES
     * Enable if subtypes that are transformed during serialization should be inlined.
     */
    @ConfigItem(defaultValue = "true")
    public boolean inlineTransformedSubtypes;

    /**
     * Option: RESPECT_JSONPROPERTY_ORDER
     * Enable if JsonProperty.order() should be respected.
     */
    @ConfigItem(defaultValue = "true")
    public boolean respectJsonpropertyOrder;

    /**
     * Option: IGNORE_PROPERTY_NAMING_STRATEGY
     * Enable if any property naming strategy should be ignored.
     */
    @ConfigItem(defaultValue = "false")
    public boolean ignorePropertyNamingStrategy;

    /**
     * Option: SKIP_SUBTYPE_LOOKUP
     * Enable if subtype lookup should be skipped.
     */
    @ConfigItem(defaultValue = "false")
    public boolean skipSubtypeLookup;

    /**
     * Option: IGNORE_TYPE_INFO_TRANSFORM
     * Enable if type info transform should be ignored.
     */
    @ConfigItem(defaultValue = "false")
    public boolean ignoreTypeInfoTransform;
}
