package com.angrygrizley.RSOI2.gamesservice;

import com.angrygrizley.RSOI2.gamesservice.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {
    public List<Game> findAllByOrderByRating();
}
