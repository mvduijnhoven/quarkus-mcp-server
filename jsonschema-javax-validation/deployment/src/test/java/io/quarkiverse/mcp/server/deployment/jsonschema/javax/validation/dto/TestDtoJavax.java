package io.quarkiverse.mcp.server.deployment.jsonschema.javax.validation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TestDtoJavax {

    @NotNull
    private String mandatoryField;

    private String optionalField;

    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String patternField;

    @Size(min = 1, max = 10)
    private String sizeField;

    // Getters and setters
    public String getMandatoryField() {
        return mandatoryField;
    }

    public void setMandatoryField(String mandatoryField) {
        this.mandatoryField = mandatoryField;
    }

    public String getOptionalField() {
        return optionalField;
    }

    public void setOptionalField(String optionalField) {
        this.optionalField = optionalField;
    }

    public String getPatternField() {
        return patternField;
    }

    public void setPatternField(String patternField) {
        this.patternField = patternField;
    }

    public String getSizeField() {
        return sizeField;
    }

    public void setSizeField(String sizeField) {
        this.sizeField = sizeField;
    }
}
