package com.example.library_restapi.repository;

import com.example.library_restapi.dto.BorrowRecordDto;
import com.example.library_restapi.dto.HistoryDto;
import com.example.library_restapi.entity.Book;
import com.example.library_restapi.entity.BorrowRecord;
import com.example.library_restapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
  //JPQL
  @Query("SELECT new com.example.library_restapi.dto.BorrowRecordDto" +
          "(br.id, u.username, b.title, br.isReturned) " +
          "FROM BorrowRecord br JOIN br.user u JOIN br.book b")
  List<BorrowRecordDto> findAllBorrowRecordsAsDto();

  @Query("SELECT new com.example.library_restapi.dto.HistoryDto" +
          "(b.title, br.borrowDate, br.dueDate, br.isReturned) " +
          "FROM BorrowRecord br JOIN br.book b WHERE br.user.id = :userId")
  List<HistoryDto> findUserHistory(@Param("userId") Long userId);

  //Check if this user has ever borrowed this specific book
  @Query("SELECT COUNT(br) > 0 FROM BorrowRecord br WHERE br.user = :user AND br.book = :book")
  boolean existsByUserAndBook(@Param("user") User user, @Param("book") Book book);

  //Check if this user currently has this specific book borrowed (not yet returned)
  @Query("SELECT COUNT(br) > 0 FROM BorrowRecord br WHERE br.user.id = :userId AND br.book.id = :bookId " +
          "AND br.isReturned = false")
  boolean existsByUserAndBookAndNotReturned(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
