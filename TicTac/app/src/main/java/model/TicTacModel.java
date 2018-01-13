package model;

import com.example.hermon.tictac.MainActivity;

public class TicTacModel {

    private static TicTacModel gameModel = null;

    public static final short EMPTY = 0;
    public static final short CIRCLE = 1;
    public static final short CROSS = 2;

    private static short nextPlayer = CIRCLE;

    public static short WINNER = EMPTY;
    public static int WIN_DIRECTION = -1;

    private short[][] board = {{EMPTY,EMPTY,EMPTY},
                               {EMPTY,EMPTY,EMPTY},
                               {EMPTY,EMPTY,EMPTY}};

    private TicTacModel(){

    }

    public static TicTacModel getGameModel(){
        if (gameModel == null){
            gameModel = new TicTacModel();
        }
        return gameModel;
    }

    public void resetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
        nextPlayer = CIRCLE;
        WINNER = EMPTY;
        WIN_DIRECTION = -1;
    }

    public void changePlayer(){
        if (nextPlayer == CIRCLE){
            nextPlayer = CROSS;
        }
        else{
            nextPlayer = CIRCLE;
        }
    }

    public void setField(int x, int y, short player){
        board[x][y] = player;
    }

    public short getField(int x, int y){
        return board[x][y];
    }

    public static short getNextPlayer() {
        return nextPlayer;
    }

    public void checkWinner(int x, int y){
        short poss_winner = board[x][y];

        // horizontal win
        if (board[x][0] == board[x][1] && board[x][1] == board[x][2]){
            WINNER = poss_winner;
            WIN_DIRECTION = 0;
        }

        // vertical win
        if (board[0][y] == board[1][y] && board[1][y] == board[2][y]){
            WINNER = poss_winner;
            WIN_DIRECTION =  1;
        }

        if (board[1][1]== poss_winner ) {
            //  first diagonal
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                WINNER = poss_winner;
                WIN_DIRECTION =  2;
            }

            // second diagonal
            if (board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
                WINNER = poss_winner;
                WIN_DIRECTION =  3;
            }
        }
    }

}
