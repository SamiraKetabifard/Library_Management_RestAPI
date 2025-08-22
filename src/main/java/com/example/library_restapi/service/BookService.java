package com.example.library_restapi.service;

import com.example.library_restapi.dto.BookCategoryCountDto;
import com.example.library_restapi.dto.BookDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BookCategory;
import com.example.library_restapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Page<Book> getAllBooks(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Book Not Found"));
    }
    public Book addBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbnNumber(bookDto.getIsbnNumber());
        book.setQuantity(bookDto.getQuantity());
        book.setIsAvailable(bookDto.getIsAvailable());

        // Handle enum conversion
        if (bookDto.getCategory() != null) {
            try {
                book.setBookCategory(BookCategory.valueOf(bookDto.getCategory().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid category. Valid values are: "
                        + Arrays.toString(BookCategory.values()));
            }
        }

        return bookRepository.save(book);
    }
    public void updateBook(Long id, BookDto bookDto) {
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        oldBook.setTitle(bookDto.getTitle());
        oldBook.setAuthor(bookDto.getAuthor());
        oldBook.setIsbnNumber(bookDto.getIsbnNumber());
        oldBook.setQuantity(bookDto.getQuantity());
        oldBook.setIsAvailable(bookDto.getIsAvailable());

        if (bookDto.getCategory() != null) {
            try {
                oldBook.setBookCategory(BookCategory.valueOf(bookDto.getCategory().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid category");
            }
        }
        bookRepository.save(oldBook);
    }
    public void deleteBookById(Long id){
        bookRepository.deleteById(id);
    }
    public List<Book> findByAuthor(String authorName) {
        return bookRepository.findByAuthor(authorName);
    }
    public List<Book> getHighlyRatedBooks() {
        return bookRepository.findHighlyRatedBooks();
    }
    public List<BookCategoryCountDto> getBookCountByCategory() {
        List<Object[]> results = bookRepository.countBooksByCategory();
        return results.stream()
                .map(row -> new BookCategoryCountDto(
                        (String) row[0],  // category
                        ((Number) row[1]).longValue()  // count
                ))
                .collect(Collectors.toList());
    }
}
