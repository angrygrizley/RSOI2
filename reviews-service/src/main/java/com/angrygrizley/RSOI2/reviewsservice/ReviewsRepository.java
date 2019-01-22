package com.angrygrizley.RSOI2.reviewsservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Review, Long> {
    List<Review> findByUidOrderByPostedTimeDesc(Long uid);
    List<Review> findByGameIdOrderByPostedTimeDesc(Long gameId);
    Page<Review> findByGameIdOrderByPostedTimeDesc(Long gameId, Pageable p);
    void deleteAllByGameId(Long gameId);
}
