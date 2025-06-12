package io.quarkiverse.mcp.server.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.mcp.server.schemagenerator.jackson")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface McpServerSchemaGeneratorJacksonRuntimeConfig {

    /**
     * Option: RESPECT_JSONPROPERTY_REQUIRED
     * Enable if JsonProperty.required() should be respected.
     */
    @WithDefault("true")
    boolean respectJsonpropertyRequired();
}
