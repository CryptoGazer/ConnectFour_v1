package org.example.sandbox;

import java.util.Arrays;
import java.util.Scanner;

public class NaiveMain {
    static final int COLS = 7;
    static final int ROWS = 6;
    static final int WIN_CONDITION = 4;

    static final int SQUARE = COLS * ROWS;

    static Character[][] field = new Character[ROWS][COLS];

    // nextFreeRow[col] is the row index where the next piece in this column will fall
    static int[] nextFreeRow = new int[COLS];

    public static void main(String[] args) {
        validateConfig();

        Arrays.fill(nextFreeRow, ROWS - 1);

        Scanner scanner = new Scanner(System.in);
        int turnCounter = 0;

        printField(field);

        while (true) {
            if (turnCounter == SQUARE) {
                System.out.println("\n=== DRAW ===\n");
                break;
            }

            char currentPiece = turnCounter % 2 == 0 ? 'X' : 'O';

            System.out.println("Player " + currentPiece + ", enter column index from 0 to " + (COLS - 1) + ": ");

            if (!scanner.hasNextInt()) {
                System.out.println("\n=== PLEASE ENTER A NUMBER ===\n");
                scanner.next();
                continue;
            }

            int colIdx = scanner.nextInt();

            if (colIdx < 0 || colIdx >= COLS) {
                System.out.println("\n=== WRONG COLUMN INDEX " + colIdx + " ===\n");
                continue;
            }

            int rowIdx = nextFreeRow[colIdx];

            if (rowIdx == -1) {
                System.out.println("\n=== COLUMN " + colIdx + " IS FULL ===\n");
                continue;
            }

            field[rowIdx][colIdx] = currentPiece;
            nextFreeRow[colIdx]--;
            turnCounter++;

            printField(field);

            if (isWinningMove(rowIdx, colIdx, currentPiece)) {
                System.out.println("\n=== PLAYER " + currentPiece + " WON ===\n");
                break;
            }
        }
    }

    static void validateConfig() {
        if (COLS <= 0 || ROWS <= 0) {
            throw new IllegalArgumentException("Rows and columns must be positive");
        }

        if (COLS > 10 || ROWS > 10) {
            throw new IllegalArgumentException("Maximum field size is 10 x 10");
        }

        if (WIN_CONDITION < 4) {
            throw new IllegalArgumentException("Win condition must be at least 4");
        }

        if (WIN_CONDITION > 10) {
            throw new IllegalArgumentException("Win condition must be at most 10");
        }

        if (WIN_CONDITION > Math.max(ROWS, COLS)) {
            throw new IllegalArgumentException("Win condition is impossible for this field size");
        }
    }

    static boolean isWinningMove(int rowIdx, int colIdx, char currentPiece) {
        int[][] directions = {
                {0, 1},   // horizontal
                {1, 0},   // vertical
                {1, 1},   // diagonal \
                {1, -1}   // diagonal /
        };

        for (int[] direction : directions) {
            int rowStep = direction[0];
            int colStep = direction[1];

            int count = 1;

            count += countPieces(rowIdx, colIdx, rowStep, colStep, currentPiece);
            count += countPieces(rowIdx, colIdx, -rowStep, -colStep, currentPiece);

            if (count >= WIN_CONDITION) {
                return true;
            }
        }

        return false;
    }

    static int countPieces(int rowIdx, int colIdx, int rowStep, int colStep, char currentPiece) {
        int count = 0;

        int currentRow = rowIdx + rowStep;
        int currentCol = colIdx + colStep;

        while (
                currentRow >= 0 &&
                        currentRow < ROWS &&
                        currentCol >= 0 &&
                        currentCol < COLS &&
                        field[currentRow][currentCol] != null &&
                        field[currentRow][currentCol] == currentPiece
        ) {
            count++;

            currentRow += rowStep;
            currentCol += colStep;
        }

        return count;
    }

    static void printField(Character[][] field) {
        for (Character[] row : field) {
            for (Character piece : row) {
                char pieceToPrint = piece == null ? '_' : piece;
                System.out.print(pieceToPrint + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}
