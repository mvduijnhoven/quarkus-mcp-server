package io.quarkiverse.mcp.server.runtime.jsonschema.jackson;

import jakarta.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

@ApplicationScoped
public class MyTools {

    @Tool(name = "add-task", description = "Adds a task.")
    public Boolean addTask(@ToolArg(name = "task", description = "The task to add.") Task t) {
        return true;
    }

    public record Task(
            @JsonProperty(value = "title", required = true) @JsonPropertyDescription("The title of the task.") String t,
            String description) {
    }
}
