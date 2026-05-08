package connectfour.ui

import androidx.compose.runtime.*
import connectfour.api.EventSource
import connectfour.api.makeMove
import connectfour.model.GameState
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

private val json = Json { ignoreUnknownKeys = true }

@Composable
fun GameScreen(gameId: String, onNewGame: () -> Unit) {
    var state by remember { mutableStateOf<GameState?>(null) }
    var gameNotFound by remember { mutableStateOf(false) }

    DisposableEffect(gameId) {
        var errorStreak = 0
        val es = EventSource("/api/games/$gameId/events")

        es.addEventListener("state") { event ->
            errorStreak = 0
            state = json.decodeFromString(event.data)
        }

        // If the game doesn't exist (server restarted), EventSource gets 404 and keeps retrying.
        // After a few consecutive failures with no state received, we give up.
        es.onerror = { _ ->
            if (state == null) {
                errorStreak++
                if (errorStreak >= 3) {
                    es.close()
                    gameNotFound = true
                }
            }
        }

        onDispose { es.close() }
    }

    if (gameNotFound) {
        LaunchedEffect(Unit) { onNewGame() }
        return
    }

    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            gap(20.px)
            padding(24.px)
        }
    }) {
        H1 { Text("Connect Four") }

        val gs = state
        if (gs == null) {
            P { Text("Connecting…") }
        } else {
            // Status bar
            val statusText = when (gs.status) {
                "FINISHED" -> "Player ${gs.winner} wins!"
                "DRAW"     -> "It's a draw!"
                else       -> "Player ${gs.currentPlayer}'s turn"
            }
            P({ style { fontSize(20.px); fontWeight("bold") } }) { Text(statusText) }

            BoardUi(gs) { col ->
                if (gs.status == "RUNNING") {
                    MainScope().launch {
                        try {
                            makeMove(gameId, col)
                            // SSE broadcasts the new state — no manual update needed
                        } catch (e: Throwable) {
                            window.alert(e.message ?: "Invalid move")
                        }
                    }
                }
            }

            if (gs.status != "RUNNING") {
                Button({
                    onClick { onNewGame() }
                    style { padding(10.px, 24.px); fontSize(16.px); cursor("pointer"); marginTop(8.px) }
                }) {
                    Text("New Game")
                }
            }
        }
    }
}
