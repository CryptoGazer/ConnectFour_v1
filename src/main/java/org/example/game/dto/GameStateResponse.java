package org.example.game.dto;

public record GameStateResponse(
        String gameId,
        int rows,
        int columns,
        int winCondition,
        String[][] board,
        String currentPlayer,
        String status,
        String winner,
        int movesCount
) {

}