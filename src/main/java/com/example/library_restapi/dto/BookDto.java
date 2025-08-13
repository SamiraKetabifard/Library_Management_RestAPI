package com.example.library_restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String title;
    private String author;
    private String isbnNumber;
    private Integer quantity;
    private Boolean isAvailable;
    private String category;

}
