package com.example.library_restapi.repository;

import com.example.library_restapi.entity.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRatingRepository extends JpaRepository<BookRating, Long> {

    Optional<BookRating> findByBookIdAndUserId(Long bookId, Long userId);
}

