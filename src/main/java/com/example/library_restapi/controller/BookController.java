package com.example.library_restapi.controller;

import com.example.library_restapi.dto.BookCategoryCountDto;
import com.example.library_restapi.dto.BookDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(bookService.getAllBooks(page, size, sortBy, direction));
    }

    @GetMapping("/getbookbyid/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> addBook(@RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.addBook(bookDto));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        bookService.updateBook(id, bookDto);
        return ResponseEntity.ok("Book with ID " + id + " has been updated successfully");
    }

    @DeleteMapping("/deletebook/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/by-author")
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @RequestParam String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }
    // Highly rated books
    @GetMapping("/highly-rated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Book>> getHighlyRatedBooks() {
        return ResponseEntity.ok(bookService.getHighlyRatedBooks());
    }
    @GetMapping("/category-stats")
    public ResponseEntity<List<BookCategoryCountDto>> getBookCountByCategory() {
        return ResponseEntity.ok(bookService.getBookCountByCategory());
    }
}
