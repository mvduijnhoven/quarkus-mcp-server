package io.quarkiverse.mcp.server.deployment;

import java.util.List;

import jakarta.inject.Singleton;

import io.quarkiverse.mcp.server.runtime.JsonSchemaGenerator;
import io.quarkiverse.mcp.server.runtime.JsonSchemaGeneratorRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class JsonSchemaGeneratorProcessor {

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem("mcp-server-json-schema-generator");
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    void createJsonSchemaGenerator(BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
            JsonSchemaGeneratorRecorder recorder) {
        var syntheticBeanBuildItem = SyntheticBeanBuildItem.configure(JsonSchemaGenerator.class)
                .scope(Singleton.class)
                .addType(JsonSchemaGeneratorRecorder.JsonSchemaGeneratorImpl.class)
                .unremovable()
                .supplier(recorder.createJsonSchemaGenerator(List.of()))
                .done();
        syntheticBeanBuildItemBuildProducer.produce(syntheticBeanBuildItem);
    }
}
