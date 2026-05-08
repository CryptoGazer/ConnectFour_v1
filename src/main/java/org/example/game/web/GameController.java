package org.example.game.web;

import org.example.game.dto.CreateGameRequest;
import org.example.game.dto.GameStateResponse;
import org.example.game.dto.MoveRequest;
import org.example.game.service.GameService;
import org.example.game.sse.GameEventsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final GameEventsService gameEventsService;

    public GameController(
            GameService gameService,
            GameEventsService gameEventsService
    ) {
        this.gameService = gameService;
        this.gameEventsService = gameEventsService;
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
        GameStateResponse state = gameService.makeMove(gameId, request.column());

        gameEventsService.broadcast(gameId, state);

        return state;
    }

    @GetMapping(
            value = "/{gameId}/events",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter subscribe(@PathVariable String gameId) {
        GameStateResponse currentState = gameService.getGame(gameId);

        return gameEventsService.subscribe(gameId, currentState);
    }
}