package connectfour.ui

import androidx.compose.runtime.*
import connectfour.model.GameState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun BoardUi(state: GameState, onColumnClick: (Int) -> Unit) {
    val cols = state.columns

    Div({
        style {
            display(DisplayStyle.InlineBlock)
            backgroundColor(Color("#0055cc"))
            padding(8.px)
            borderRadius(8.px)
        }
    }) {
        // Drop buttons row
        Div({
            style {
                display(DisplayStyle.Grid)
                property("grid-template-columns", "repeat($cols, 56px)")
                gap(4.px)
                marginBottom(4.px)
            }
        }) {
            for (col in 0 until cols) {
                Button({
                    onClick { onColumnClick(col) }
                    style {
                        width(56.px); height(28.px)
                        background("rgba(255,255,255,0.15)")
                        border(0.px, LineStyle.None, Color.transparent)
                        borderRadius(4.px)
                        cursor("pointer")
                        color(Color("white"))
                        fontSize(14.px)
                        property("transition", "background 0.15s")
                    }
                }) { Text("▼") }
            }
        }

        // Board cells
        Div({
            style {
                display(DisplayStyle.Grid)
                property("grid-template-columns", "repeat($cols, 56px)")
                gap(4.px)
            }
        }) {
            for (row in 0 until state.rows) {
                for (col in 0 until cols) {
                    val cell = state.board[row][col]
                    // key on cell value so Compose creates a new DOM node when a piece is placed,
                    // which re-triggers the CSS fall-in animation
                    key(cell ?: "empty-$row-$col") {
                        Div({
                            classes(listOfNotNull("board-cell", cell?.let { "piece-$it" }))
                        })
                    }
                }
            }
        }
    }
}
