package io.quarkiverse.mcp.server.deployment.jsonschema.javax.validation;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;

import io.quarkiverse.mcp.server.deployment.jsonschema.javax.validation.dto.TestDtoJavax;
// import io.quarkiverse.mcp.server.runtime.config.SchemaGeneratorConfigCustomizer; // Not directly used by class
import io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation.JavaxValidationSchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation.config.JavaxValidationBuildTimeConfig;
import io.quarkus.test.QuarkusUnitTest;

import static org.junit.jupiter.api.Assertions.*;

public class JavaxValidationSchemaGeneratorTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
            .addClasses(
                TestDtoJavax.class,
                JavaxValidationSchemaGeneratorConfigCustomizer.class,
                JavaxValidationBuildTimeConfig.class
                // SchemaGeneratorConfigCustomizer.class // Not needed if we are injecting the specific customizer
            )
            .addAsResource("application-javax-test.properties", "application.properties")
        );

    @Test
    void testJavaxValidationAnnotations() {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);

        // Manually create and apply the customizer as it would be in a Quarkus environment
        JavaxValidationBuildTimeConfig buildConfig = new JavaxValidationBuildTimeConfig();
        // Default config values:
        // buildConfig.notNullableFieldIsRequired = true;
        // buildConfig.notNullableMethodIsRequired = true;
        // buildConfig.includePatternProperties = true;
        // buildConfig.includeValidationMessages = false;

        JavaxValidationSchemaGeneratorConfigCustomizer customizer = new JavaxValidationSchemaGeneratorConfigCustomizer();
        // Simulate injection for the test
        customizer.config = buildConfig;
        customizer.customize(configBuilder);

        SchemaGeneratorConfig schemaGenConfig = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(schemaGenConfig);
        JsonNode jsonSchema = generator.generateSchema(TestDtoJavax.class);

        // System.out.println(jsonSchema.toPrettyString()); // For debugging

        assertNotNull(jsonSchema);
        JsonNode properties = jsonSchema.get("properties");
        assertNotNull(properties);

        // Test mandatoryField
        JsonNode mandatoryFieldSchema = properties.get("mandatoryField");
        assertNotNull(mandatoryFieldSchema);
        assertTrue(jsonSchema.get("required").toString().contains("\"mandatoryField\""), "'mandatoryField' should be required by default config");

        // Test patternField
        JsonNode patternFieldSchema = properties.get("patternField");
        assertNotNull(patternFieldSchema);
        assertEquals("^[A-Za-z0-9]+$", patternFieldSchema.get("pattern").asText());

        // Test sizeField
        JsonNode sizeFieldSchema = properties.get("sizeField");
        assertNotNull(sizeFieldSchema);
        assertEquals(1, sizeFieldSchema.get("minLength").asInt());
        assertEquals(10, sizeFieldSchema.get("maxLength").asInt());


        // Test with NOT_NULLABLE_FIELD_IS_REQUIRED = false
        buildConfig.notNullableFieldIsRequired = false;
        configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
        customizer.customize(configBuilder);
        schemaGenConfig = configBuilder.build();
        generator = new SchemaGenerator(schemaGenConfig);
        jsonSchema = generator.generateSchema(TestDtoJavax.class);

        // System.out.println(jsonSchema.toPrettyString()); // For debugging

        assertFalse(jsonSchema.has("required") && jsonSchema.get("required").toString().contains("\"mandatoryField\""),
                    "'mandatoryField' should NOT be required when notNullableFieldIsRequired is false");
    }
}
