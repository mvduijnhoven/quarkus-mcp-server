package jakartavalidation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.mcp.server.ToolManager;
import io.quarkiverse.mcp.server.runtime.jsonschema.jakartavalidation.JakartaValidationSchemaGeneratorConfigCustomizer;
import io.quarkus.test.QuarkusUnitTest;
import io.vertx.core.json.JsonObject;

public class JakartaValidationSchemaGeneratorConfigCustomizerTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClasses(JakartaValidationSchemaGeneratorConfigCustomizer.class, MyTools.class));

    @Inject
    ToolManager toolManager;

    @Test
    public void testDefaultModulesAreLoadedAndApplied() {
        // Given
        var expectedToolJson = """
                {
                  "name" : "add-task",
                  "description" : "Adds a task.",
                  "inputSchema" : {
                    "type" : "object",
                    "properties" : {
                      "task" : {
                        "type" : "object",
                        "properties" : {
                          "description" : {
                            "type" : "string"
                          },
                          "title" : {
                            "type" : "string",
                            "minLength" : 1
                          }
                        },
                        "required" : [ "title" ],
                        "description" : "The task to add."
                      }
                    },
                    "required" : [ "task" ]
                  }
                }
                """;

        // When
        var actualToolJson = toolManager.getTool("add-task").asJson().toString();

        // Then
        assertJsonEquals(expectedToolJson, actualToolJson);
    }

    private static void assertJsonEquals(String expected, String actual) {
        assertEquals(new JsonObject(expected), new JsonObject(actual));
    }
}
