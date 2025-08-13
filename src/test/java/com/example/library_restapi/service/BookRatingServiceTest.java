package com.example.library_restapi.service;

import com.example.library_restapi.dto.RatingDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BookRating;
import com.example.library_restapi.entity.User;
import com.example.library_restapi.repository.BookRatingRepository;
import com.example.library_restapi.repository.BookRepository;
import com.example.library_restapi.repository.BorrowRecordRepository;
import com.example.library_restapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRatingServiceTest {

    @Mock
    private BookRatingRepository ratingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BookRatingService bookRatingService;

    @Test
    void rateBook_ValidConditions_Success() {
        RatingDto dto = new RatingDto(5, 1L);
        User user = new User();
        user.setId(1L);
        Book book = new Book();
        book.setId(1L);

        when(userRepository.findByUsername("samira")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByUserAndBook(user, book)).thenReturn(true);
        when(borrowRecordRepository.existsByUserAndBookAndNotReturned(1L, 1L)).thenReturn(false);
        when(ratingRepository.findByBookIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(BookRating.class))).thenReturn(new BookRating());

        String result = bookRatingService.rateBook(dto, "samira");

        assertEquals("Book rated successfully", result);
    }

    @Test
    void rateBook_UserNotBorrowed_ThrowsException() {
        RatingDto dto = new RatingDto(5, 1L);
        User user = new User();
        Book book = new Book();

        when(userRepository.findByUsername("samira")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByUserAndBook(user, book)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> bookRatingService.rateBook(dto, "samira"),
                "You haven't borrowed this book");
    }

    @Test
    void rateBook_AlreadyRated_ThrowsException() {
        RatingDto dto = new RatingDto(5, 1L);
        User user = new User();
        Book book = new Book();
        BookRating existingRating = new BookRating();

        when(userRepository.findByUsername("samira")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByUserAndBook(user, book)).thenReturn(true);
        when(borrowRecordRepository.existsByUserAndBookAndNotReturned(1L, 1L)).thenReturn(false);
        when(ratingRepository.findByBookIdAndUserId(1L, 1L)).thenReturn(Optional.of(existingRating));

        assertThrows(RuntimeException.class,
                () -> bookRatingService.rateBook(dto, "samira"),
                "You have already rated this book");
    }
}
