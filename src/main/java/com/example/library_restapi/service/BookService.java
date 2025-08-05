package com.example.library_restapi.service;

import com.example.library_restapi.dto.BookDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
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
}
