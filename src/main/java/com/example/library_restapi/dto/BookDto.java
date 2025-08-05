package com.example.library_restapi.dto;

import lombok.Data;

@Data
public class BookDto {

    private String title;
    private String author;
    private String isbnNumber;
    private Integer quantity;
    private Boolean isAvailable;
}
