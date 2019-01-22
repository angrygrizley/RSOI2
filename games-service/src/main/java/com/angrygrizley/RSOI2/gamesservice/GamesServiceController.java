package com.angrygrizley.RSOI2.gamesservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class GamesServiceController {
    private final GamesService gamesService;
    Logger logger;

    @Autowired
    public GamesServiceController(GamesService gamesService){
        this.gamesService = gamesService;
        logger  = LoggerFactory.getLogger(GamesServiceImplementation.class);
    }

    @PostMapping(value = "/games")
    public void createGame(@RequestBody Game game){
        gamesService.createGame(game);
        logger.info("[POST] /games ", game);
    }

    @GetMapping(value = "/games")
    public List<Game> getAllGames(){
        logger.info("[GET] /games ");
        return gamesService.getAllGames();
    }

    @PostMapping(value = "/games/{id}/add_review")
    public void addReview(@PathVariable Long id) throws GameNotFoundException {
        gamesService.addReview(id);
        logger.info("[POST] /games/" + id + "/add_review");
    }

    @PostMapping(value = "/games/{id}/delete_review")
    public void deleteReview(@PathVariable Long id) throws GameNotFoundException {
        gamesService.deleteReview(id);
        logger.info("[POST] /games/" + id + "/delete_review");
    }

    @PostMapping(value = "games/{id}/setRating/{rating}")
    public void setRating(@PathVariable Long id, @PathVariable double rating) throws GameNotFoundException{
        gamesService.setRating(id, rating);
        logger.info("[POST] /games/" + id + "/serRating/" + rating);
    }

    @GetMapping(value = "/game/{id}")
    public Game getGameById(@PathVariable Long id) throws GameNotFoundException {
        logger.info("[GET] /game/" + id);
        return gamesService.getGameById(id);
    }

    @GetMapping(value = "/gamesbyrating")
    public List<Game> getGamesByRating(){
        logger.info("[GET] /games ");
        return gamesService.getGamesByRating();
    }

}
