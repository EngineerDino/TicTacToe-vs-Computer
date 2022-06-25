package tictactoe;


public class Main {


    public static void main(String[] args) {
        while(true) {
            Game game = new Game();
            if (!game.settings()) {
                break;
            } else {
                game.playGame();
            }
        }

    }
}


//TODO: Implement the hard level minimax algo

