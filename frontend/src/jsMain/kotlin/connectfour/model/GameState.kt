package connectfour.model

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val gameId: String,
    val rows: Int,
    val columns: Int,
    val winCondition: Int,
    val board: List<List<String?>>,
    val currentPlayer: String,
    val status: String,
    val winner: String?,
    val movesCount: Int
)
