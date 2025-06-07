package io.quarkiverse.mcp.server.deployment.jsonschema.javaxvalidation; // Adjust package per feature

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class JsonSchemaJavaxValidationProcessor { // Adjust class name per feature

    private static final String FEATURE = "mcp-jsonschema-javax-validation"; // Adjust feature name per feature

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
