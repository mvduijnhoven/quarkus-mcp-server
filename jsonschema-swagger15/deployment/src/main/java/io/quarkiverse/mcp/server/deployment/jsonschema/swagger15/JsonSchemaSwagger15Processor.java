package io.quarkiverse.mcp.server.deployment.jsonschema.swagger15; // Adjust package per feature

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class JsonSchemaSwagger15Processor { // Adjust class name per feature

    private static final String FEATURE = "mcp-jsonschema-swagger15"; // Adjust feature name per feature

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
