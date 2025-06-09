package jakartavalidation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

@ApplicationScoped
public class MyTools {

    @Tool(name = "add-task", description = "Adds a task.")
    public Boolean addTask(@ToolArg(name = "task", description = "The task to add.") Task t) {
        return true;
    }

    public record Task(@NotBlank String title, String description) {
    }
}
