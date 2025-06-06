package io.quarkiverse.mcp.server.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.FeatureMetadata;
import io.quarkiverse.mcp.server.ToolFilter;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkus.arc.InstanceImpl;
import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.vertx.core.Vertx;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ToolManagerSchemaGenerationTest {

    // Dependencies for ToolManagerImpl
    McpMetadata metadata;
    Vertx vertx;
    ObjectMapper objectMapper;
    ConnectionManager connectionManager;
    InstanceImpl<CurrentIdentityAssociation> currentIdentityAssociation; // Use InstanceImpl for mocking Arc's Instance
    ResponseHandlers responseHandlers;
    List<ToolFilter> filters;

    McpServerRuntimeConfig runtimeConfig;

    // Helper classes for testing
    static class SimplePojo {
        public String name;
        public int value;
    }

    static class JacksonPojo {
        @JsonProperty("custom_name")
        public String originalName;
        @JsonIgnore
        public String ignoredField;
        public int age;
    }

    static class JakartaValidationPojo {
        @NotNull
        public String mandatoryField;
        @Size(min = 5, max = 10)
        public String sizedField;
        public String optionalField;
    }

    static class CombinedPojo {
        @JsonProperty("key")
        @NotNull
        public String identifier;

        @Size(max = 5)
        public String shortText;
    }

    @BeforeEach
    void setUp() {
        // Mock all dependencies of ToolManagerImpl
        metadata = mock(McpMetadata.class);
        when(metadata.tools()).thenReturn(Collections.<FeatureMetadata<ToolResponse>>emptyList());
        when(metadata.defaultValueConverters()).thenReturn(Collections.emptyMap());

        vertx = mock(Vertx.class);
        objectMapper = new ObjectMapper(); // Use a real ObjectMapper
        connectionManager = mock(ConnectionManager.class);

        // Mock Instance<CurrentIdentityAssociation>
        CurrentIdentityAssociation mockIdentityAssociation = mock(CurrentIdentityAssociation.class);
        currentIdentityAssociation = mock(InstanceImpl.class);
        when(currentIdentityAssociation.get()).thenReturn(mockIdentityAssociation);


        responseHandlers = mock(ResponseHandlers.class);
        filters = Collections.emptyList(); // No filters for these tests

        // Initialize McpServerRuntimeConfig
        runtimeConfig = new McpServerRuntimeConfig();
        runtimeConfig.schema = new McpServerRuntimeConfig.SchemaConfig();
        runtimeConfig.schema.jacksonModule = new McpServerRuntimeConfig.JacksonModuleConfig();
        runtimeConfig.schema.jakartaValidationModule = new McpServerRuntimeConfig.JakartaValidationModuleConfig();
    }

    private ToolManagerImpl createToolManager() {
        return new ToolManagerImpl(metadata, vertx, objectMapper, connectionManager,
                                   currentIdentityAssociation, responseHandlers, filters, runtimeConfig);
    }

    @Test
    void testSchemaGenerationWithNoModules() {
        runtimeConfig.schema.jacksonModule.enabled = false;
        runtimeConfig.schema.jakartaValidationModule.enabled = false;
        ToolManagerImpl toolManager = createToolManager();

        // Assuming a method in ToolManagerImpl to generate schema for a class for testing
        JsonNode schema = toolManager.generateSchemaForTest(SimplePojo.class);

        assertNotNull(schema);
        assertEquals("object", schema.get("type").asText());
        assertNotNull(schema.get("properties").get("name"));
        assertNotNull(schema.get("properties").get("value"));
        // No module-specific keywords expected
        assertNull(schema.get("required"));
        JsonNode nameProps = schema.get("properties").get("name");
        // Check that no Jackson/Jakarta specific annotations are processed
        // (e.g. no "custom_name", no "minLength", "maxLength", "pattern" etc.)
        // A simple string property schema would be {"type":"string"}.
        // If it has other schema keywords like "description", "pattern", etc. those would be extra.
        // For a plain string with no annotations, victools jsonschema-generator usually just produces {"type":"string"}
        assertTrue(nameProps.size() == 1 && nameProps.has("type"), "Plain string schema should only have 'type'");
    }

    @Test
    void testSchemaGenerationWithJacksonModuleEnabled() {
        runtimeConfig.schema.jacksonModule.enabled = true;
        runtimeConfig.schema.jakartaValidationModule.enabled = false;
        ToolManagerImpl toolManager = createToolManager();

        JsonNode schema = toolManager.generateSchemaForTest(JacksonPojo.class);

        assertNotNull(schema);
        assertNotNull(schema.get("properties").get("custom_name"), "Should use @JsonProperty name");
        assertNull(schema.get("properties").get("originalName"), "Original name should not be present");
        assertNull(schema.get("properties").get("ignoredField"), "Should respect @JsonIgnore");
        assertNotNull(schema.get("properties").get("age"));
    }

    @Test
    void testSchemaGenerationWithJakartaValidationModuleEnabled() {
        runtimeConfig.schema.jacksonModule.enabled = false;
        runtimeConfig.schema.jakartaValidationModule.enabled = true;
        ToolManagerImpl toolManager = createToolManager();

        JsonNode schema = toolManager.generateSchemaForTest(JakartaValidationPojo.class);

        assertNotNull(schema);
        assertNotNull(schema.get("properties").get("mandatoryField"));
        assertNotNull(schema.get("properties").get("sizedField"));

        JsonNode requiredArray = schema.get("required");
        assertNotNull(requiredArray, "Required array should be present");
        assertTrue(requiredArray.isArray());
        boolean foundMandatory = false;
        for (JsonNode fieldName : requiredArray) {
            if ("mandatoryField".equals(fieldName.asText())) {
                foundMandatory = true;
                break;
            }
        }
        assertTrue(foundMandatory, "mandatoryField should be in required array");

        assertEquals(5, schema.get("properties").get("sizedField").get("minLength").asInt());
        assertEquals(10, schema.get("properties").get("sizedField").get("maxLength").asInt());
    }

    @Test
    void testSchemaGenerationWithBothModulesEnabled() {
        runtimeConfig.schema.jacksonModule.enabled = true;
        runtimeConfig.schema.jakartaValidationModule.enabled = true;
        ToolManagerImpl toolManager = createToolManager();

        JsonNode schema = toolManager.generateSchemaForTest(CombinedPojo.class);

        assertNotNull(schema);
        // Jackson part
        assertNotNull(schema.get("properties").get("key"), "Should use @JsonProperty name 'key'");
        assertNull(schema.get("properties").get("identifier"), "Original name 'identifier' should not be present");

        // Jakarta Validation part for 'key' (which was 'identifier' before Jackson mapping)
        JsonNode requiredArray = schema.get("required");
        assertNotNull(requiredArray, "Required array should be present");
        boolean foundKey = false;
        for (JsonNode fieldName : requiredArray) {
            if ("key".equals(fieldName.asText())) { // Name after Jackson processing
                foundKey = true;
                break;
            }
        }
        assertTrue(foundKey, "'key' (from identifier) should be in required array");

        // Jakarta Validation part for 'shortText'
        assertNotNull(schema.get("properties").get("shortText"));
        assertEquals(5, schema.get("properties").get("shortText").get("maxLength").asInt());
    }
}
