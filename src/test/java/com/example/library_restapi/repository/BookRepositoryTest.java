package com.example.library_restapi.repository;

import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BookCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByAuthor_ReturnBooks() {

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("hafez");
        book1.setIsbnNumber("123");
        book1.setQuantity(1);
        book1.setIsAvailable(true);
        book1.setBookCategory(BookCategory.LITERATURE);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("hafez");
        book2.setIsbnNumber("456");
        book2.setQuantity(1);
        book2.setIsAvailable(true);
        book2.setBookCategory(BookCategory.LITERATURE);

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.flush();
        entityManager.clear();

        List<Book> result = bookRepository.findByAuthor("hafez");
        assertEquals(2, result.size());
    }
}