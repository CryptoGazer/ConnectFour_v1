package org.example.game.repository;

import org.example.game.domain.Game;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameRepository implements GameRepository {

    private final Map<String, Game> store = new ConcurrentHashMap<>();

    @Override
    public void save(Game game) {
        store.put(game.getId(), game);
    }

    @Override
    public Optional<Game> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
