package com.zpalm.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "RecipeStepBuilder", toBuilder = true)
@JsonDeserialize(builder = RecipeStep.RecipeStepBuilder.class)
public class RecipeStep {

    //private Long id;
    private String step;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RecipeStepBuilder {

    }
}
