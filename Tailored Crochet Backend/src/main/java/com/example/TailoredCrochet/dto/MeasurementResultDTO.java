package com.example.TailoredCrochet.dto;

import com.example.TailoredCrochet.models.MeasurementProfile;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MeasurementResultDTO {
    private String name;
    private String notes;
    private Map<String, Double> measurements;

    public MeasurementResultDTO() {}

    public MeasurementResultDTO(String name, String notes, Map<String, Double> measurements) {
        this.name = name;
        this.notes = notes;
        this.measurements = measurements;
    }

    public MeasurementResultDTO(MeasurementProfile measurementProfile) {
        this.name = measurementProfile.getProfileName();
        this.notes = measurementProfile.getNotes();
        this.measurements = new HashMap<>();
        this.measurements.put("height", measurementProfile.getHeight());
        this.measurements.put("chest_circ", measurementProfile.getChestCirc());
        this.measurements.put("waist_circ", measurementProfile.getWaistCirc());
        this.measurements.put("pelvis_circ", measurementProfile.getPelvisCirc());
        this.measurements.put("neck_circ", measurementProfile.getNeckCirc());
        this.measurements.put("bicep_circ", measurementProfile.getBicepCirc());
        this.measurements.put("thigh_circ", measurementProfile.getThighCirc());
        this.measurements.put("knee_circ", measurementProfile.getKneeCirc());
        this.measurements.put("arm_length", measurementProfile.getArmLength());
        this.measurements.put("leg_length", measurementProfile.getLegLength());
        this.measurements.put("calf_length", measurementProfile.getCalfLength());
        this.measurements.put("head_circ", measurementProfile.getHeadCirc());
        this.measurements.put("wrist_circ", measurementProfile.getWristCirc());
        this.measurements.put("arm_span", measurementProfile.getArmSpan());
        this.measurements.put("shoulders_width", measurementProfile.getShouldersWidth());
        this.measurements.put("torso_length", measurementProfile.getTorsoLength());
        this.measurements.put("inner_leg", measurementProfile.getInnerLeg());
    }
}