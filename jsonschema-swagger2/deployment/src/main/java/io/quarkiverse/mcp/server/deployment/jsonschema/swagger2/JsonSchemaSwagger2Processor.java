package io.quarkiverse.mcp.server.deployment.jsonschema.swagger2; // Adjust package per feature

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class JsonSchemaSwagger2Processor { // Adjust class name per feature

    private static final String FEATURE = "mcp-jsonschema-swagger2"; // Adjust feature name per feature

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
