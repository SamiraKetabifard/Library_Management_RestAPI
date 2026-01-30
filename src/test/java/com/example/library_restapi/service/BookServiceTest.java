package com.example.library_restapi.service;

import com.example.library_restapi.dto.BookCategoryCountDto;
import com.example.library_restapi.dto.BookDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BookCategory;
import com.example.library_restapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_ReturnsPaginatedBooks() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        Page<Book> expectedPage = new PageImpl<>(List.of(new Book()));

        when(bookRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Book> result = bookService.getAllBooks(0, 10, "title", "asc");

        assertEquals(expectedPage, result);
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getBookById_Exists_ReturnsBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getBookById_NotExists_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void addBook_ValidCategory_SavesBook() {
        BookDto dto = new BookDto("Title", "Jac", "1",
                5, true, "SCIENCE");

        Book savedBook = new Book();
        savedBook.setBookCategory(BookCategory.SCIENCE);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Book result = bookService.addBook(dto);

        assertEquals(BookCategory.SCIENCE, result.getBookCategory());
    }

    @Test
    void addBook_InvalidCategory_ThrowsException() {
        BookDto dto = new BookDto("Title", "Jac", "1",
                5, true, "INVALID");

        assertThrows(RuntimeException.class, () -> bookService.addBook(dto));
    }

    @Test
    void updateBook_ValidCategory_UpdatesBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);

        BookDto dto = new BookDto("Golestan Saadi", "Saadi", "1",
                5, true, "LITERATURE");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        bookService.updateBook(1L, dto);

        assertEquals("Golestan Saadi", existingBook.getTitle());
        assertEquals(BookCategory.LITERATURE, existingBook.getBookCategory());
    }

    @Test
    void getBookCountByCategory_ReturnsStats() {
        Object[] stat1 = new Object[]{"SCIENCE", 3L};
        Object[] stat2 = new Object[]{"LITERATURE", 2L};

        when(bookRepository.countBooksByCategory()).thenReturn(List.of(stat1, stat2));

        List<BookCategoryCountDto> result = bookService.getBookCountByCategory();

        assertEquals(2, result.size());
        assertEquals("SCIENCE", result.get(0).getCategory());
        assertEquals(3L, result.get(0).getCount());
    }
}
