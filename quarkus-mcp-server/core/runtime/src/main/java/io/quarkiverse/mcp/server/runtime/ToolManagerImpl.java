package io.quarkiverse.mcp.server.runtime;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import io.quarkiverse.mcp.Tool;
import io.quarkiverse.mcp.ToolArgument;
import io.quarkiverse.mcp.ToolExecutable;
import io.quarkiverse.mcp.ToolExecutionException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ToolManagerImpl {

    private static final Logger LOG = Logger.getLogger(ToolManagerImpl.class);

    private final Map<String, ToolExecutable> tools = new HashMap<>();
    private final SchemaGenerator schemaGenerator;
    private final McpServerRuntimeConfig mcpServerRuntimeConfig;

    @Inject
    public ToolManagerImpl(List<ToolExecutable> toolImplementations, McpServerRuntimeConfig mcpServerRuntimeConfig) {
        this.mcpServerRuntimeConfig = mcpServerRuntimeConfig;
        for (ToolExecutable toolImpl : toolImplementations) {
            String toolName = toolImpl.getClass().getAnnotation(Tool.class).value();
            if (tools.containsKey(toolName)) {
                throw new IllegalStateException("Duplicate tool name: " + toolName);
            }
            tools.put(toolName, toolImpl);
        }

        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
                .without(Option.SCHEMA_VERSION_INDICATOR);

        if (mcpServerRuntimeConfig.schema.jacksonModule.enabled) {
            if (isClassPresent("com.github.victools.jsonschema.module.jackson.JacksonModule")) {
                configBuilder.forTypesInGeneral().withModule(new JacksonModule());
            } else {
                LOG.warn("Jackson module for schema generation is enabled but 'com.github.victools.jsonschema.module.jackson.JacksonModule' not found on classpath. Skipping.");
            }
        }

        if (mcpServerRuntimeConfig.schema.jakartaValidationModule.enabled) {
            if (isClassPresent("com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule")) {
                configBuilder.forTypesInGeneral().withModule(new JakartaValidationModule());
            } else {
                LOG.warn("Jakarta Validation module for schema generation is enabled but 'com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule' not found on classpath. Skipping.");
            }
        }

        SchemaGeneratorConfig schemaGeneratorConfig = configBuilder.build();
        this.schemaGenerator = new SchemaGenerator(schemaGeneratorConfig);
    }

    public List<String>getToolNames() {
        return new ArrayList<>(tools.keySet());
    }

    public JsonNodegetToolSchema(String toolName) {
        ToolExecutable tool = tools.get(toolName);
        if (tool == null) {
            return null;
        }
        Method executeMethod = findExecuteMethod(tool.getClass());
        return schemaGenerator.generateSchema(executeMethod.getDeclaringClass());
    }

    public Object executeTool(String toolName, Map<String, Object> arguments) throws ToolExecutionException {
        ToolExecutable tool = tools.get(toolName);
        if (tool == null) {
            throw new ToolExecutionException("Tool not found: " + toolName);
        }
        Method executeMethod = findExecuteMethod(tool.getClass());
        Object[] args = new Object[executeMethod.getParameterCount()];
        for (int i = 0; i < executeMethod.getParameterCount(); i++) {
            Parameter parameter = executeMethod.getParameters()[i];
            ToolArgument argAnnotation = parameter.getAnnotation(ToolArgument.class);
            if (argAnnotation == null) {
                throw new ToolExecutionException("Parameter " + parameter.getName() + " of tool " + toolName + " is not annotated with @ToolArgument");
            }
            String argName = argAnnotation.value();
            if (!arguments.containsKey(argName)) {
                throw new ToolExecutionException("Missing argument " + argName + " for tool " + toolName);
            }
            args[i] = arguments.get(argName);
        }
        try {
            return executeMethod.invoke(tool, args);
        } catch (Exception e) {
            throw new ToolExecutionException("Error executing tool " + toolName, e);
        }
    }

    private Method findExecuteMethod(Class<?> toolClass) {
        for (Method method : toolClass.getMethods()) {
            if (method.getName().equals("execute")) {
                // Basic heuristic: assume the method named "execute" is the one.
                // More sophisticated checks could be added (e.g., specific annotation).
                return method;
            }
        }
        throw new IllegalStateException("No execute method found in tool " + toolClass.getName());
    }

    private boolean isClassPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
