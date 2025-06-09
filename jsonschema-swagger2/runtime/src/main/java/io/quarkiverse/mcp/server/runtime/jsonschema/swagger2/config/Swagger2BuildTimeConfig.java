package io.quarkiverse.mcp.server.runtime.jsonschema.swagger2.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigRoot(name = "mcp.server.jsonschema.swagger2", phase = ConfigPhase.BUILD_TIME)
public class Swagger2BuildTimeConfig {

    /**
     * Enable the Swagger 2 module.
     * If true, Swagger 2 annotations will be used for schema generation.
     */
    @ConfigItem(defaultValue = "true") // Assuming it should be enabled by default
    public boolean enable;
}
