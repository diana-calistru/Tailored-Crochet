package com.example.TailoredCrochet.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "swatch")
public class Swatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "swatch_id")
    private Long id;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @Column (name = "row_count")
    private Integer rows;

    @Column(name = "stitch_count")
    private Integer stitches;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
