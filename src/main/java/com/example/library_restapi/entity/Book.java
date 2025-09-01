package com.example.library_restapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbnNumber;
    private Integer quantity;
    private Boolean isAvailable;

    //bidirectional
    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<BookRating> ratings;

    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;
}
