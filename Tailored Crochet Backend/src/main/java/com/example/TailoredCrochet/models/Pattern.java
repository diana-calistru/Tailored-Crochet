package com.example.TailoredCrochet.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "pattern")
public class Pattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "pattern_id")
    private Long id;

    @Column(name = "pattern_name")
    private String patternName;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "swatch_id")
    private Swatch swatch;


    @ManyToOne
    @JoinColumn(name = "measurement_profile_id", nullable = false)
    private MeasurementProfile measurementProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "pattern_part",
            joinColumns = @JoinColumn(name = "pattern_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> parts = new HashSet<>();

    public void addPart(Part part) {
        parts.add(part);
        part.getPatterns().add(this);
    }

    public void removePart(Part part) {
        parts.remove(part);
        part.getPatterns().remove(this);
    }
}
