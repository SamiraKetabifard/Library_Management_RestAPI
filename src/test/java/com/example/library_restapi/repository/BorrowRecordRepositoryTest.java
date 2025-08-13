package com.example.library_restapi.repository;

import com.example.library_restapi.dto.BorrowRecordDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BorrowRecord;
import com.example.library_restapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BorrowRecordRepositoryTest {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private TestEntityManager entityManager;

        @Test
        void findAllBorrowRecordsAsDto_ReturnsRecords() {
            User user = new User();
            user.setUsername("hamin");
            entityManager.persist(user);

            Book book = new Book();
            book.setTitle("ai2");
            entityManager.persist(book);

            BorrowRecord record = new BorrowRecord();
            record.setUser(user);
            record.setBook(book);
            record.setBorrowDate(LocalDate.now());
            record.setDueDate(LocalDate.now().plusDays(14));
            record.setIsReturned(false);
            entityManager.persist(record);

            //  Explicitly flush and clear persistence context
            entityManager.flush();
            entityManager.clear();

            List<BorrowRecordDto> result = borrowRecordRepository.findAllBorrowRecordsAsDto();
            assertEquals(1, result.size());
            assertEquals("hamin", result.get(0).getUsername());
            assertEquals("ai2", result.get(0).getBookTitle());
        }
    }