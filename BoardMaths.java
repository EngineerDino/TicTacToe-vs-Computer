package tictactoe;

public class BoardMaths {

    public static int countFreeSlots(String[][] board) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                counter += board[i][j].equals("_") ? 1 : 0;
            }
        }
        return counter;
    }

    public static int[][] getFreeSlots(String[][] board) {
        int[][] freeSlots = new int[countFreeSlots(board)][2];
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ("_".equals(board[i][j])) {
                    freeSlots[counter][0] = i;
                    freeSlots[counter][1] = j;
                    counter += 1;
                }
            }
        }
        return freeSlots;
    }

    public static String[][] cloneBoard(String[][] board) {
        String[][] cloned = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cloned[i][j] = board[i][j];
            }
        }
        return cloned;
    }

    public static void printBoard(String[][] board) {
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


    public static String checkBoard(String[][] board) {
        String wCheck = checkString(board);
        String status = "Not finished";

        if (wCheck.contains("XXX")) {
            status = "X wins";
        } else if (wCheck.contains("OOO")) {
            status = "O wins";
        } else if (!wCheck.contains("_")) {
            status = "Draw";
        }
        return status;
    }

    //This is the main tool to check for the status of the board. I return all rows, columns and two diagonals in one string
    // Always separated by a "-". Now I can check this string for all kinds of occurrences (XXX or OO_ etc.)
    private static String checkString(String[][] board) {
        String wCheck = " ";
        for (int i = 0; i < 3; i++) {
            wCheck += board[i][0] + board[i][1] + board[i][2] + "-";
        }
        for (int i = 0; i < 3; i++) {
            wCheck += board[0][i] + board[1][i] + board[2][i] + "-";
        }
        wCheck += board[0][0] + board[1][1] + board[2][2] + "-" + board[0][2] + board[1][1] + board[2][0];

        return wCheck;
    }

    public static int[] dangerSlot(String[][] board, String symbol) {
        String check = BoardMaths.checkString(board);
        String[] dangerStrings = {"_" + symbol + symbol, symbol + "_" + symbol, symbol + symbol + "_"};
        int[] dangerSlot = {-1, -1};
        int freePosition = -1;
        int rowColDiag = -1;
        for (int i = 0; i < dangerStrings.length; i++) {
            if (check.contains(dangerStrings[i])) {
                freePosition = i;
                //This is gonna be a number that tells us, in which row or column or diagonal the danger occurs.
                rowColDiag = check.indexOf(dangerStrings[i]) / 4;
            }
        }

        //this means, no "2 in a row" were found and we return the standard value for dangerSlot which is set {-1,-1}
        if (rowColDiag == -1) {
            return dangerSlot;
        }

        // Now rowColDiag is either 0, 1 or 2 for the rows; 3,4 or 5 for the columns; 6 or 7 for the diagonals.
        //Now we have to figure out the coordinates of the free "_" spot (which column and row)
        if (rowColDiag <= 2) {              //we are in one of the rows
            dangerSlot[0] = rowColDiag;
            dangerSlot[1] = freePosition;
        } else if (rowColDiag <= 5) {       //we are in one of the columns
            dangerSlot[0] = freePosition;
            dangerSlot[1] = rowColDiag - 3;
        } else if (rowColDiag == 6) {       //main diagonal
            dangerSlot[0] = freePosition;
            dangerSlot[1] = freePosition;
        } else if (rowColDiag == 7) {       //secondary diagonal
            dangerSlot[0] = freePosition;
            dangerSlot[1] = 2 - freePosition;
        }

        return dangerSlot;
    }
}
