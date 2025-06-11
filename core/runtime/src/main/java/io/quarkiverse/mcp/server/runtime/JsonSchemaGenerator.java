package io.quarkiverse.mcp.server.runtime;

import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface JsonSchemaGenerator {

    ObjectNode generateSchema(Type mainTargetType);
}
