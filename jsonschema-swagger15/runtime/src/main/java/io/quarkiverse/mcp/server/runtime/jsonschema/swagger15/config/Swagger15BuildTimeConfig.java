package io.quarkiverse.mcp.server.runtime.jsonschema.swagger15.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigRoot(name = "mcp.server.jsonschema.swagger15", phase = ConfigPhase.BUILD_TIME)
public class Swagger15BuildTimeConfig {

    /**
     * Enable the Swagger 1.5 module.
     * If true, Swagger 1.5 annotations will be used for schema generation.
     */
    @ConfigItem(defaultValue = "true") // Assuming it should be enabled by default
    public boolean enable;
}
