package tictactoe;

import java.util.Random;
import java.util.Scanner;

public abstract class Player {
    String name;
    String symbol;

    public Player(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    //The great thing is, that the board is passed by reference! So any changes we make to the board here
    //are made in the actual board!!
    protected abstract void makeMove(String[][] board);


}


class EasyAi extends Player {

    public EasyAi(String symbol) {
        super("easy", symbol);
    }

    private void randomMove(String[][] board) {
        Random rand = new Random();
        int freeSlots = BoardMaths.countFreeSlots(board);
        int randomMove = rand.nextInt(freeSlots) + 1;
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

    protected void makeMove(String[][] board) {
        randomMove(board);
    }

}

class MediumAi extends Player {
    String opponentSymbol;

    public MediumAi(String symbol, String opponentSymbol) {
        super("medium", symbol);
        this.opponentSymbol = opponentSymbol;
    }

    private void randomMove(String[][] board) {
        Random rand = new Random();
        int freeSlots = BoardMaths.countFreeSlots(board);
        int randomMove = rand.nextInt(freeSlots) + 1;
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

    protected void makeMove(String[][] board) {
        int[] danger = BoardMaths.dangerSlot(board, opponentSymbol);
        int[] winMove = BoardMaths.dangerSlot(board, symbol);
        System.out.println("Making move level \"medium\"");
        // -1 is the standard value that the method dangerSlot returns for both coordinates if there are no 2 in a row
        //First we check, if AI can win - if so, it does it.
        //Next we check if there is a danger, if so it neutralizes it.
        //If none of the above hold true, it makes a random move.
        if (winMove[0] != -1) {
            board[winMove[0]][winMove[1]] = symbol;
            return;
        }
        if (danger[0] == -1) {
            randomMove(board);
        } else {
            board[danger[0]][danger[1]] = symbol;
        }
    }

}


class Human extends Player {

    public Human(String symbol) {
        super("user", symbol);
    }

    @Override
    protected void makeMove(String[][] board) {
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
}

