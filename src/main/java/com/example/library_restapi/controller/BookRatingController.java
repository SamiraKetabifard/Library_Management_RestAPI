package com.example.library_restapi.controller;

import com.example.library_restapi.dto.RatingDto;
import com.example.library_restapi.service.BookRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class BookRatingController {

    private final BookRatingService bookRatingService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> rateBook(@RequestBody @Valid RatingDto ratingDto,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String result = bookRatingService.rateBook(ratingDto, userDetails.getUsername());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}