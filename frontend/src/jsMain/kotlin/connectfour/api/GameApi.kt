package connectfour.api

import connectfour.model.GameState
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

suspend fun createGame(rows: Int, columns: Int, winCondition: Int): GameState {
    val body = """{"rows":$rows,"columns":$columns,"winCondition":$winCondition}"""
    return json.decodeFromString(apiPost("/api/games", body))
}

suspend fun fetchGame(gameId: String): GameState =
    json.decodeFromString(apiGet("/api/games/$gameId"))

suspend fun makeMove(gameId: String, column: Int): GameState {
    val body = """{"column":$column}"""
    return json.decodeFromString(apiPost("/api/games/$gameId/moves", body))
}
