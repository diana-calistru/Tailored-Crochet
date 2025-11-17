package com.example.TailoredCrochet.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "part_id")
    private Long id;

    @Column (name = "part_type")
    private String partType;

    @Column (name = "part_style")
    private String partStyle;

    @Column (columnDefinition = "jsonb")
    private String instructions;

    @Column (name = "image_url")
    private String imageUrl;

    @ManyToMany(mappedBy = "parts")
    private Set<Pattern> patterns = new HashSet<>();

    @ManyToMany(mappedBy = "parts")
    private Set<GarmentType> garmentTypes = new HashSet<>();
}
