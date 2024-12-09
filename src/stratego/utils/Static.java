package stratego.utils;

public class Static {

    // Bevat vaste waarden die nooit veranderen, bordgrootte, stukkenrangen etc.

    public static void printGameRules() {
        System.out.println("Stratego Singleplayer Spelregels:");
        System.out.println("1. Het doel is om de vlag van de tegenstander te vinden.");
        System.out.println("2. Verplaats je stukken over het bord volgens hun bewegingsregels.");
        System.out.println("3. Aanvallen en veroveren van vijandelijke stukken.");
        System.out.println("4. Verlies niet je eigen vlag!");
    }

    public static boolean isMoveValid(String piece, int startX, int startY, int endX, int endY) {
        if (!"Flag".equals(piece)) {
            return Math.abs(endX - startX) <= 1 && Math.abs(endY - startY) <= 1;
        }
        return false;
    }

    public static boolean isGameOver(boolean isFlagCaptured) {
        return isFlagCaptured;
    }

    public static void main(String[] args) {
        printGameRules();

        boolean validMove = isMoveValid("Soldier", 0, 0, 1, 1);
        System.out.println("Is de zet geldig? " + validMove);

        boolean gameOver = isGameOver(false);
        System.out.println("Is het spel voorbij? " + gameOver);
    }
}