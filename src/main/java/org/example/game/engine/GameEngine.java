package org.example.game.engine;

import org.example.game.domain.Game;
import org.example.game.domain.GameStatus;
import org.example.game.domain.Player;
import org.springframework.stereotype.Component;

@Component
public class GameEngine {

    public Game createGame(String id, int rows, int columns, int winCondition) {
        validateConfig(rows, columns, winCondition);
        return new Game(id, rows, columns, winCondition);
    }

    public void makeMove(Game game, int column) {
        if (game.getStatus() != GameStatus.RUNNING) {
            throw new IllegalStateException("Game is already finished");
        }

        if (column < 0 || column >= game.getColumns()) {
            throw new IllegalArgumentException("Column index is out of bounds");
        }

        int row = game.getNextFreeRow(column);

        if (row == -1) {
            throw new IllegalArgumentException("Column is full");
        }

        Player currentPlayer = game.getCurrentPlayer();

        game.placePiece(row, column, currentPlayer);

        if (isWinningMove(game, row, column, currentPlayer)) {
            game.finish(currentPlayer);
            return;
        }

        if (game.isFull()) {
            game.draw();
            return;
        }

        game.switchPlayer();
    }

    private void validateConfig(int rows, int columns, int winCondition) {
        if (rows < 4 || columns < 4) {
            throw new IllegalArgumentException("Rows and columns must be at least 4");
        }

        if (rows > 10 || columns > 10) {
            throw new IllegalArgumentException("Maximum field size is 10 x 10");
        }

        if (winCondition < 4) {
            throw new IllegalArgumentException("Win condition must be at least 4");
        }

        if (winCondition > 10) {
            throw new IllegalArgumentException("Win condition must be at most 10");
        }

        if (winCondition > Math.max(rows, columns)) {
            throw new IllegalArgumentException("Win condition is impossible for this field size");
        }
    }

    private boolean isWinningMove(Game game, int row, int column, Player player) {
        int[][] directions = {
                {0, 1},
                {1, 0},
                {1, 1},
                {1, -1}
        };

        for (int[] direction : directions) {
            int rowStep = direction[0];
            int columnStep = direction[1];

            int count = 1;

            count += countPieces(game, row, column, rowStep, columnStep, player);
            count += countPieces(game, row, column, -rowStep, -columnStep, player);

            if (count >= game.getWinCondition()) {
                return true;
            }
        }

        return false;
    }

    private int countPieces(
            Game game,
            int row,
            int column,
            int rowStep,
            int columnStep,
            Player player
    ) {
        int count = 0;

        int currentRow = row + rowStep;
        int currentColumn = column + columnStep;

        while (
                currentRow >= 0 &&
                        currentRow < game.getRows() &&
                        currentColumn >= 0 &&
                        currentColumn < game.getColumns() &&
                        game.getBoard()[currentRow][currentColumn] == player
        ) {
            count++;

            currentRow += rowStep;
            currentColumn += columnStep;
        }

        return count;
    }
}