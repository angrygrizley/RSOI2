package com.angrygrizley.RSOI2.gatewayservice;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@Slf4j
public class GatewayServiceController {
    private final GatewayService gatewayService;
    private Logger logger;

    @Autowired
    public GatewayServiceController(GatewayService gatewayService){
        logger = LoggerFactory.getLogger(GatewayServiceController.class);
        this.gatewayService = gatewayService;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsers() throws IOException{
        logger.info("[GET] /users");
        return gatewayService.getUsers();
    }

    @DeleteMapping(path = "/users/{userId}")
    public void deleteUser(@PathVariable Long userId) throws IOException{
        logger.info("[DELETE] /users/" + userId);
        gatewayService.deleteUser(userId);
    }

    @PostMapping(path = "/users")
    public void addUser(@RequestBody String user) throws IOException{
        logger.info("[POST] /users\n ", user);
        gatewayService.addUser(user);
    }

    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserById(@PathVariable Long userId) throws IOException {
        logger.info("[GET] /users/" +userId);
        return gatewayService.getUserById(userId);
    }

    @GetMapping(path = "/users/{userId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getReviewsByUser(@PathVariable Long userId) throws IOException {
        logger.info("[GET] /users/" + userId + "/reviews");
        return gatewayService.getReviewsByUser(userId);
    }

    @GetMapping(path = "/game/{gameId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getReviewsForGame(@PathVariable Long gameId,
                                    @RequestParam (value = "page") int page,
                                    @RequestParam (value = "size") int size) throws IOException{
        logger.info("[GET] /game/" + gameId + "/reviews/?page=" + page + "&size=" + size);
        PageRequest p;
        p = PageRequest.of(page, size);
        return gatewayService.getReviewsForGame(gameId, p);
    }

    @GetMapping(path = "/game/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGameById(@PathVariable Long gameId) throws IOException{
        logger.info("[GET] /game/" + gameId);
        return gatewayService.getGameById(gameId);
    }


    @GetMapping(path = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGamesWithReviews() throws IOException, JSONException {
        logger.info("[GET] /games");
        return gatewayService.getGamesWithReviews();
    }

    @PostMapping(value = "/reviews")
    public void createReview(@RequestBody String review) throws IOException {
        gatewayService.createReview(review);
        logger.info("[POST] /reviews ", "review: ", review);
    }

    @DeleteMapping(value = "/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) throws IOException {
        gatewayService.deleteReview(reviewId);
        logger.info("[DELETE] /reviews/" + reviewId);
    }

    @GetMapping(path = "/gamesbyrating", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGamesByRating() throws IOException {
        logger.info("[GET] /games");
        return gatewayService.getGamesByRating();
    }

    @PutMapping (path = "reviews/edit")
    public String editReview(@RequestBody String review) throws IOException {
        logger.info("[PUT] /reviews/edit");
        return gatewayService.editReview(review);
    }

    @PutMapping (path = "users/edit")
    public String editUser(@RequestBody String user) throws IOException {
        logger.info("[PUT] /users/edit");
        return gatewayService.editUser(user);
    }

    @PutMapping (path = "games/edit")
    public String editGame(@RequestBody String game) throws IOException {
        logger.info("[PUT] /games/edit");
        return gatewayService.editGame(game);
    }
}
