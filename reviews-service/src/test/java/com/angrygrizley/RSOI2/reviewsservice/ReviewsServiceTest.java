package com.angrygrizley.RSOI2.reviewsservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReviewsServiceTest {
    private ReviewsService reviewsService;

    @Mock
    ReviewsRepository reviewsRepository;

    @Before
    public void setUp(){
        initMocks(this);
        reviewsService = new ReviewsServiceImplementation(reviewsRepository);
    }

    @Test
    public void shouldGetReviews(){
        List<Review> reviews= new ArrayList<>();
        Review review = new Review();
        review.setGameId(4L);
        review.setUid(2L);
        review.setText("Awesome");

        given(reviewsRepository.findAll()).willReturn(reviews);
        List<Review> reviewsReturned = reviewsService.getAllReviews();
        assertThat(reviewsReturned, is(reviews));
    }

    @Test
    public void shouldGetReviewsByUser(){
        List<Review> reviews= new ArrayList<>();
        Review review = new Review();
        review.setGameId(4L);
        review.setUid(2L);
        review.setText("Awesome");

        given(reviewsRepository.findByUidOrderByPostedTimeDesc(2L)).willReturn(reviews);
        List<Review> reviewsReturned = reviewsService.getReviewsByUser(2L);
        assertThat(reviewsReturned, is(reviews));
    }

    @Test
    public void shouldGetReviewsByBook(){
        List<Review> reviews= new ArrayList<>();
        Review review = new Review();
        review.setGameId(4L);
        review.setUid(2L);
        review.setText("Awesome");

//        given(reviewsRepository.findByBookIdOrderByPostedTimeDesc(4L)).willReturn(reviews);
//        List<Review> reviewsReturned = reviewsService.getReviewsByBook(4L);
//        assertThat(reviewsReturned, is(reviews));
    }
}
