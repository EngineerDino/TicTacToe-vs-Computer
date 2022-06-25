package tictactoe;


import java.util.Arrays;
import java.util.Scanner;

public class Game {
    String[][] board = {{"_", "_", "_"}, {"_", "_", "_"}, {"_", "_", "_"}};

    String[] symbols = {"X", "O"};

    final String[] OPTIONS = {"user", "easy", "medium", "hard"};
    String status = "Not finished";
    int activePlayer = 0;
    Player[] players = new Player[2];



    public void setPlayers(String[] players) {
        for(int i = 0; i < 2; i++) {
            switch(players[i]) {
                case "user":
                    this.players[i] = new Human(symbols[i]);
                    break;
                case "easy":
                    this.players[i] = new EasyAi(symbols[i]);
                    break;
                case "medium":
                    this.players[i] = new MediumAi(symbols[i], symbols[(i + 1) % 2]);
                    break;
                case "hard":
                    this.players[i] = new HardAi(symbols[i], symbols[(i + 1) % 2]);
                    break;
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
            players[activePlayer].makeMove(this.board);
            // This is a formula to switch between 0 and 1.
            activePlayer = (activePlayer + 1) % 2;
            BoardMaths.printBoard(this.board);
            this.status = BoardMaths.checkBoard(this.board);
            int[][] free = BoardMaths.getFreeSlots(board);

        }
        System.out.println(this.status);

    }
}






