package org.example.game.service;

import org.example.game.domain.Game;
import org.example.game.domain.Player;
import org.example.game.dto.GameStateResponse;
import org.example.game.engine.GameEngine;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final GameEngine gameEngine;
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public GameService(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public GameStateResponse createGame(int rows, int columns, int winCondition) {
        try {
            String id = UUID.randomUUID().toString();

            Game game = gameEngine.createGame(id, rows, columns, winCondition);
            games.put(id, game);

            return toResponse(game);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    public GameStateResponse getGame(String gameId) {
        Game game = findGame(gameId);
        return toResponse(game);
    }

    public GameStateResponse makeMove(String gameId, int column) {
        Game game = findGame(gameId);

        try {
            gameEngine.makeMove(game, column);
            return toResponse(game);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    private Game findGame(String gameId) {
        Game game = games.get(gameId);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        return game;
    }

    private GameStateResponse toResponse(Game game) {
        return new GameStateResponse(
                game.getId(),
                game.getRows(),
                game.getColumns(),
                game.getWinCondition(),
                mapBoard(game.getBoard()),
                game.getCurrentPlayer().name(),
                game.getStatus().name(),
                game.getWinner() == null ? null : game.getWinner().name(),
                game.getMovesCount()
        );
    }

    private String[][] mapBoard(Player[][] board) {
        String[][] result = new String[board.length][board[0].length];

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                result[row][column] = board[row][column] == null
                        ? null
                        : board[row][column].name();
            }
        }

        return result;
    }
}