package org.example.game.repository;

import org.example.game.domain.Game;
import java.util.Optional;

public interface GameRepository {
    void save(Game game);
    Optional<Game> findById(String id);
}
