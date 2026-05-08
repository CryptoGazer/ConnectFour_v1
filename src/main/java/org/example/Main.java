package org.example;

import java.util.*;

public class Main {
    static void main() {
        // TestValues
        int COLS = 7;
        int ROWS = 6;
        int WIN_CONDITION = 4;  // default for ConnectFour

        int SQUARE = COLS * ROWS;

        Character[][] field = new Character[ROWS][COLS];
        HashMap<Integer, Integer> idxToMax = new HashMap<>();

        for (int k = 0; k < COLS; k ++) {
            idxToMax.put(k, ROWS - 1);
        }

        Scanner scanner = new Scanner(System.in);
        int turnCounter = 0; // increment after each step

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
