package com.example.library_restapi.service;

import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BorrowRecord;
import com.example.library_restapi.entity.User;
import com.example.library_restapi.repository.BookRepository;
import com.example.library_restapi.repository.BorrowRecordRepository;
import com.example.library_restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public BorrowRecord borrowTheBook(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        if (book.getQuantity() <= 0 || !book.getIsAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(14));//14 days from now
        borrowRecord.setIsReturned(false);
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        book.setQuantity(book.getQuantity() - 1);

        if (book.getQuantity() == 0) {
            book.setIsAvailable(false);
        }

        bookRepository.save(book);
        return borrowRecordRepository.save(borrowRecord);
    }

    public BorrowRecord returnTheBook(Long borrowRecordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new RuntimeException("borrow Record is not found"));

        if (borrowRecord.getIsReturned()) {
            throw new RuntimeException("Book is Already returned");
        }

        Book book = borrowRecord.getBook();
        book.setQuantity(book.getQuantity() + 1);
        book.setIsAvailable(true);
        bookRepository.save(book);

        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.setIsReturned(true);

        return borrowRecordRepository.save(borrowRecord);
    }
}
