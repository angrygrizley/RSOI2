package com.angrygrizley.RSOI2.gamesservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GamesServiceTest {
    private GamesService gamesService;

    @Mock
    GamesRepository gamesRepository;

    @Before
    public void setUp(){
        initMocks(this);
        gamesService = new GamesServiceImplementation(gamesRepository);
    }

    @Test
    public void shouldCreateNewGame(){
        Game game = new Game();
        game.setName("game");

        gamesRepository.save(game);
    }

    @Test
    public void shouldReturnGamesList(){
        List<Game> games = new ArrayList<>();
        Game game = new Game();
        game.setName("game");
        games.add(game);

        given(gamesRepository.findAll()).willReturn(games);
        List<Game> gamesReturned = gamesService.getAllGames();
        assertThat(gamesReturned, is(games));
    }

    @Test
    public void shouldSetReviewsNum(){
        Game game = new Game();
        game.setName("game");
        game.setReviewsNum(5);

        try {
            given(gamesRepository.save(game)).willReturn(game);
            Game gamesaved = gamesRepository.save(game);
            given(gamesRepository.findById(gamesaved.getId())).willReturn(Optional.of(gamesaved));

            int revs_num = gamesService.getReviewsNum(gamesaved.getId());
            gamesService.setReviewsNum(gamesaved.getId(), 10);
            assertEquals(10, gamesService.getReviewsNum(gamesaved.getId()));
        }
        catch (GameNotFoundException ex){
            fail();
        }
    }

    @Test
    public void shouldAddReview(){
        Game game = new Game();
        game.setName("game");
        game.setReviewsNum(5);

        try {
            given(gamesRepository.save(game)).willReturn(game);
            given(gamesRepository.findById(game.getId())).willReturn(Optional.of(game));
            Game gamesaved = gamesRepository.save(game);

            gamesService.addReview(gamesaved.getId());
            assertEquals(6, gamesService.getReviewsNum(gamesaved.getId()));
        }
        catch (GameNotFoundException ex){
            fail();
        }
    }

}
