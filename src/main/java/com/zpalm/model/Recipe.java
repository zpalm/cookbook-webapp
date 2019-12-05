package com.zpalm.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recipe {

    private final Long id;
    private final String name;
    private final List<String> ingredients;
    private final List<String> entry;
    
}
