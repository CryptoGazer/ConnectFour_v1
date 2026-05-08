package connectfour

import androidx.compose.runtime.*
import connectfour.ui.GameScreen
import connectfour.ui.SetupScreen
import kotlinx.browser.localStorage

@Composable
fun App() {
    // On load, try to resume a saved game from localStorage
    var gameId by remember { mutableStateOf(localStorage.getItem("gameId")) }

    if (gameId == null) {
        SetupScreen { newId ->
            localStorage.setItem("gameId", newId)
            gameId = newId
        }
    } else {
        GameScreen(
            gameId = gameId!!,
            onNewGame = {
                localStorage.removeItem("gameId")
                gameId = null
            }
        )
    }
}
