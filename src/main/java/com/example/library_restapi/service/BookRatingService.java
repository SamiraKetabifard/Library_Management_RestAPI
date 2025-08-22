package com.example.library_restapi.service;

import com.example.library_restapi.dto.RatingDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BookRating;
import com.example.library_restapi.entity.User;
import com.example.library_restapi.repository.BookRatingRepository;
import com.example.library_restapi.repository.BookRepository;
import com.example.library_restapi.repository.BorrowRecordRepository;
import com.example.library_restapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookRatingService {

    private final BookRatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Transactional
    public String rateBook(RatingDto ratingDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(ratingDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if user has borrowed this book at all
        boolean hasBorrowed = borrowRecordRepository.existsByUserAndBook(user, book);
        if (!hasBorrowed) {
            throw new RuntimeException("You haven't borrowed this book");
        }
        // Check if user has unreturned copy of this book
        boolean hasUnreturned = borrowRecordRepository.existsByUserAndBookAndNotReturned(user.getId(), book.getId());
        if (hasUnreturned) {
            throw new RuntimeException("You must return the book before rating it");
        }
        // Check if already rated
        if (ratingRepository.findByBookIdAndUserId(book.getId(), user.getId()).isPresent()) {
            throw new RuntimeException("You have already rated this book");
        }
        BookRating rating = new BookRating();
        rating.setRate(ratingDto.getRating());
        rating.setBook(book);
        rating.setUser(user);

        ratingRepository.save(rating);
        return "Book rated successfully";
    }
}