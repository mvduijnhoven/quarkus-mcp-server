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
     * Option: RESPECT_JSONPROPERTY_REQUIRED
     * Enable if JsonProperty.required() should be respected.
     */
    @WithDefault("true")
    boolean respectJsonpropertyRequired();
}
