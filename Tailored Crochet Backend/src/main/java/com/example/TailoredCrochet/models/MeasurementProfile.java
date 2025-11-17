package com.example.TailoredCrochet.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "measurement_profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_profile_per_user",
                        columnNames = {"user_id", "measurement_profile_name"})
        })
@Data
public class MeasurementProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "measurement_profile_id")
    private Long id;

    @Column(name = "measurement_profile_name", nullable = false)
    private String profileName;

    @Column (name = "notes")
    private String notes;

    @Column (name = "height")
    private double height;

    @Column(name = "chest_circ")
    private double chestCirc;

    @Column(name = "waist_circ")
    private double waistCirc;

    @Column(name = "pelvis_circ")
    private double pelvisCirc;

    @Column(name = "neck_circ")
    private double neckCirc;

    @Column(name = "bicep_circ")
    private double bicepCirc;

    @Column(name = "thigh_circ")
    private double thighCirc;

    @Column(name = "knee_circ")
    private double kneeCirc;

    @Column(name = "arm_length")
    private double armLength;

    @Column(name = "leg_length")
    private double legLength;

    @Column(name = "calf_length")
    private double calfLength;

    @Column(name = "head_circ")
    private double headCirc;

    @Column(name = "wrist_circ")
    private double wristCirc;

    @Column(name = "arm_span")
    private double armSpan;

    @Column(name = "shoulders_width")
    private double shouldersWidth;

    @Column(name = "torso_length")
    private double torsoLength;

    @Column(name = "inner_leg")
    private double innerLeg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
