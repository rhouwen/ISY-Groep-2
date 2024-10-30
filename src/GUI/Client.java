package GUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Client implements Runnable {
    private String hostName = "127.0.0.1";



    private int portNumber = 7789;

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;
    private boolean placed = false;

    private Set<Integer> attemptedMoves;
    private Set<Integer> forbiddenMoves; // Set om verboden moves bij te houden rondom gezonken schepen
    private List<Integer> targetQueue;
    private Stack<Integer> hitStack; // Stack om de laatste BOEM hits bij te houden voor gericht zoeken
    private Random random;

    @Override
    public void run() {
        attemptedMoves = new HashSet<>();
        forbiddenMoves = new HashSet<>();
        targetQueue = new ArrayList<>();
        hitStack = new Stack<>();
        random = new Random();

        try {
            client = new Socket(hostName, portNumber);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread thread = new Thread(inputHandler);
            thread.start();

            // Login en abonneer op het spel
            out.println("login " + "ISY GROEP 2 BABY");
            out.println("subscribe battleship");

            String inputMessage;
            while ((inputMessage = in.readLine()) != null) {
                System.out.println(inputMessage);

                if (inputMessage.contains("YOURTURN")) {
                    if (!placed) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void placeShips() {
        out.println("place 1 6");   // Lengte 6
        out.println("place 16 19"); // Lengte 4
        out.println("place 32 34"); // Lengte 3
        out.println("place 60 61"); // Lengte 2
        placed = true;
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
            hitStack.push(lastMove); // Voeg de hit toe aan de stack
            addAdjacentTargets(lastMove);
        } else if ("PLONS".equals(result)) {
            if (!hitStack.isEmpty()) {
                int lastHit = hitStack.peek(); // Haal de laatste BOEM op zonder te verwijderen
                addAdjacentTargets(lastHit);
            }
        }
    }

    private void handleSinkResult() {
        while (!hitStack.isEmpty()) {
            int sunkMove = hitStack.pop();
            markForbiddenAround(sunkMove); // Markeer de omliggende cellen als verboden
        }
        targetQueue.clear();
    }

    private void addAdjacentTargets(int move) {
        int row = move / 8;
        int col = move % 8;

        int[] possibleMoves = {move - 1, move + 1, move - 8, move + 8};

        for (int target : possibleMoves) {
            if (isValidMove(target, row, col) && !attemptedMoves.contains(target) && !forbiddenMoves.contains(target)) {
                targetQueue.add(target);
                attemptedMoves.add(target);
            }
        }
    }

    private void markForbiddenAround(int move) {
        int row = move / 8;
        int col = move % 8;

        int[] forbiddenCells = {
                move - 1, move + 1, move - 8, move + 8,
                move - 9, move - 7, move + 7, move + 9
        };

        for (int cell : forbiddenCells) {
            if (isValidForbidden(cell, row, col)) {
                forbiddenMoves.add(cell);
            }
        }
    }

    private boolean isValidMove(int target, int row, int col) {
        if (target < 0 || target >= 64) return false;

        int targetRow = target / 8;
        int targetCol = target % 8;

        return Math.abs(row - targetRow) <= 1 && Math.abs(col - targetCol) <= 1
                && !(Math.abs(row - targetRow) == 1 && Math.abs(col - targetCol) == 1);
    }

    private boolean isValidForbidden(int target, int row, int col) {
        if (target < 0 || target >= 64) return false;

        int targetRow = target / 8;
        int targetCol = target % 8;

        return Math.abs(row - targetRow) <= 1 && Math.abs(col - targetCol) <= 1;
    }

    private int parseMove(String inputMessage) {
        String[] parts = inputMessage.split("MOVE: \"");
        String movePart = parts[1].split("\"")[0];
        return Integer.parseInt(movePart);
    }

    private String parseResult(String inputMessage) {
        String[] parts = inputMessage.split("RESULT: \"");
        return parts[1].split("\"")[0];
    }

    public void shutdown() {
        done = true;
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inputReader.readLine();
                    if (message.equalsIgnoreCase("/quit")) {
                        inputReader.close();
                        shutdown();
                    } else if (message.startsWith("/login")) {
                        String[] i = message.split(" ", 2);
                        String name = i[1];
                        login(name);
                    } else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }

        private void login(String name) {
            out.println("login " + name);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}