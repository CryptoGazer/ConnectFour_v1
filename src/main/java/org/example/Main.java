package org.example;

import java.util.*;

public class Main {
    // TestValues
    static int COLS = 7;
    static int ROWS = 6;
    static int WIN_CONDITION = 4;  // default for ConnectFour

    static int SQUARE = COLS * ROWS;

    static Character[][] field = new Character[ROWS][COLS];
    static HashMap<Integer, Integer> idxToMax = new HashMap<>();

    static void main() {
        assert WIN_CONDITION <= ROWS &&
                WIN_CONDITION >= 4 &&
                WIN_CONDITION <= 10 &&
                COLS >= 7 &&
                ROWS >= 6;

        for (int k = 0; k < COLS; k ++) {
            idxToMax.put(k, ROWS - 1);
        }

        Scanner scanner = new Scanner(System.in);
        int turnCounter = 0;  // increment after each step

        printField(field);

        // replace true
        while (true) {
            if (turnCounter == SQUARE) {
                System.out.println("\n===DRAW===\n");
                break;
            }

            System.out.println("Enter col idx: ");
            int colIdx = scanner.nextInt();
            int rowIdx = idxToMax.get(colIdx);

            if (colIdx < 0 || colIdx >= COLS) {
                System.out.println("\n===WRONG INDEX " + colIdx + " ===\n");
                continue;
            }

            if (rowIdx == -1) {
                System.out.println("\n===INDEX " + colIdx + " IS FULL===\n");
                continue;
            }

            char currentPiece;
            if (turnCounter % 2 == 0) {
                currentPiece = 'X';
            } else {
                currentPiece = 'O';
            }

            field[rowIdx][colIdx] = currentPiece;

            idxToMax.merge(colIdx, -1, Integer::sum);
            turnCounter++;

            printField(field);
        }


    }

    static Character checkWinCondition(int colIdx) {
        int rowIdx = idxToMax.get(colIdx);
        Character winningChar;
        int count = 0;
        // down
        if (rowIdx < ROWS - WIN_CONDITION) {
            int curRowIdx = rowIdx;
            while (curRowIdx < ROWS - 1) {
                if (field[curRowIdx][colIdx] == field[curRowIdx + 1][colIdx]) {
                    winningChar = field[curRowIdx][colIdx];
                    count++;
                    if (count >= WIN_CONDITION) {
                        return winningChar;
                    }
                } else {
                    winningChar = null;
                    count = 0;
                }
                curRowIdx--;
            }
            count = 0;
        }


    }

    static void printField(Character[][] field) {
        for (Character[] row : field) {
            for (Character piece : row) {
                Character pieceToPrint = piece;
                if (pieceToPrint == null) {
                    pieceToPrint = '_';
                }
                System.out.print(pieceToPrint + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}
