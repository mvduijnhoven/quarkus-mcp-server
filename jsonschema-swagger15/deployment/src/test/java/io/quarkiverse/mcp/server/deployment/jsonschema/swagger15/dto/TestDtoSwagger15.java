package io.quarkiverse.mcp.server.deployment.jsonschema.swagger15.dto;

import io.swagger.annotations.ApiModelProperty;

public class TestDtoSwagger15 {

    @ApiModelProperty(value = "This is a description for the name field.", required = true, example = "John Doe")
    private String name;

    @ApiModelProperty(value = "Age of the person.", example = "30")
    private int age;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
