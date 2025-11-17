package com.example.TailoredCrochet.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatternDTO {
    private Long id;
    private String garmentType;
    private String patternName;
    private String measurementProfileName;
    private List<PartDTO> parts;
}
