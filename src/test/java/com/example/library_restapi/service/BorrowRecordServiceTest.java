package com.example.library_restapi.service;

import com.example.library_restapi.dto.HistoryDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BorrowRecord;
import com.example.library_restapi.entity.User;
import com.example.library_restapi.repository.BookRepository;
import com.example.library_restapi.repository.BorrowRecordRepository;
import com.example.library_restapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowRecordServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowRecordService borrowRecordService;

    @Test
    void borrowTheBook_AvailableBook_Success() {
        Book book = new Book();
        book.setId(1L);
        book.setQuantity(5);
        book.setIsAvailable(true);

        User user = new User();
        user.setUsername("samira");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findByUsername("samira")).thenReturn(Optional.of(user));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).
                thenAnswer(inv -> inv.getArgument(0));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("samira", null)
        );

        BorrowRecord result = borrowRecordService.borrowTheBook(1L);

        assertNotNull(result);
        assertEquals(4, book.getQuantity());
        assertFalse(result.getIsReturned());
    }

    @Test
    void borrowTheBook_UnavailableBook_ThrowsException() {
        Book book = new Book();
        book.setQuantity(0);
        book.setIsAvailable(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(RuntimeException.class, () -> borrowRecordService.borrowTheBook(1L));
    }

    @Test
    void returnTheBook_NotReturned_Success() {
        BorrowRecord record = new BorrowRecord();
        record.setIsReturned(false);

        Book book = new Book();
        book.setQuantity(5);

        record.setBook(book);

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(record);

        BorrowRecord result = borrowRecordService.returnTheBook(1L);

        assertTrue(result.getIsReturned());
        assertNotNull(result.getReturnDate());
        assertEquals(6, book.getQuantity());
    }

    @Test
    void getBorrowHistory_ReturnsUserRecords() {
        User user = new User();
        user.setId(1L);

        HistoryDto dto = new HistoryDto("Book", LocalDate.now(),
                LocalDate.now().plusDays(14), false);

        when(userRepository.findByUsername("samira")).thenReturn(Optional.of(user));
        when(borrowRecordRepository.findUserHistory(1L)).thenReturn(List.of(dto));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("samira", null));

        List<HistoryDto> result = borrowRecordService.getBorrowHistory();

        assertEquals(1, result.size());
        assertEquals("Book", result.get(0).getBookTitle());
    }
}
