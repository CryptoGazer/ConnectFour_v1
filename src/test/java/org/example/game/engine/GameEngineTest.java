package org.example.game.engine;

import org.example.game.domain.Game;
import org.example.game.domain.GameStatus;
import org.example.game.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine();
    }

    // Win detection

    @Test
    void horizontalWin() {
        Game game = engine.createGame("1", 6, 7, 4);
        // X fills bottom of cols 0-3; O is parked above X in the same columns
        engine.makeMove(game, 0); // X (5,0)
        engine.makeMove(game, 0); // O (4,0)
        engine.makeMove(game, 1); // X (5,1)
        engine.makeMove(game, 1); // O (4,1)
        engine.makeMove(game, 2); // X (5,2)
        engine.makeMove(game, 2); // O (4,2)
        engine.makeMove(game, 3); // X (5,3) → 4 in a row

        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertEquals(Player.X, game.getWinner());
    }

    @Test
    void verticalWin() {
        Game game = engine.createGame("2", 6, 7, 4);
        // X stacks in col 0; O plays in col 1
        for (int i = 0; i < 3; i++) {
            engine.makeMove(game, 0); // X
            engine.makeMove(game, 1); // O
        }
        engine.makeMove(game, 0); // X → 4th piece in col 0

        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertEquals(Player.X, game.getWinner());
    }

    @Test
    void diagonalWin() {
        // 4x4 board; X wins the anti-diagonal (3,0) -> (2,1) -> (1,2) -> (0,3)
        Game game = engine.createGame("3", 4, 4, 4);

        engine.makeMove(game, 0); // X (3,0)
        engine.makeMove(game, 1); // O (3,1)
        engine.makeMove(game, 1); // X (2,1)
        engine.makeMove(game, 2); // O (3,2)
        engine.makeMove(game, 3); // X (3,3) – filler
        engine.makeMove(game, 2); // O (2,2)
        engine.makeMove(game, 2); // X (1,2)
        engine.makeMove(game, 3); // O (2,3)
        engine.makeMove(game, 3); // X (1,3) – filler
        engine.makeMove(game, 0); // O (2,0) – filler
        engine.makeMove(game, 3); // X (0,3) -> (3,0)(2,1)(1,2)(0,3) diagonal

        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertEquals(Player.X, game.getWinner());
    }

    @Test
    void draw() {
        // 5x2 board with win=4: columns alternate X/O so no 4-in-a-row is possible;
        // filling both columns exhausts the board, then draw
        Game game = engine.createGame("4", 5, 2, 4);
        for (int i = 0; i < 5; i++) engine.makeMove(game, 0); // fills col 0
        for (int i = 0; i < 5; i++) engine.makeMove(game, 1); // fills col 1

        assertEquals(GameStatus.DRAW, game.getStatus());
        assertNull(game.getWinner());
    }

    // Game-state guards

    @Test
    void moveRejectedAfterGameEnds() {
        Game game = engine.createGame("5", 6, 7, 4);
        for (int i = 0; i < 3; i++) {
            engine.makeMove(game, 0);
            engine.makeMove(game, 1);
        }
        engine.makeMove(game, 0); // X wins
        assertEquals(GameStatus.FINISHED, game.getStatus());

        assertThrows(IllegalStateException.class, () -> engine.makeMove(game, 2));
    }

    @Test
    void columnOutOfBounds() {
        Game game = engine.createGame("6", 6, 7, 4);
        assertThrows(IllegalArgumentException.class, () -> engine.makeMove(game, -1));
        assertThrows(IllegalArgumentException.class, () -> engine.makeMove(game, 7));
    }

    @Test
    void fullColumnRejected() {
        // 5-row board, win=4; both players alternate into col 0 →
        // pattern bottom-to-top: X O X O X → neither reaches 4 consecutive
        Game game = engine.createGame("7", 5, 7, 4);
        for (int i = 0; i < 5; i++) engine.makeMove(game, 0);

        assertThrows(IllegalArgumentException.class, () -> engine.makeMove(game, 0));
    }

    // Config validation

    @Test
    void invalidConfig_winConditionTooSmall() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.createGame("8", 6, 7, 3));
    }

    @Test
    void invalidConfig_winConditionTooLarge() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.createGame("9", 6, 7, 11));
    }

    @Test
    void invalidConfig_winConditionImpossible() {
        // win=5 is unreachable on a 4×4 board
        assertThrows(IllegalArgumentException.class,
                () -> engine.createGame("10", 4, 4, 5));
    }

    @Test
    void invalidConfig_zeroDimension() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.createGame("11", 0, 7, 4));
    }
}
