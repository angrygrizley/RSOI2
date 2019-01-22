package com.angrygrizley.RSOI2.gamesservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Service
public class GamesServiceImplementation implements GamesService {
    private final GamesRepository gamesRepository;
    @Autowired
    public GamesServiceImplementation(GamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }

    @Override
    public List<Game> getAllGames() {
        return gamesRepository.findAll();
    }

    @Override
    public void createGame(Game game) {
        gamesRepository.save(game);
    }



    @Override
    public void setReviewsNum(Long id, int reviewsNum) throws GameNotFoundException {
        Game game = gamesRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        game.setReviewsNum(reviewsNum);
        gamesRepository.save(game);
    }

    @Override
    public int getReviewsNum(Long id) throws GameNotFoundException{
        Game game = gamesRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        return game.getReviewsNum();
    }

    @Override
    public void addReview(Long id) throws GameNotFoundException {
        Game game = gamesRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        game.setReviewsNum(game.getReviewsNum() + 1);
        gamesRepository.save(game);
    }

    @Override
    public void deleteReview(Long id) throws GameNotFoundException {
        Game game = gamesRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        game.setReviewsNum(game.getReviewsNum() - 1);
        gamesRepository.save(game);
    }

    @Override
    public void setRating(Long id, double rating) throws GameNotFoundException {
        Game game = gamesRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        game.setRating(rating);
        gamesRepository.save(game);
    }

    @Override
    public Game getGameById(@PathVariable Long id) throws GameNotFoundException{
        return gamesRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    @Override
    public List<Game> getGamesByRating() {
        return gamesRepository.findAllOrderByRating();
    }
}
