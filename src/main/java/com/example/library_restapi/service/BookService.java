package com.example.library_restapi.service;

import com.example.library_restapi.dto.BookDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Book addBook(BookDto bookDto){
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getTitle());
        book.setIsbnNumber(bookDto.getIsbnNumber());
        book.setIsAvailable(bookDto.getIsAvailable());
        book.setQuantity(bookDto.getQuantity());

        return bookRepository.save(book);
    }
    public Book updateBook(Long id, BookDto bookDto){
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        oldBook.setTitle(bookDto.getTitle());
        oldBook.setAuthor(bookDto.getAuthor());
        oldBook.setIsbnNumber(bookDto.getIsbnNumber());
        oldBook.setIsAvailable(bookDto.getIsAvailable());
        oldBook.setQuantity(bookDto.getQuantity());

        return bookRepository.save(oldBook);
    }
    public void deleteBookById(Long id){
        bookRepository.deleteById(id);
    }
    public List<Book> findByAuthor(String authorName) {
        return bookRepository.findByAuthor(authorName); // or findByAuthorContainingNative
    }

}
