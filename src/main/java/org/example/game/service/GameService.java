package org.example.game.service;

import org.example.game.domain.Game;
import org.example.game.domain.Player;
import org.example.game.dto.GameStateResponse;
import org.example.game.engine.GameEngine;
import org.example.game.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class GameService {

    private final GameEngine gameEngine;
    private final GameRepository gameRepository;

    public GameService(GameEngine gameEngine, GameRepository gameRepository) {
        this.gameEngine = gameEngine;
        this.gameRepository = gameRepository;
    }

    public GameStateResponse createGame(int rows, int columns, int winCondition) {
        try {
            String id = UUID.randomUUID().toString();
            Game game = gameEngine.createGame(id, rows, columns, winCondition);
            gameRepository.save(game);
            return toResponse(game);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public GameStateResponse getGame(String gameId) {
        return toResponse(findGame(gameId));
    }

    public GameStateResponse makeMove(String gameId, int column) {
        Game game = findGame(gameId);
        // synchronize per-game to prevent concurrent moves corrupting board/nextFreeRows
        synchronized (game) {
            try {
                gameEngine.makeMove(game, column);
                return toResponse(game);
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }

    private Game findGame(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
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
            for (int col = 0; col < board[row].length; col++) {
                result[row][col] = board[row][col] == null ? null : board[row][col].name();
            }
        }
        return result;
    }
}
