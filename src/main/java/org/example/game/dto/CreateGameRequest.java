package org.example.game.dto;

public record CreateGameRequest(
        int rows,
        int columns,
        int winCondition
) {

}