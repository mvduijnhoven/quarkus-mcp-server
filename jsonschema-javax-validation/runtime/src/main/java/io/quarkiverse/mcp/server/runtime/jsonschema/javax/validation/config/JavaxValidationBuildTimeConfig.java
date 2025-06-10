package io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigRoot(name = "mcp.server.jsonschema.javax-validation", phase = ConfigPhase.BUILD_TIME)
public class JavaxValidationBuildTimeConfig {

    /**
     * Option: NOT_NULLABLE_FIELD_IS_REQUIRED
     * Consider fields that are not nullable (e.g. @NotNull) as required.
     */
    @ConfigItem(defaultValue = "true")
    public boolean notNullableFieldIsRequired;

    /**
     * Option: NOT_NULLABLE_METHOD_IS_REQUIRED
     * Consider methods that are not nullable (e.g. @NotNull) as required.
     */
    @ConfigItem(defaultValue = "true")
    public boolean notNullableMethodIsRequired;

    /**
     * Option: INCLUDE_PATTERN_PROPERTIES
     * Include @Pattern annotations as 'pattern' schema attributes.
     */
    @ConfigItem(defaultValue = "true")
    public boolean includePatternProperties;
}
