package com.example.library_restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BorrowRecordDto {

    private Long recordId;
    private String username;
    private String bookTitle;
    private Boolean isReturned;
}
