package io.quarkiverse.mcp.server.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.mcp.server.schemagenerator.jackson")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface McpServerSchemaGeneratorJacksonRuntimeConfig {

    /**
     * Whether to use the SchemaGenerator's Jackson Module.
     * If this module is not present as a dependency, this module won't be enabled.
     */
    @WithDefault("true")
    boolean enabled();

    /**
     * Corresponds to {@code JacksonOption.RESPECT_JSONPROPERTY_ORDER}.
     * <p>
     * If enabled, the order of properties in the generated schema will respect
     * the order defined in a {@code @JsonPropertyOrder} annotation on a given type.
     * </p>
     * <p>
     * <b>Default: {@code true}</b>. This is a sensible default as it honors the
     * developer's explicit intent to define a property sequence.
     * </p>
     */
    @WithDefault("true")
    boolean respectJsonPropertyOrder();

    /**
     * Corresponds to {@code JacksonOption.RESPECT_JSONPROPERTY_REQUIRED}.
     * <p>
     * If enabled, a property will be marked as "required" in the schema if its
     * corresponding field or method is annotated with {@code @JsonProperty(required = true)}.
     * </p>
     * <p>
     * <b>Default: {@code true}</b>. This is a critical default for producing accurate
     * schemas that reflect the contract defined by the Jackson annotations.
     * </p>
     */
    @WithDefault("true")
    boolean respectJsonPropertyRequired();

    /**
     * Corresponds to {@code JacksonOption.FLATTENED_ENUMS_FROM_JSONVALUE}.
     * <p>
     * If enabled, the schema for an enum will be a simple array of values (e.g., strings)
     * derived from the method annotated with {@code @JsonValue}.
     * </p>
     * <p>
     * <b>Default: {@code true}</b>. Using {@code @JsonValue} is the standard Jackson
     * pattern for customizing enum serialization. The schema should match this
     * representation by default.
     * </p>
     */
    @WithDefault("true")
    boolean flattenedEnumsFromJsonValue();

    /**
     * Corresponds to {@code JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY}.
     * <p>
     * If enabled, the schema for an enum will be derived from {@code @JsonProperty}
     * annotations on the enum's constants.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. This is a less common pattern than {@code @JsonValue}.
     * Enabling it by default could lead to unexpected behavior if {@code @JsonProperty}
     * is used for other reasons.
     * </p>
     */
    @WithDefault("false")
    boolean flattenedEnumsFromJsonProperty();

    /**
     * Corresponds to {@code JacksonOption.INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS}.
     * <p>
     * If enabled, only methods explicitly annotated with {@code @JsonProperty} will be
     * included in the schema.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. This is a highly restrictive option. Standard
     * Jackson behavior is to include all public getters, and the schema generator
     * should align with that expectation by default.
     * </p>
     */
    @WithDefault("false")
    boolean includeOnlyJsonPropertyAnnotatedMethods();

    /**
     * Corresponds to {@code JacksonOption.IGNORE_PROPERTY_NAMING_STRATEGY}.
     * <p>
     * If enabled, any configured {@code PropertyNamingStrategy} (e.g., snake_case)
     * will be ignored, and field names from the Java class will be used directly.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. A naming strategy is often a project-wide
     * setting. The schema must respect it by default to match the actual JSON output.
     * </p>
     */
    @WithDefault("false")
    boolean ignorePropertyNamingStrategy();

    /**
     * Corresponds to {@code JacksonOption.ALWAYS_REF_SUBTYPES}.
     * <p>
     * If enabled, subtypes in a polymorphic hierarchy will always be represented
     * by a {@code $ref} to a definition, rather than being inlined.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. The default library behavior of intelligently
     * deciding when to inline vs. when to use a {@code $ref} provides a good
     * balance between readability and avoiding repetition.
     * </p>
     */
    @WithDefault("false")
    boolean alwaysRefSubtypes();

    /**
     * Corresponds to {@code JacksonOption.INLINE_TRANSFORMED_SUBTYPES}.
     * <p>
     * A specialized option for handling subtypes that have been transformed.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. This is not a common use case and should
     * only be enabled deliberately when such transformations are in use.
     * </p>
     */
    @WithDefault("false")
    boolean inlineTransformedSubtypes();

    /**
     * Corresponds to {@code JacksonOption.SKIP_SUBTYPE_LOOKUP}.
     * <p>
     * If enabled, subtype resolution via {@code @JsonSubTypes} will be disabled entirely.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. Disabling subtype lookup by default would
     * cripple one of the key features of the generator, which is its ability to
     * correctly model polymorphic types.
     * </p>
     */
    @WithDefault("false")
    boolean skipSubtypeLookup();

    /**
     * Corresponds to {@code JacksonOption.IGNORE_TYPE_INFO_TRANSFORM}.
     * <p>
     * If enabled, the transformation of the schema based on a {@code @JsonTypeInfo}
     * annotation will be skipped.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. The {@code @JsonTypeInfo} annotation is the
     * standard mechanism for representing type information in JSON. Ignoring it
     * would lead to an incomplete and incorrect schema for polymorphic types.
     * </p>
     */
    @WithDefault("false")
    boolean ignoreTypeInfoTransform();

    /**
     * Corresponds to {@code JacksonOption.JSONIDENTITY_REFERENCE_ALWAYS_AS_ID}.
     * <p>
     * If enabled, properties referencing an object that has an ID (via
     * {@code @JsonIdentityInfo}) will be represented as a simple ID field,
     * rather than a {@code $ref}.
     * </p>
     * <p>
     * <b>Default: {@code false}</b>. The standard approach is to use {@code $ref},
     * which is more aligned with the JSON Schema specification for referencing.
     * This option should be enabled only if a different output format is specifically required.
     * </p>
     */
    @WithDefault("false")
    boolean jsonIdentityReferenceAlwaysAsId();
}
