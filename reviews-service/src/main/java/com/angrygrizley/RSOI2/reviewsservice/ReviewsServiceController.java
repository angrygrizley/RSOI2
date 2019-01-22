package com.angrygrizley.RSOI2.reviewsservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ReviewsServiceController {
    private ReviewsService reviewsService;
    private Logger logger;

    @Autowired
    ReviewsServiceController(ReviewsService reviewsService){
        this.reviewsService = reviewsService;
        logger = LoggerFactory.getLogger(ReviewsServiceController.class);
    }

    @PostMapping(value = "/reviews")
    public Long createReview(@RequestBody Review review){
        logger.info("[POST] /reviews", review);
        return reviewsService.createReview(review);
    }

    @GetMapping(value = "reviews/{id}")
    public Review getReviewById(@PathVariable Long id) throws ReviewNotFoundException {
        logger.info("[GET] /reviews/" + id);
        return reviewsService.getReviewById(id);
    }

    @GetMapping(value = "/reviews")
    public List<Review> getAllReviews(){
        logger.info("[GET] /reviews");
        return reviewsService.getAllReviews();
    }

    @GetMapping(value = "/reviews/byuser/{userId}")
    public List<Review> getReviewsByUser(@PathVariable Long userId){
        logger.info("[GET] /reviews/byuser/" + userId);
        return reviewsService.getReviewsByUser(userId);
    }

    @GetMapping(value = "/reviews/bygame/{gameId}")
    public Object getReviewsByGame(@PathVariable Long gameId,
                                         @RequestParam (value = "page", required = false) Integer page,
                                         @RequestParam (value = "size", required = false) Integer size){
        if (page != null && size != null) {
            logger.info("[GET] /reviews/bygame/" + gameId + " page= " + page + ", size= " + size);
            PageRequest p = PageRequest.of(page, size);
            return reviewsService.getReviewsByGame(gameId, p);
        }
        else{
            logger.info("[GET] /reviews/bygame/");
            return reviewsService.getReviewsByGame(gameId);
        }
    }

    @DeleteMapping(value = "reviews/{id}")
    public void deleteReviewById(@PathVariable Long id) {
        logger.info("[DELETE] /reviews/" + id);
        reviewsService.deleteById(id);
    }

    @DeleteMapping(value = "reviews/bygame/{gameId}")
    public void deleteReviewsByGame(@PathVariable Long gameId) {
        logger.info("[DELETE] /reviews/bygame/" + gameId);
        reviewsService.deleteReviewsByGame(gameId);
    }
}
