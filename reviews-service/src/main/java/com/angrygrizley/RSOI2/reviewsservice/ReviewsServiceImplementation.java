package com.angrygrizley.RSOI2.reviewsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@Service
public class ReviewsServiceImplementation implements ReviewsService {
    private final ReviewsRepository reviewsRepository;

    @Autowired
    public ReviewsServiceImplementation(ReviewsRepository reviewsRepository){
        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewsRepository.findAll();
    }

    @Override
    public Long createReview(Review review) {
        review.setPostedTime(Calendar.getInstance().getTime().getTime());
        reviewsRepository.save(review);
        return review.getId();
    }

    @Override
    public List<Review> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewsRepository.findByUidOrderByPostedTimeDesc(userId);
        return reviews;
    }


    @Override
    @Transactional
    public Page<Review> getReviewsByGame(Long gameId, PageRequest p) {
        return reviewsRepository.findByGameIdOrderByPostedTimeDesc(gameId, p);
    }

    @Override
    @Transactional
    public List<Review> getReviewsByGame(Long gameId) {
        return reviewsRepository.findByGameIdOrderByPostedTimeDesc(gameId);
    }


    @Override
    public void deleteReviewsByGame(Long gameId) {
        reviewsRepository.deleteAllByGameId(gameId);
    }

    @Override
    public void deleteById(Long id) {
        reviewsRepository.deleteById(id);
    }

    @Override
    public Review getReviewById(Long id) throws ReviewNotFoundException {
        return reviewsRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public Review putReview(Review newReview) throws ReviewNotFoundException {
        return reviewsRepository.findById(newReview.getId()).map(Review -> {
            Review.setPostedTime(newReview.getPostedTime());
            Review.setRating(newReview.getRating());
            Review.setText(newReview.getText());
            Review.setUid(newReview.getUid());
            return reviewsRepository.save(Review);
        }).orElseThrow(() -> new ReviewNotFoundException(newReview.getId()));
    }
}