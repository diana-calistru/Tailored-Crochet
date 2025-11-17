package com.example.TailoredCrochet.dto;

import com.example.TailoredCrochet.models.GarmentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarmentTypeDTO {
    private final String garmentName;
    private final String imageUrl;

    public GarmentTypeDTO (GarmentType garmentType) {
        this.garmentName = garmentType.getGarmentName();
        this.imageUrl = garmentType.getImageUrl();
    }
}
