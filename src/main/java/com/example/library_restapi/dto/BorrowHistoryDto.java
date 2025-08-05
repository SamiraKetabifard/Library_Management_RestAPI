package com.example.library_restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BorrowHistoryDto {
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status; // "RETURNED" or "ACTIVE"
}
