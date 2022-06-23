package tictactoe;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Game {
    String[][] board = {{"_", "_", "_"}, {"_", "_", "_"}, {"_", "_", "_"}};

    final String[] OPTIONS = {"user", "easy", "medium"};
    String status = "Not finished";
    int activePlayer = 0;
    String[] players = {"user", "easy"}; //
    String[] symbols = {"X", "O"};


    public void setPlayers(String[] players) {
        this.players = players;
    }
    private void makeMove() {
        switch (players[activePlayer]) {
            case "user":
                playerMove();
                break;
            case "easy":
                easyMove();
                break;
            case "medium":
                mediumMove();
                break;
            default:
                System.out.println("Something went wrong!");
                break;
        }
    }
    private void playerMove() {
        Scanner scanner = new Scanner(System.in);
        boolean isInteger;
        int rowInput;
        int colInput;
        while(true) {
            System.out.print("Enter the coordinates:");

            //Check if the next two Inputs are Integers and if so, save them.
            isInteger = scanner.hasNextInt();
            rowInput = isInteger ? scanner.nextInt() : 0;
            isInteger = scanner.hasNextInt();
            colInput = isInteger ? scanner.nextInt() : 0;

            //Check for all kinds of wrong input and save the symbol in the board if everything is ok.
            if (!isInteger) {
                System.out.println("You should enter numbers!");
            } else if (rowInput > 3 || rowInput < 1 || colInput > 3 || colInput < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
            } else if (!board[rowInput - 1][colInput - 1].equals("_")) {
                System.out.println("This cell is occupied! Choose another one!");
            } else {
                board[rowInput - 1][colInput - 1] = symbols[activePlayer];
                break;
            }
            //Make sure the scanner reads the whole line such that he is done and "reset" for the next run of the loop
            scanner.nextLine();
            //End of  infinite while to post one Symbol
        }
    }

    private void easyMove() {
        System.out.println("Making move level \"easy\"");
        randomMove();
    }
    private void mediumMove() {
        int[] danger = BoardMaths.dangerSlot(board, symbols[(activePlayer+1) % 2]);
        int[] winMove = BoardMaths.dangerSlot(board, symbols[activePlayer]);
        System.out.println("Making move level \"medium\"");
        // -1 is the standard value that the method dangerSlot returns for both coordinates if there are no 2 in a row
        //First we check, if AI can win - if so, it does it.
        //Next we check if there is a danger, if so it neutralizes it.
        //If none of the above hold true, it makes a random move.
        if (winMove[0] != -1) {
            this.board[winMove[0]][winMove[1]] = symbols[activePlayer];
            return;
        }
        if (danger[0] == -1) {
            randomMove();
        } else {
            this.board[danger[0]][danger[1]] = symbols[activePlayer];
        }
    }
    private void randomMove() {
        Random rand = new Random();
        int freeSlots = BoardMaths.countFreeSlots(this.board);
        int randomMove = rand.nextInt(freeSlots) + 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("_")) {
                    randomMove -= 1;
                }
                if (randomMove == 0) {
                    board[i][j] = symbols[activePlayer];
                    return;
                }
            }
        }
    }


    public boolean settings() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Input command:");
            String[] inputs = scanner.nextLine().strip().split(" ");

            switch (evalInput(inputs)) {
                case 0:
                    return false;
                case 1:
                    System.out.println("Bad parameters!");
                    continue;
                case 2:
                    setPlayers(Arrays.copyOfRange(inputs, 1, 3));
                    return true;
            }
            //this line of code should never be reached :D
        }
    }

    private int evalInput(String[] inputs) {
        if ("exit".equals(inputs[0])) {
            return 0;
        } else if (inputs.length != 3 || !"start".equals(inputs[0])) {
            return 1;
        }
        //now we for sure are left with inputs that contain start plus 2 more strings.
        // check if the next two words are element of the "allowed" keywords defined in the final field "options"
        if (Arrays.asList(this.OPTIONS).contains(inputs[1]) && Arrays.asList(this.OPTIONS).contains(inputs[2])) {
            return 2;
        } else {
            return 1;
        }
    }

    public void playGame() {
        BoardMaths.printBoard(this.board);
        while (status.equals("Not finished")) {
            makeMove();
            // This is a formula to switch between 0 and 1.
            activePlayer = (activePlayer + 1) % 2;
            BoardMaths.printBoard(this.board);
            this.status = BoardMaths.checkBoard(this.board);

        }
        System.out.println(this.status);

    }
}




// TODO: Make a class Player and then subclasses Computer and Human that override the method makeMove().
// Or an interface Player which has to have an absract calls makeMove and the static class randomMove

