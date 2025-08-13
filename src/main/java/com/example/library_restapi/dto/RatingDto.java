package com.example.library_restapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDto {

    @Min(1) @Max(5)
    private Integer rating;

    private Long bookId;
}
