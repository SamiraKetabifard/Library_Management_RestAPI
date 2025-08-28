package com.example.library_restapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1) @Max(5)
    private Integer rate;

    //bidirectional
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    //uni
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
