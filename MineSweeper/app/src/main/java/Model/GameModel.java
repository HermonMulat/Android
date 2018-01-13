package Model;

import com.example.hermon.minesweeper.GameActivity;
import android.content.Context;


import java.util.ArrayList;
import java.util.Random;
import FieldSpot.FieldSpot;


public class GameModel {

    // Private variables for GameModel
    private static GameModel sweeperModel = null;
    private static ArrayList<FieldSpot> mineField = new ArrayList<FieldSpot>();
    private int gridSize;
    private int numberOfMines;
    private int availableFlags;

    public boolean WINCONDITION;


    public static boolean GAMEOVER = false;

    public int getGridSize(){
        return gridSize;
    }
    public int getRemainingFlags() { return availableFlags; }
    public void foundMine() {
        if (availableFlags != 0) {
            availableFlags--;
        }
    }
    public void removedFlag(){
        if (availableFlags < numberOfMines){
            availableFlags ++;
        }
    }
    public void donNeedFlags(){
        availableFlags = 0;
    }

    private GameModel(int size){
        gridSize = size;
        resetField();
    }

    public void resetField() {
        // generate/replace new fields
        // select mine count based on grid size
        int totalCount = (gridSize*gridSize)/(36/gridSize);
        mineField.clear();
        GAMEOVER = false;
        numberOfMines = totalCount;
        availableFlags = totalCount;
        for (int coord=0; coord < gridSize*gridSize; coord++){
            mineField.add(new FieldSpot());
        }

        // populate mines
        Random rand = new Random();
        for (int mineCount=0; mineCount < totalCount; mineCount++) {
            // make sure you place a mine at a new spot
            while (!putMine(rand.nextInt(gridSize),rand.nextInt(gridSize))) {}
        }
    }

    // initialize the game model with difficulty settings/ or just retrieve model
    public static GameModel initializeModel(int n){
        sweeperModel = new GameModel(n);
        return sweeperModel;
    }

    public static GameModel getModel (){
        return sweeperModel;
    }

    private boolean putMine(int x, int y){
        // Try placing a mine - if it succeeds (there was no mine previously), update neighbors
        if (field(x,y).placeMine()){
            for (int dx = -1; dx <= 1 ; dx++) {
                for (int dy = -1; dy <= 1 ; dy++) {
                    if (field(x+dx,y+dy)!= null){
                        field(x+dx,y+dy).increaseMineCount();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public FieldSpot field(int x, int y){
        if (x>=0 && y>=0 && y<gridSize&& x<gridSize){
            return mineField.get(x * gridSize + y);
        }
        return null;
    }

    public void stepOnField(int x, int y){
        reveal(x,y);
        if (!GAMEOVER){
            updateGameState();
        }
    }

    private void updateGameState() {
        int revealedCount = 0;
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if (field(x,y)!= null && field(x,y).showUnderneath()){
                    revealedCount ++;
                }
            }
        }
        if (revealedCount == gridSize*gridSize - numberOfMines){
            GAMEOVER = true;
            WINCONDITION = true;
            donNeedFlags();
        }
    }

    public void reveal(int x, int y) {
        FieldSpot currPoss = field(x, y);
        if (currPoss == null || currPoss.showUnderneath()){
            return;
        }
        if (currPoss.isMine()){
            currPoss.revealUnderneath();
            GAMEOVER = true;
            WINCONDITION = false;
            return;
        }

        if (currPoss.getMinesAround() != 0){
            currPoss.revealUnderneath();
            return;
        }
        else{
            currPoss.revealUnderneath();
            for (int dx = -1; dx <=1 ; dx++) {
                for (int dy = -1; dy <= 1 ; dy++) {
                    reveal(x+dx,y+dy);
                }
            }
        }
    }
}
