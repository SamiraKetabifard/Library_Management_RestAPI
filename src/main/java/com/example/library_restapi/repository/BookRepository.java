package com.example.library_restapi.repository;

import com.example.library_restapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(String author);

    //Native query to find books with average rating > 4
    @Query(value =
            "SELECT b.* FROM book b"+
            "JOIN book_rating r ON b.id = r.book_id"+
            "GROUP BY b.id"+
            "HAVING AVG(r.rate) > 4"
            , nativeQuery = true)
    List<Book> findHighlyRatedBooks();

    //Native query to count books by category
    @Query(value = """
            SELECT book_category as category, COUNT(*) as count 
            FROM book 
            GROUP BY book_category
            """,
            nativeQuery = true)
    List<Object[]> countBooksByCategory();
}
