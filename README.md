# Connect Four

A web-based Connect Four game with a configurable board size and win condition.

**Stack:** Spring Boot (Java 21) · Kotlin/JS · Compose HTML · SSE

---

## Prerequisites

| Tool | Version |
|------|---------|
| JDK  | 21+     |

> Node.js is **not required** — the Kotlin/JS Gradle plugin downloads it automatically on first build.

---

## Quick Start

The compiled frontend is already included in the repository, so a single command is enough:

```bash
./gradlew bootRun
```

Then open **http://localhost:8080** in your browser.

---

## Building the Frontend

Run this whenever you modify anything under `frontend/src/`:

```bash
./gradlew :frontend:copyToBackend   # build JS bundle and copy to backend static dir
./gradlew bootRun                   # start the server
```

> The first frontend build downloads Node.js and npm packages (~1–2 min). Subsequent builds are fast.

### Development mode (hot reload)

Run both commands in separate terminals:

```bash
# Terminal 1 — backend
./gradlew bootRun

# Terminal 2 — frontend dev server with proxy to :8080
./gradlew :frontend:jsBrowserDevelopmentRun
```

Open **http://localhost:3000**. The webpack dev server proxies all `/api/**` requests to Spring Boot.

---

## Running Tests

```bash
./gradlew test
```

Tests cover the core game engine: horizontal/vertical/diagonal win detection, draw detection, move validation, and config validation.

---

## API Reference

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/games` | Create a game. Body: `{"rows":6,"columns":7,"winCondition":4}` |
| `GET`  | `/api/games/{id}` | Get current game state |
| `POST` | `/api/games/{id}/moves` | Make a move. Body: `{"column":3}` |
| `GET`  | `/api/games/{id}/events` | SSE stream — emits `state` events after each move |

**Board constraints:** rows and columns 2–100; win condition 4–10 (must not exceed the largest dimension).

---

## Project Structure

```
src/main/java/org/example/
├── game/
│   ├── domain/         Game, Player, GameStatus
│   ├── engine/         GameEngine  — pure game logic
│   ├── repository/     GameRepository + InMemoryGameRepository
│   ├── service/        GameService
│   ├── sse/            GameEventsService
│   └── web/            GameController (REST + SSE)
└── config/             WebConfig (CORS)

frontend/src/jsMain/kotlin/connectfour/
├── api/                HTTP helpers, SSE external declaration
├── model/              GameState (serializable)
├── ui/                 SetupScreen, GameScreen, BoardUi
├── App.kt              Root composable + localStorage persistence
└── main.kt             Entry point
```

---

## Features

- Configurable board size (e.g. 10×10, 15×15)
- Configurable win condition (Connect 4 through Connect 10)
- Gravity-based piece placement
- Two-player local multiplayer (alternating turns)
- Win and draw detection
- Real-time updates via SSE — both players share one URL
- **Persistence:** game survives a browser refresh via `localStorage`
- Falling piece CSS animation
