package com.zpalm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "IngredientTypeBuilder", toBuilder = true)
@JsonDeserialize(builder = IngredientType.IngredientTypeBuilder.class)
public class IngredientType {

    private String type;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IngredientTypeBuilder {

    }
}
