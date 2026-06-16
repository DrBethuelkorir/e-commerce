package com.beviamy.dreamers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String FiliName;
    private String FileType;

    @Lob
    private Blob image;
    private String Downloadurl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
