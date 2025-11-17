package com.example.TailoredCrochet.dto;

import com.example.TailoredCrochet.models.GarmentType;
import com.example.TailoredCrochet.models.Part;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartDTO {
    private Long id;
    private String partType;
    private String partStyle;
    private String imageUrl;

}
