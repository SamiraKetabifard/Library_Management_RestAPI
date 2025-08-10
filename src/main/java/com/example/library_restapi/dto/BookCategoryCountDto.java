package com.example.library_restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookCategoryCountDto {

    private String category;
    private Long count;
}
