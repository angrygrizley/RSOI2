package com.angrygrizley.RSOI2.reviewsservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ReviewsService {
    List<Review> getAllReviews();
    Long createReview(Review review);
    List<Review> getReviewsByUser(Long userId);
    Page<Review> getReviewsByGame(Long gameId, PageRequest p);
    List<Review> getReviewsByGame(Long gameId);
    void deleteReviewsByGame(Long gameId);
    void deleteById(Long id);
    Review getReviewById(Long id) throws ReviewNotFoundException;
}
