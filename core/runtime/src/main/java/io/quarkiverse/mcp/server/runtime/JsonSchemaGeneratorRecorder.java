package io.quarkiverse.mcp.server.runtime;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.generator.Module;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class JsonSchemaGeneratorRecorder {

    public Supplier<JsonSchemaGenerator> createJsonSchemaGenerator(List<Supplier<Module>> moduleSuppliers) {
        return () -> {
            var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
                    .without(Option.SCHEMA_VERSION_INDICATOR);
            moduleSuppliers.forEach(moduleSupplier -> configBuilder.with(moduleSupplier.get()));
            var schemaGenerator = new SchemaGenerator(configBuilder.build());

            return new JsonSchemaGeneratorImpl(schemaGenerator);
        };
    }

    public static class JsonSchemaGeneratorImpl implements JsonSchemaGenerator {

        private final SchemaGenerator schemaGenerator;

        public JsonSchemaGeneratorImpl(SchemaGenerator schemaGenerator) {
            this.schemaGenerator = schemaGenerator;
        }

        @Override
        public ObjectNode generateSchema(Type mainTargetType) {
            return schemaGenerator.generateSchema(mainTargetType);
        }
    }
}
