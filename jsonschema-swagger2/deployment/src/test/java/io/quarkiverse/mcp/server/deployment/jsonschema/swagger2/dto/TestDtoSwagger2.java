package io.quarkiverse.mcp.server.deployment.jsonschema.swagger2.dto;

// The victools swagger2 module uses io.swagger.annotations (from swagger-core 1.x)
import io.swagger.annotations.ApiModelProperty;

public class TestDtoSwagger2 {

    @ApiModelProperty(value = "Description for the item ID.", required = true, example = "item-123")
    private String itemId;

    @ApiModelProperty(value = "Quantity of the item.", example = "10")
    private Integer quantity;

    // Getters and setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
