package com.example.TailoredCrochet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartWithInstructions {
    private String partType;
    private String partStyle;
    private String instructionsJson;

    public PartWithInstructions(String partType, String partStyle, String instructions) {
        this.partType = partType;
        this.partStyle = partStyle;
        this.instructionsJson = instructions;
    }
}
