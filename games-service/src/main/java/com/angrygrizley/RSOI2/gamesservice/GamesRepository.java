package com.angrygrizley.RSOI2.gamesservice;

import com.angrygrizley.RSOI2.gamesservice.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamesRepository extends JpaRepository<Game, Long> {
}
