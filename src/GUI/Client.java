package GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Client implements Runnable {
    private String hostName = "127.0.0.1";
    private int portNumber = 7789;
    private String username;

    public Client(String username) {
        this.username = username;
    }

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;

    private boolean placed = false;
    private boolean needToPlaceShips = true;

    private Set<Integer> attemptedMoves;
    private Set<Integer> forbiddenMoves; // Set to keep track of forbidden moves around sunk ships
    private List<Integer> targetQueue;
    private Stack<Integer> hitStack; // Stack to keep track of the last BOEM hits for targeted searching
    private Random random;

    private int[] board; // Board representation

    @Override
    public void run() {
        random = new Random();

        try {
            client = new Socket(hostName, portNumber);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Login to the server
            out.println("login " + username);

            String inputMessage;
            while ((inputMessage = in.readLine()) != null && !done) {
                System.out.println(inputMessage);

                if (inputMessage.startsWith("SVR GAMELIST")) {
                    // Handle game list if necessary
                }

                if (inputMessage.startsWith("SVR PLAYERLIST")) {
                    // Handle player list if necessary
                }

                if (inputMessage.startsWith("SVR GAME MATCH")) {
                    // Match assigned, reset game state
                    resetGameState();
                    // Optionally, subscribe to the game here if required
                }

                if (inputMessage.startsWith("SVR GAME CHALLENGE")) {
                    // Handle incoming challenge
                    String challengeNumber = parseChallengeNumber(inputMessage);
                    out.println("challenge accept " + challengeNumber);
                }

                if (inputMessage.contains("YOURTURN")) {
                    if (needToPlaceShips) {
                        placeShips();
                    } else {
                        makeMove();
                    }
                }

                if (inputMessage.startsWith("SVR GAME MOVE")) {
                    handleMoveResult(inputMessage);
                }

                if (inputMessage.startsWith("SVR GAME SINK")) {
                    handleSinkResult();
                }

                if (inputMessage.startsWith("SVR GAME WIN")) {
                    System.out.println("We have won the game!");
                    // Reset game state for a new game
                    resetGameState();
                    // Wait for next match assignment or challenge
                }

                if (inputMessage.startsWith("SVR GAME LOSS")) {
                    System.out.println("We have lost the game.");
                    // Reset game state for a new game
                    resetGameState();
                    // Wait for next match assignment or challenge
                }

                if (inputMessage.startsWith("SVR GAME DRAW")) {
                    System.out.println("The game ended in a draw.");
                    // Reset game state for a new game
                    resetGameState();
                    // Wait for next match assignment or challenge
                }

                if (inputMessage.startsWith("SVR HELP")) {
                    // Handle help message
                }

                if (inputMessage.startsWith("ERR")) {
                    System.err.println("Error from server: " + inputMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    private void resetGameState() {
        placed = false;
        needToPlaceShips = true;
        attemptedMoves = new HashSet<>();
        forbiddenMoves = new HashSet<>();
        targetQueue = new ArrayList<>();
        hitStack = new Stack<>();
        board = new int[64]; // Reset the board
    }

    private void placeShips() {
        int[] shipLengths = {6, 4, 3, 2};

        for (int length : shipLengths) {
            boolean placedShip = false;
            int attempts = 0;
            while (!placedShip && attempts < 1000) { // Increased attempts for robustness
                int startCell = random.nextInt(64);
                boolean isHorizontal = random.nextBoolean();
                if (canPlaceShip(startCell, length, isHorizontal)) {
                    placeShip(startCell, length, isHorizontal);
                    placedShip = true;
                }
                attempts++;
            }
            if (!placedShip) {
                System.err.println("Failed to place ship of length " + length);
            }
        }
        placed = true;
        needToPlaceShips = false;
        // After placing ships, do not make a move immediately.
        // Wait for the next SVR GAME YOURTURN message.
    }

    private boolean canPlaceShip(int startCell, int length, boolean isHorizontal) {
        int row = startCell / 8;
        int col = startCell % 8;

        // Check if the ship fits within the board
        if (isHorizontal) {
            if (col + length > 8) {
                return false; // Ship goes off the board to the right
            }
        } else {
            if (row + length > 8) {
                return false; // Ship goes off the board to the bottom
            }
        }

        // Check for overlap or adjacent ships
        for (int i = -1; i <= length; i++) {
            for (int j = -1; j <= 1; j++) {
                int currentRow, currentCol;
                if (isHorizontal) {
                    currentRow = row + j;
                    currentCol = col + i;
                } else {
                    currentRow = row + i;
                    currentCol = col + j;
                }
                if (currentRow >= 0 && currentRow < 8 && currentCol >= 0 && currentCol < 8) {
                    int index = currentRow * 8 + currentCol;
                    if (board[index] == 1) {
                        return false; // Overlaps or touches another ship
                    }
                }
            }
        }
        return true; // Ship can be placed
    }

    private void placeShip(int startCell, int length, boolean isHorizontal) {
        int row = startCell / 8;
        int col = startCell % 8;
        int endCell;

        if (isHorizontal) {
            endCell = startCell + length - 1;
        } else {
            endCell = startCell + (length - 1) * 8;
        }

        // Mark the ship's cells as occupied
        for (int i = 0; i < length; i++) {
            int currentRow = row;
            int currentCol = col;
            if (isHorizontal) {
                currentCol = col + i;
            } else {
                currentRow = row + i;
            }
            int index = currentRow * 8 + currentCol;
            board[index] = 1;
        }

        // Send the "place" command to the server
        out.println("place " + startCell + " " + endCell);
    }

    private void makeMove() {
        int move;
        if (!targetQueue.isEmpty()) {
            move = targetQueue.remove(0);
        } else {
            do {
                move = random.nextInt(64);
            } while (attemptedMoves.contains(move) || forbiddenMoves.contains(move));
        }

        attemptedMoves.add(move);
        out.println("move " + move);
    }

    private void handleMoveResult(String inputMessage) {
        String result = parseResult(inputMessage);
        int lastMove = parseMove(inputMessage);

        if ("BOEM".equals(result)) {
            hitStack.push(lastMove); // Add the hit to the stack
            addAdjacentTargets(lastMove);
        } else if ("PLONS".equals(result)) {
            // Do nothing specific for a miss
        }
    }

    private void handleSinkResult() {
        while (!hitStack.isEmpty()) {
            int sunkMove = hitStack.pop();
            markForbiddenAround(sunkMove); // Mark the surrounding cells as forbidden
        }
        targetQueue.clear();
    }

    private void addAdjacentTargets(int move) {
        int row = move / 8;
        int col = move % 8;

        int[] possibleMoves = {move - 1, move + 1, move - 8, move + 8};

        for (int target : possibleMoves) {
            if (isValidMove(target, row, col) && !attemptedMoves.contains(target) && !forbiddenMoves.contains(target) && !targetQueue.contains(target)) {
                targetQueue.add(target);
                // Do not add to attemptedMoves here; only add after making the move
            }
        }
    }

    private void markForbiddenAround(int move) {
        int row = move / 8;
        int col = move % 8;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = col + dc;
                int cell = newRow * 8 + newCol;
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    forbiddenMoves.add(cell);
                }
            }
        }
    }

    private boolean isValidMove(int target, int row, int col) {
        if (target < 0 || target >= 64) return false;

        int targetRow = target / 8;
        int targetCol = target % 8;

        // Ensure that the move is adjacent horizontally or vertically
        return (Math.abs(row - targetRow) + Math.abs(col - targetCol)) == 1;
    }

    private int parseMove(String inputMessage) {
        String[] parts = inputMessage.split("MOVE: \"");
        if (parts.length < 2) return -1;
        String movePart = parts[1].split("\"")[0];
        return Integer.parseInt(movePart);
    }

    private String parseResult(String inputMessage) {
        String[] parts = inputMessage.split("RESULT: \"");
        if (parts.length < 2) return "";
        return parts[1].split("\"")[0];
    }

    private String parseChallengeNumber(String inputMessage) {
        String[] parts = inputMessage.split("CHALLENGENUMBER: ");
        if (parts.length < 2) return "";
        String challengePart = parts[1].split(",")[0];
        return challengePart.trim();
    }

    public void shutdown() {
        done = true;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Vraag de gebruiker om een naam wanneer je de main-methode runt
        String username = args.length > 0 ? args[0] : "DefaultName";
        Client client = new Client(username);
        client.run();
    }
}