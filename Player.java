package tictactoe;

import java.util.Random;
import java.util.Scanner;


//An abstract Player class and 4 subclasses (Human, Easy, Medium and Hard) which all override the method makeMove.
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

    @Override
    protected void makeMove(String[][] board) {
        System.out.println("Making move level \"easy\"");
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

    @Override
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

class HardAi extends Player {
    String opponentSymbol;
    public HardAi(String symbol, String opponentSymbol) {
        super("hard", symbol);
        this.opponentSymbol = opponentSymbol;
    }

    @Override
    protected void makeMove(String[][] board) {
        System.out.println("Making move level \"hard\"");

        String[][] boardCopy = BoardMaths.cloneBoard(board);
        int[][] emptySlots = BoardMaths.getFreeSlots(board);
        int[] bestCoordinates = new int[2];
        int bestScore = -10;
        int temp;

        //Go through all the empty Slots and check with the miniMax-Algo which slot will yield the best result!
        for (int[] emptySlot : emptySlots) {
            boardCopy[emptySlot[0]][emptySlot[1]] = this.symbol;
            //Maximizing is set to false because we already made the move and next up is the opponent.
            temp = miniMax(boardCopy, false);
            if (temp > bestScore) {
                bestScore = temp;
                bestCoordinates[0] = emptySlot[0];
                bestCoordinates[1] = emptySlot[1];
            }
            boardCopy[emptySlot[0]][emptySlot[1]] = "_";
        }

        // Now we have in bestCoordinates the move that will lead to the best result. Thus we can make the move!
        board[bestCoordinates[0]][bestCoordinates[1]] = this.symbol;
    }

    //***************************************************
    //*************** The MiniMax-Algorithmus ***********
    //***************************************************
    private int miniMax(String[][] board, boolean myTurn) {

        String[][] boardCopy = BoardMaths.cloneBoard(board);
        int[][] emptySlots = BoardMaths.getFreeSlots(board);
        int temp;
        int maxOrminScore;
        int rating = -10;

        //First check if it is a terminal state! Thats the anchor of the recursion.
        if (!"Not finished".equals(BoardMaths.checkBoard(board))) {
            //Terminal state found!
            return rating(board);
        }

        //We have to make two cases depending on which step we are! Either its our Turn or our Opponents.
        //If its myTurn i want to find the slot that yields the highest (here: 0 or 1) result.
        if (myTurn) {
            maxOrminScore = -10;
            for (int i = 0; i < emptySlots.length && maxOrminScore != 1; i++) {
                boardCopy[emptySlots[i][0]][emptySlots[i][1]] = this.symbol;
                temp = miniMax(boardCopy, false);
                if (temp > maxOrminScore) {
                    maxOrminScore = temp;
                }
                boardCopy[emptySlots[i][0]][emptySlots[i][1]] = "_";
            }
        //If its time to try out slots that our opponent would play, we want OUR result to be minimal.
        //Differently put: we want to find the worst case for us!
        } else {
            maxOrminScore = 10;
            for (int i = 0; i < emptySlots.length && maxOrminScore != -1; i++) {
                boardCopy[emptySlots[i][0]][emptySlots[i][1]] = this.opponentSymbol;
                temp = miniMax(boardCopy, true);
                if (temp < maxOrminScore) {
                    maxOrminScore = temp;
                }
                boardCopy[emptySlots[i][0]][emptySlots[i][1]] = "_";
            }
        }
        return maxOrminScore;
    }

    //In case we reached a terminal state, this gives back 0 for draw, 1 for we win, -1 for opponent wins
    private int rating(String[][] board) {
        int result = 0;
        String endResult = BoardMaths.checkBoard(board); //Can be "Draw" or "X wins" or "O wins".
        if("Draw".equals(endResult)) {
            result = 0;
        } else {
            result = endResult.contains(this.symbol) ? 1 : -1;
        }

        return result;
    }
}




