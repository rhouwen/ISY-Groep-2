package stratego.gui;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class GameSimulator {

    public static void main(String[] args) {
        int aggressiveWins = 0;
        int defensiveWins = 0;
        int totalTurnsAggressive = 0;
        int totalTurnsDefensive = 0;

        for (int i = 0; i < 100; i++) {
            boardMP board = new boardMP();
            multiplayerPlace.placePieces(board);

            boolean aggressiveTurn = true;
            int turns = 0;
            Set<String> previousMoves = new HashSet<>();

            while (true) {
                turns++;
                if (aggressiveTurn) {
                    if (!AggressiveAlgorithm.makeMove(board, new PrintWriter(System.out, true), previousMoves)) {
                        defensiveWins++;
                        totalTurnsDefensive += turns;
                        break;
                    }
                } else {
                    if (!DefensiveAlgorithm.makeMove(board, new PrintWriter(System.out, true), previousMoves)) {
                        aggressiveWins++;
                        totalTurnsAggressive += turns;
                        break;
                    }
                }
                aggressiveTurn = !aggressiveTurn;
            }
        }

        System.out.println("Aggressive Algorithm Wins: " + aggressiveWins);
        System.out.println("Defensive Algorithm Wins: " + defensiveWins);
        System.out.println("Average Turns for Aggressive Algorithm: " + (totalTurnsAggressive / 100.0));
        System.out.println("Average Turns for Defensive Algorithm: " + (totalTurnsDefensive / 100.0));
    }
}