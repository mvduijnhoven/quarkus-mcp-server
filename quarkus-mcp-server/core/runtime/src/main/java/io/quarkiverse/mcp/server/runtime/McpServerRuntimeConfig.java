package io.quarkiverse.mcp.server.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "mcp-server", phase = ConfigPhase.RUN_TIME)
public class McpServerRuntimeConfig {

    /**
     * Configuration for JSON schema generation.
     */
    @ConfigItem(name = "schema")
    public SchemaConfig schema;

    @ConfigGroup
    public static class SchemaConfig {

        /**
         * Jackson module configuration.
         */
        @ConfigItem(name = "jackson-module")
        public JacksonModuleConfig jacksonModule;

        /**
         * Jakarta Validation module configuration.
         */
        @ConfigItem(name = "jakarta-validation-module")
        public JakartaValidationModuleConfig jakartaValidationModule;
    }

    @ConfigGroup
    public static class JacksonModuleConfig {

        /**
         * If true, the Jackson module will be enabled for JSON schema generation.
         * The 'com.github.victools:jsonschema-module-jackson' dependency must be present on the classpath.
         */
        @ConfigItem(defaultValue = "false")
        public boolean enabled;
    }

    @ConfigGroup
    public static class JakartaValidationModuleConfig {

        /**
         * If true, the Jakarta Validation module will be enabled for JSON schema generation.
         * The 'com.github.victools:jsonschema-module-jakarta-validation' dependency must be present on the classpath.
         */
        @ConfigItem(defaultValue = "false")
        public boolean enabled;
    }
}
