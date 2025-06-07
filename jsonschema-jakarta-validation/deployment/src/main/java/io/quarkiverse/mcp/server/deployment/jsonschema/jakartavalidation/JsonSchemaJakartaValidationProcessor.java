package io.quarkiverse.mcp.server.deployment.jsonschema.jakartavalidation; // Adjust package per feature

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class JsonSchemaJakartaValidationProcessor { // Adjust class name per feature

    private static final String FEATURE = "mcp-jsonschema-jakarta-validation"; // Adjust feature name per feature

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
