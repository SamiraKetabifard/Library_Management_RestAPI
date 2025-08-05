package com.example.library_restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OverdueBookDto {

    private String bookTitle;
    private String borrowerName;
    private LocalDate dueDate;
    private long daysOverdue;
}
