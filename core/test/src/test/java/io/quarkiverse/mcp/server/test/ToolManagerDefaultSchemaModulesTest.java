package io.quarkiverse.mcp.server.test;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import io.quarkiverse.mcp.server.ToolManager;
import io.quarkiverse.mcp.server.runtime.ToolManagerImpl;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.lang.reflect.Field;

public class ToolManagerDefaultSchemaModulesTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClasses(DefaultTool.class, DefaultTool.InputArgs.class)
                     // Ensure Jackson and Jakarta Validation modules are available for this test
                    .addAsResource("application.properties"));

    @Inject
    ToolManager toolManager;

    public static class DefaultTool {
        public static class InputArgs {
            @NotNull
            @JsonPropertyDescription("The name of the item")
            public String itemName;

            @Size(min = 1, max = 10)
            public String itemCode;
        }

        public String processItem(InputArgs args) {
            return "Processed " + args.itemName;
        }
    }

    @Test
    public void testDefaultModulesAreLoadedAndApplied() throws Exception {
        toolManager.newTool("defaultTestTool")
                .description("A test tool for default schema modules")
                .setFunction(DefaultTool.class, "processItem", DefaultTool.InputArgs.class, String.class)
                .register();

        ToolManager.ToolInfo toolInfo = toolManager.getTool("defaultTestTool");
        Assertions.assertNotNull(toolInfo, "Tool should be registered");

        // Verify that Jackson and JakartaValidation modules are loaded by default
        Field schemaGeneratorField = ToolManagerImpl.class.getDeclaredField("schemaGenerator");
        schemaGeneratorField.setAccessible(true);
        SchemaGenerator schemaGenerator = (SchemaGenerator) schemaGeneratorField.get(toolManager);
        SchemaGeneratorConfig config = schemaGenerator.getConfig();

        Assertions.assertNotNull(config.getModule(JacksonModule.class), "JacksonModule should be loaded by default");
        Assertions.assertNotNull(config.getModule(JakartaValidationModule.class), "JakartaValidationModule should be loaded by default");

        // Check the generated schema
        String schemaJson = toolInfo.asJson().getJsonObject("inputSchema").toString();
        System.out.println("Default Schema: " + schemaJson);
        Assertions.assertTrue(schemaJson.contains("\"description\":\"The name of the item\""), "Schema should include @JsonPropertyDescription");
        Assertions.assertTrue(schemaJson.contains("\"required\":[\"itemName\"]"), "Schema should mark 'itemName' as required due to @NotNull");
        Assertions.assertTrue(schemaJson.contains("\"minLength\":1"), "Schema should include @Size min constraint");
        Assertions.assertTrue(schemaJson.contains("\"maxLength\":10"), "Schema should include @Size max constraint");
    }
}
