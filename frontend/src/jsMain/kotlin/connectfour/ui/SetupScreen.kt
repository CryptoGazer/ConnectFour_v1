package connectfour.ui

import androidx.compose.runtime.*
import connectfour.api.createGame
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun SetupScreen(onGameCreated: (String) -> Unit) {
    var rows by remember { mutableStateOf(6) }
    var columns by remember { mutableStateOf(7) }
    var winCondition by remember { mutableStateOf(4) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            gap(16.px)
            padding(40.px)
        }
    }) {
        H1 { Text("Connect Four") }

        ConfigRow("Rows") {
            NumberInput(rows, attrs = {
                attr("min", "2"); attr("max", "100")
                onInput { rows = it.value?.toInt() ?: rows }
                style { width(100.px) }
            })
        }

        ConfigRow("Columns") {
            NumberInput(columns, attrs = {
                attr("min", "2"); attr("max", "100")
                onInput { columns = it.value?.toInt() ?: columns }
                style { width(100.px) }
            })
        }

        ConfigRow("Win condition (4–10)") {
            NumberInput(winCondition, attrs = {
                attr("min", "4"); attr("max", "10")
                onInput { winCondition = it.value?.toInt() ?: winCondition }
                style { width(100.px) }
            })
        }

        error?.let {
            Span({ style { color(Color("tomato")) } }) { Text(it) }
        }

        Button({
            if (loading) disabled()
            onClick {
                loading = true
                error = null
                MainScope().launch {
                    try {
                        val game = createGame(rows, columns, winCondition)
                        onGameCreated(game.gameId)
                    } catch (e: Throwable) {
                        error = e.message ?: "Failed to create game"
                        loading = false
                    }
                }
            }
            style { padding(10.px, 24.px); fontSize(16.px); cursor("pointer") }
        }) {
            Text(if (loading) "Creating…" else "Start Game")
        }
    }
}

@Composable
private fun ConfigRow(label: String, content: @Composable () -> Unit) {
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            gap(12.px)
        }
    }) {
        Label { Text(label) }
        content()
    }
}
