package tictactoe;

import java.util.Scanner;
import java.util.Random;

public class Game {
    String[][] board = {{"_", "_", "_"}, {"_", "_", "_"}, {"_", "_", "_"}};
    String status = "Not finished";
    int activePlayer = 0;
    int[] players = {0, 1}; // 0 is the human, 1 is the "easy" AI, in future there will be 2 and 3...
    String[] symbols = {"X", "O"};

    private void makeMove() {
        switch (players[activePlayer]) {
            case 0:
                playerMove(symbols[activePlayer]);
                break;
            case 1:
                randomMove(symbols[activePlayer]);
                break;
            default:
                System.out.println("Something went wrong!");
                break;
        }
    }
    private void playerMove(String symbol) {
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
                board[rowInput - 1][colInput - 1] = symbol;
                break;
            }
            //Make sure the scanner reads the whole line such that he is done and "reset" for the next run of the loop
            scanner.nextLine();
            //End of  infinite while to post one Symbol
        }
    }

    private void randomMove(String symbol) {
        Random rand = new Random();
        int freeSlots = countFreeSlots();
        int randomMove = rand.nextInt(freeSlots) + 1;
        System.out.println("Making move level \"easy\"");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("_")) {
                    randomMove -= 1;
                }
                if (randomMove == 0) {
                    board[i][j] = symbol;
                    return;
                }
            }
        }
    }

    private int countFreeSlots() {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                counter += board[i][j].equals("_") ? 1 : 0;
            }
        }
        return counter;
    }

    private void printBoard() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[i][j]);
            }
            System.out.print(" |" + "\n");
        }
        System.out.println("---------");
    }

    private void checkBoard() {
        String wCheck = " ";
        // Save all rows and columns and diagonals in one string, separated by "-".
        for (int i = 0; i < 3; i++) {
            wCheck += board[i][0] + board[i][1] + board[i][2] + "-" + board[0][i] + board[1][i] + board[2][i] + "-";
        }
        wCheck += board[0][0] + board[1][1] + board[2][2] + "-" + board[0][2] + board[1][1] + board[2][0];

        if (wCheck.contains("XXX")) {
            status = "X wins";
        } else if (wCheck.contains("OOO")) {
            status = "O wins";
        } else if (!wCheck.contains("_")) {
            status = "Draw";
        }
    }

    public void playGame() {
        printBoard();
        while (status.equals("Not finished")) {
            makeMove();
            activePlayer = (activePlayer + 1) % 2;
            printBoard();
            checkBoard();
        }
        System.out.println(status);
    }
}
