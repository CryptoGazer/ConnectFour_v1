package org.example.game.web;

import org.example.game.dto.CreateGameRequest;
import org.example.game.dto.GameStateResponse;
import org.example.game.dto.MoveRequest;
import org.example.game.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public GameStateResponse createGame(@RequestBody CreateGameRequest request) {
        return gameService.createGame(
                request.rows(),
                request.columns(),
                request.winCondition()
        );
    }

    @GetMapping("/{gameId}")
    public GameStateResponse getGame(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping("/{gameId}/moves")
    public GameStateResponse makeMove(
            @PathVariable String gameId,
            @RequestBody MoveRequest request
    ) {
        return gameService.makeMove(gameId, request.column());
    }
}