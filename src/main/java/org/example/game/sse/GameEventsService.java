package org.example.game.sse;

import org.example.game.dto.GameStateResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class GameEventsService {

    private final Map<String, List<SseEmitter>> emittersByGameId = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String gameId, GameStateResponse initialState) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        emittersByGameId
                .computeIfAbsent(gameId, ignored -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> removeEmitter(gameId, emitter));
        emitter.onTimeout(() -> removeEmitter(gameId, emitter));
        emitter.onError(error -> removeEmitter(gameId, emitter));

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("state")
                            .data(initialState, MediaType.APPLICATION_JSON)
            );
        } catch (IOException exception) {
            removeEmitter(gameId, emitter);
        }

        return emitter;
    }

    public void broadcast(String gameId, GameStateResponse state) {
        List<SseEmitter> emitters = emittersByGameId.getOrDefault(gameId, List.of());

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("state")
                                .data(state, MediaType.APPLICATION_JSON)
                );
            } catch (IOException exception) {
                removeEmitter(gameId, emitter);
            }
        }
    }

    private void removeEmitter(String gameId, SseEmitter emitter) {
        List<SseEmitter> emitters = emittersByGameId.get(gameId);

        if (emitters == null) {
            return;
        }

        emitters.remove(emitter);

        if (emitters.isEmpty()) {
            emittersByGameId.remove(gameId);
        }
    }
}