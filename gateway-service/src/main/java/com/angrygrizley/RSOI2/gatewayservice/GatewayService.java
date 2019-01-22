package com.angrygrizley.RSOI2.gatewayservice;

import org.json.JSONException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;


public interface GatewayService {
    String getUsers() throws IOException;
    String getUserById(Long userId) throws IOException;
    String getReviewsByUser(Long userId) throws IOException;
    String getGamesWithReviews() throws IOException, JSONException;
    String getReviewsForGame(Long gameId, PageRequest p) throws IOException;
    String getGameById(Long gameId) throws IOException;
    String getGamesByRating() throws IOException;
    void addUser(String user) throws IOException;
    void createReview(String review) throws IOException;
    void deleteReview(Long reviewId) throws IOException;
    void deleteUser(Long id) throws IOException;
}
