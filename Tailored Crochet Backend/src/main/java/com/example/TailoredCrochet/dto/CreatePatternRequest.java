package com.example.TailoredCrochet.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreatePatternRequest {

    private String patternName;
    private String garmentType;

    private List<PartSelection> selectedParts = new ArrayList<>();

    private SwatchDTO swatch;

    private String measurementProfileName;

    private String userEmail;

    @Setter
    @Getter
    public static class PartSelection {
        private String partType;
        private String partStyle;
    }

    @Setter
    @Getter
    public static class SwatchDTO {
        private Double width;
        private Double height;
        private Integer rows;
        private Integer stitches;
    }
}
