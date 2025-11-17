package com.example.TailoredCrochet.dto;

import com.example.TailoredCrochet.models.Swatch;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatternGenerationRequest {
    private String patternName;
    private String garmentType;
    private String measurementProfileName;
    private Swatch swatch;
    private List<PartRequest> selectedParts;

    private String userEmail;
}

