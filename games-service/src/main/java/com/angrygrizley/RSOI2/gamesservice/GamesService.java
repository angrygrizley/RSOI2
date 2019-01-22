package com.angrygrizley.RSOI2.gamesservice;

import com.angrygrizley.RSOI2.gamesservice.Game;
import com.angrygrizley.RSOI2.gamesservice.GameNotFoundException;

import java.util.List;

public interface GamesService {
    List<Game> getAllGames();
    void createGame(Game game);
    void setReviewsNum(Long id, int reviewsNum) throws GameNotFoundException;
    Game getGameById(Long id) throws GameNotFoundException;
    int getReviewsNum(Long id) throws GameNotFoundException;
    void addReview(Long id) throws GameNotFoundException;
    void deleteReview(Long id) throws GameNotFoundException;
    void setRating(Long id, double rating) throws GameNotFoundException;
}