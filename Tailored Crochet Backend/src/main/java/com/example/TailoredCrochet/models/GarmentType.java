package com.example.TailoredCrochet.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "garment_type")
public class GarmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "garment_type_id")
    private Long id;

    @Column (name = "garment_name")
    private String garmentName;

    @Column (name = "garment_image")
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "garment_type_part",
            joinColumns = @JoinColumn(name = "garment_type_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> parts = new HashSet<>();
}
