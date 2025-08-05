package com.example.library_restapi.controller;

import com.example.library_restapi.entity.BorrowRecord;
import com.example.library_restapi.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrowRecords")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    // 1. Borrow Book (Authenticated users only)
    @PostMapping("/borrowBook/{bookId}")
    public ResponseEntity<BorrowRecord> borrowTheBook(@PathVariable long bookId) {
        return ResponseEntity.ok(borrowRecordService.borrowTheBook(bookId));
    }

    // 2. Return Book (Authenticated users only)
    @PostMapping("/returnBook/{borrowRecordId}")
    public ResponseEntity<BorrowRecord> returnTheBook(@PathVariable Long borrowRecordId) {
        return ResponseEntity.ok(borrowRecordService.returnTheBook(borrowRecordId));
    }
}
