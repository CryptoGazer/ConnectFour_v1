package org.example.game.domain;

import java.util.Arrays;

public class Game {

    private final String id;
    private final int rows;
    private final int columns;
    private final int winCondition;

    private final Player[][] board;
    private final int[] nextFreeRows;

    private Player currentPlayer = Player.X;
    private GameStatus status = GameStatus.RUNNING;
    private Player winner;
    private int movesCount = 0;

    public Game(String id, int rows, int columns, int winCondition) {
        this.id = id;
        this.rows = rows;
        this.columns = columns;
        this.winCondition = winCondition;
        this.board = new Player[rows][columns];
        this.nextFreeRows = new int[columns];

        Arrays.fill(this.nextFreeRows, rows - 1);
    }

    public int getNextFreeRow(int column) {
        return nextFreeRows[column];
    }

    public void placePiece(int row, int column, Player player) {
        board[row][column] = player;
        nextFreeRows[column]--;
        movesCount++;
    }

    public boolean isFull() {
        return movesCount == rows * columns;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.next();
    }

    public void finish(Player winner) {
        this.status = GameStatus.FINISHED;
        this.winner = winner;
    }

    public void draw() {
        this.status = GameStatus.DRAW;
    }

    public String getId() {
        return id;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getWinCondition() {
        return winCondition;
    }

    public Player[][] getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }

    public int getMovesCount() {
        return movesCount;
    }
}