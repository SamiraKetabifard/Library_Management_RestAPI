package com.example.library_restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HistoryDto {

    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private Boolean isReturned;
}
