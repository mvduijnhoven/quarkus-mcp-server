package io.quarkiverse.mcp.server.deployment.jsonschema.swagger15;

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

import io.quarkiverse.mcp.server.deployment.jsonschema.swagger15.dto.TestDtoSwagger15;
import io.quarkiverse.mcp.server.runtime.jsonschema.swagger15.Swagger15SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.swagger15.config.Swagger15BuildTimeConfig;
import io.quarkus.test.QuarkusUnitTest;

import static org.junit.jupiter.api.Assertions.*;

public class Swagger15SchemaGeneratorTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
            .addClasses(
                TestDtoSwagger15.class,
                Swagger15SchemaGeneratorConfigCustomizer.class,
                Swagger15BuildTimeConfig.class
            )
            .addAsResource("application-swagger15-test.properties", "application.properties")
        );

    private SchemaGeneratorConfigBuilder getBaseConfigBuilder() {
        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    }

    @Test
    void testSwagger15Annotations_moduleEnabled() {
        Swagger15BuildTimeConfig buildConfig = new Swagger15BuildTimeConfig();
        buildConfig.enable = true; // Explicitly enable

        Swagger15SchemaGeneratorConfigCustomizer customizer = new Swagger15SchemaGeneratorConfigCustomizer();
        customizer.config = buildConfig;

        SchemaGeneratorConfigBuilder configBuilder = getBaseConfigBuilder();
        customizer.customize(configBuilder);
        SchemaGeneratorConfig schemaGenConfig = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(schemaGenConfig);
        JsonNode jsonSchema = generator.generateSchema(TestDtoSwagger15.class);

        //System.out.println("Enabled: " + jsonSchema.toPrettyString());

        assertNotNull(jsonSchema);
        JsonNode properties = jsonSchema.get("properties");
        assertNotNull(properties);

        JsonNode nameSchema = properties.get("name");
        assertNotNull(nameSchema);
        assertEquals("This is a description for the name field.", nameSchema.get("description").asText());
        assertTrue(jsonSchema.get("required").toString().contains("\"name\""), "'name' should be required");
        assertEquals("John Doe", nameSchema.get("examples").get(0).asText(), "Example should be present for name");


        JsonNode ageSchema = properties.get("age");
        assertNotNull(ageSchema);
        assertEquals("Age of the person.", ageSchema.get("description").asText());
        assertEquals(30, ageSchema.get("examples").get(0).asInt(), "Example should be present for age");
        assertFalse(jsonSchema.has("required") && jsonSchema.get("required").toString().contains("\"age\""),
                    "'age' should NOT be required");
    }

    @Test
    void testSwagger15Annotations_moduleDisabled() {
        Swagger15BuildTimeConfig buildConfig = new Swagger15BuildTimeConfig();
        buildConfig.enable = false; // Explicitly disable

        Swagger15SchemaGeneratorConfigCustomizer customizer = new Swagger15SchemaGeneratorConfigCustomizer();
        customizer.config = buildConfig;

        SchemaGeneratorConfigBuilder configBuilder = getBaseConfigBuilder();
        customizer.customize(configBuilder); // This should not add the module
        SchemaGeneratorConfig schemaGenConfig = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(schemaGenConfig);
        JsonNode jsonSchema = generator.generateSchema(TestDtoSwagger15.class);

        //System.out.println("Disabled: " + jsonSchema.toPrettyString());

        assertNotNull(jsonSchema);
        JsonNode properties = jsonSchema.get("properties");
        assertNotNull(properties);

        JsonNode nameSchema = properties.get("name");
        assertNotNull(nameSchema);
        // When module is disabled, these swagger annotations should not be processed
        assertNull(nameSchema.get("description"), "Description should be absent when module is disabled");
        assertFalse(jsonSchema.has("required") && jsonSchema.get("required").toString().contains("\"name\""),
                    "'name' should NOT be required when module is disabled");
        assertNull(nameSchema.get("examples"), "Examples should be absent when module is disabled");


        JsonNode ageSchema = properties.get("age");
        assertNotNull(ageSchema);
        assertNull(ageSchema.get("description"), "Description for age should be absent");
    }
}
