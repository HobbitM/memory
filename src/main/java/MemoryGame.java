import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class MemoryGame {

    static String[][] wordsEasy = new String[2][4];
    static String[][] wordsHard = new String[4][4];

    static boolean[][] upDown = new boolean[4][4];
    static Scanner s = new Scanner(System.in);
    static int gameSizeInt = 0;

    public static void main(String[] args) {

        String gameSize = setDiff();;
        while (!(gameSize.equals("1") || gameSize.equals("2"))){
            gameSize = setDiff();
        }
        setup(gameSize);
        long startWatch = System.currentTimeMillis();
        if (gameSize.equals("1")){
            gameSizeInt = 4;
            game(upDown, wordsEasy, startWatch, gameSizeInt); // calls the game
        }
        else{
            gameSizeInt = 8;
            game(upDown, wordsHard, startWatch, gameSizeInt); // calls the game
        }
    }

    private static String setDiff() {
        System.out.println("write 1 or 2 ");
        System.out.println("PICK DIFFICULTY: ");
        System.out.println("1 - easy     2 - hard");
        return s.next();
    }

    //print the board
    public static void setup(String gameSize) {

        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < 4; a++) {
                upDown[i][a]=false;
            }
        }

        if (gameSize.equals("1")){
            wordsEasy = randomizer(gameSize); //Shuffle cards
        }
        else
            wordsHard = randomizer(gameSize); //Shuffle cards
    }

    //print the board
    public static void displayBoard(boolean[][] upDown, String[][] cards, int lifes, int gameSize) {

        System.out.println("-------------");
        if (gameSize == 4)
            System.out.println("Level: EASY");
        else
            System.out.println("Level: HARD");
        System.out.println("Guess chanses: " + lifes);
        System.out.println("     1 2 3 4 ");
        String[] abc = new String[] {"1", "2", "3", "4"};
        for (int i = 0; i < gameSize/2; i++) {
            System.out.print(" " + (abc[i]) + " | ");
            for (int a = 0; a < 4; a++) {
                if (upDown[i][a]) {
                    System.out.print(cards[i][a]);
                    System.out.print(" ");
                }
                else
                    System.out.print("X ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String[][] randomizer(String gameSize) {
        List<String> wordsFromFile = fileLoader("src/main/resources/Words.txt");
        List<String> wordsForGame = new ArrayList<>();
        Random random = new Random();
        int wordsSize = wordsFromFile.size();
        List<Integer> indexes = new ArrayList<>();

        if (gameSize.equals("1")) {

            return getCardsEasy(wordsFromFile, wordsForGame, random, wordsSize, indexes);
        } else if (gameSize.equals("2")) {

            return getCardsHard(wordsFromFile, wordsForGame, random, wordsSize, indexes);
        } else
                return new String[4][4];
    }

    private static String[][] getCardsHard(List<String> wordsFromFile, List<String> wordsForGame, Random random, int wordsSize, List<Integer> indexes) {
        int t = 8;
        return fillList(wordsFromFile, wordsForGame, random, wordsSize, indexes, t);
    }

    private static String[][] getCardsEasy(List<String> wordsFromFile, List<String> wordsForGame, Random random, int wordsSize, List<Integer> indexes) {
        int t = 4;
        return fillList(wordsFromFile, wordsForGame, random, wordsSize, indexes, t);
    }

    private static String[][] fillList(List<String> wordsFromFile, List<String> wordsForGame, Random random, int wordsSize, List<Integer> indexes, int t) {
        int tempt = t;
        for (int j = 0; j < tempt; j++) {
            int index = random.nextInt(wordsSize);
            if (!indexes.contains(index)) {
                indexes.add(index);
                wordsForGame.add(wordsFromFile.get(index));
                wordsForGame.add(wordsFromFile.get(index));
            } else {
                tempt = tempt + 1;
            }
        }

        indexes = new ArrayList<>();
        String[][] cards = new String[t/2][4];
        for (int r = 0; r < t/2; r++) // Cards receive Numbers
        {
            for (int s = 0; s < 4; s++) {
                int index = random.nextInt(wordsForGame.size());
                while (indexes.contains(index)) {
                    index = random.nextInt(wordsForGame.size());
                }
                cards[r][s] = wordsForGame.get(index);
                indexes.add(index);
            }
        }
        return cards;
    }

    //Start the Game
    public static void game(boolean[][] upDown, String[][] cards, long startWatch, int gameSize) {
        int noDownCards = 2*gameSize;
        int lifes;
        long time = 0;
        long stopWatch = 0;

        if (gameSize == 4)
            lifes = 10;
        else
            lifes = 15;

        while (noDownCards > 0) {
            displayBoard(upDown, cards, lifes, gameSize);
            System.out.println("Enter co-ordinate 1");
            String g1 = pickCard(gameSize);
            int g1x = Integer.parseInt(g1.substring(0, 1))-1;
            int g1y = Integer.parseInt(g1.substring(1, 2))-1;
            System.out.println(cards[g1x][g1y]);

            System.out.println("Enter co-ordinate 2");
            String g2 = pickCard(gameSize);
            if (!g2.equals(g1)){
                int g2x = Integer.parseInt(g2.substring(0, 1))-1;
                int g2y = Integer.parseInt(g2.substring(1, 2))-1;
                System.out.println(cards[g2x][g2y]);
                if (cards[g1x][g1y].equals(cards[g2x][g2y])) {
                    System.out.println("You found a match");
                    upDown[g1x][g1y] = true;
                    upDown[g2x][g2y] = true;
                    noDownCards -= 2;
                } else
                    lifes = lifes -1;
                    if (lifes == 0) {
                        break;
                    }
            } else
                System.out.println("That's cheating ;)");
        }
        displayBoard(upDown, cards, lifes, gameSize);
        if (lifes == 0){
            System.out.println("Game over");
        } else {
            stopWatch = System.currentTimeMillis();
            time = TimeUnit.MILLISECONDS.toSeconds((stopWatch - startWatch));
            System.out.println("You win");
            System.out.println("You solved the memory game with " + lifes + " lifes left");
            System.out.println("Your time was: " + time + " s");
        }

        saveToHighScore(time, lifes, stopWatch);
        System.out.println("\nDO YOU WANNA PLAY AGAIN?");
        System.out.println("yes or no");

        String input = s.next();
        if (input.equalsIgnoreCase("yes")){
            startWatch = System.currentTimeMillis();
            String gameSize2 = setDiff();

            setup(gameSize2);

            if (gameSize2.equals("1")){
                gameSizeInt = 4;
                game(upDown, wordsEasy, startWatch, gameSizeInt); // calls the game
            }
            else{
                gameSizeInt = 8;
                game(upDown, wordsHard, startWatch, gameSizeInt); // calls the game
            }
        } else
            System.out.println("BYE BYE");
    }

    private static void saveToHighScore(long time, int lifes, long date) {
        System.out.println("Give your name for Highscore Board");
        String name = s.next();

        System.out.println(name + "You will be remembered!");

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(date);

        String highscore = name + "  TIME: " + time + " s" + "  LIFES REMAINING: " + lifes + "  DATE: " + resultdate;
        System.out.println(highscore);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Highscore.txt", true));
            writer.append('\n');
            writer.append(highscore);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String pickCard(int gameSize) {

        List<String> easyBoard = Arrays.asList("1", "2");
        List<String> hardBoard = Arrays.asList("1", "2", "3", "4");


        String g1 = "  ";
        if (gameSize == 4) {
            while (!(easyBoard.contains(g1.substring(0, 1)) && hardBoard.contains(g1.substring(1, 2)))) {
                System.out.println("Pick proper coordinates");
                g1 = s.next();
            }
            return g1;

        } else {
            while (!(hardBoard.contains(g1.substring(0, 1)) && hardBoard.contains(g1.substring(1, 2)))) {
                System.out.println("Pick proper coordinates");
                g1 = s.next();
            }
            return g1;
        }
    }

    public static List<String> fileLoader(String fileName) {
        List<String> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}