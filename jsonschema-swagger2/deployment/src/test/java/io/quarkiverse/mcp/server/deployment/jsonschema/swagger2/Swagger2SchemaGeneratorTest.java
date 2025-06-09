package io.quarkiverse.mcp.server.deployment.jsonschema.swagger2;

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

import io.quarkiverse.mcp.server.deployment.jsonschema.swagger2.dto.TestDtoSwagger2;
import io.quarkiverse.mcp.server.runtime.jsonschema.swagger2.Swagger2SchemaGeneratorConfigCustomizer;
import io.quarkiverse.mcp.server.runtime.jsonschema.swagger2.config.Swagger2BuildTimeConfig;
import io.quarkus.test.QuarkusUnitTest;

import static org.junit.jupiter.api.Assertions.*;

public class Swagger2SchemaGeneratorTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
            .addClasses(
                TestDtoSwagger2.class,
                Swagger2SchemaGeneratorConfigCustomizer.class,
                Swagger2BuildTimeConfig.class
            )
            .addAsResource("application-swagger2-test.properties", "application.properties")
        );

    private SchemaGeneratorConfigBuilder getBaseConfigBuilder() {
        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    }

    @Test
    void testSwagger2Annotations_moduleEnabled() {
        Swagger2BuildTimeConfig buildConfig = new Swagger2BuildTimeConfig();
        buildConfig.enable = true; // Explicitly enable

        Swagger2SchemaGeneratorConfigCustomizer customizer = new Swagger2SchemaGeneratorConfigCustomizer();
        customizer.config = buildConfig;

        SchemaGeneratorConfigBuilder configBuilder = getBaseConfigBuilder();
        customizer.customize(configBuilder);
        SchemaGeneratorConfig schemaGenConfig = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(schemaGenConfig);
        JsonNode jsonSchema = generator.generateSchema(TestDtoSwagger2.class);

        // System.out.println("Enabled: " + jsonSchema.toPrettyString());

        assertNotNull(jsonSchema);
        JsonNode properties = jsonSchema.get("properties");
        assertNotNull(properties);

        JsonNode itemIdSchema = properties.get("itemId");
        assertNotNull(itemIdSchema);
        assertEquals("Description for the item ID.", itemIdSchema.get("description").asText());
        assertTrue(jsonSchema.get("required").toString().contains("\"itemId\""), "'itemId' should be required");
        assertEquals("item-123", itemIdSchema.get("examples").get(0).asText());

        JsonNode quantitySchema = properties.get("quantity");
        assertNotNull(quantitySchema);
        assertEquals("Quantity of the item.", quantitySchema.get("description").asText());
        // Ensure 'quantity' is not accidentally marked as required
        assertFalse(jsonSchema.has("required") && jsonSchema.get("required").toString().contains("\"quantity\""),
                    "'quantity' should NOT be required");
        assertEquals(10, quantitySchema.get("examples").get(0).asInt());
    }

    @Test
    void testSwagger2Annotations_moduleDisabled() {
        Swagger2BuildTimeConfig buildConfig = new Swagger2BuildTimeConfig();
        buildConfig.enable = false; // Explicitly disable

        Swagger2SchemaGeneratorConfigCustomizer customizer = new Swagger2SchemaGeneratorConfigCustomizer();
        customizer.config = buildConfig;

        SchemaGeneratorConfigBuilder configBuilder = getBaseConfigBuilder();
        customizer.customize(configBuilder); // This should not add the module
        SchemaGeneratorConfig schemaGenConfig = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(schemaGenConfig);
        JsonNode jsonSchema = generator.generateSchema(TestDtoSwagger2.class);

        // System.out.println("Disabled: " + jsonSchema.toPrettyString());

        assertNotNull(jsonSchema);
        JsonNode properties = jsonSchema.get("properties");
        assertNotNull(properties);

        JsonNode itemIdSchema = properties.get("itemId");
        assertNotNull(itemIdSchema);
        assertNull(itemIdSchema.get("description"));
        assertFalse(jsonSchema.has("required") && jsonSchema.get("required").toString().contains("\"itemId\""));
        assertNull(itemIdSchema.get("examples"));

        JsonNode quantitySchema = properties.get("quantity");
        assertNotNull(quantitySchema);
        assertNull(quantitySchema.get("description"));
    }
}
