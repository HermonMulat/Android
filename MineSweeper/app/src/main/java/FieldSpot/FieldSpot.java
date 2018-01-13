package FieldSpot;


public class FieldSpot{
    private boolean isMine,showUnderneath,hasFlag;
    private int minesAround;

    public FieldSpot(){
        isMine = false;
        showUnderneath = false;
        minesAround = 0;
        hasFlag = false;
    }

    public boolean placeMine(){
        boolean toReturn = !isMine;
        isMine = true;
        return toReturn;
    }

    public void placeFlag(){
        hasFlag = !hasFlag;
    }

    public boolean hasFlag(){
        return hasFlag;
    }


    public boolean revealUnderneath(){
        boolean toReturn = !showUnderneath;
        showUnderneath = true;
        return toReturn;
    }
    public void increaseMineCount(){
        minesAround += 1;
    }
    public int getMinesAround(){
        return minesAround;
    }

    public boolean isMine(){
        return isMine;
    }
    public boolean showUnderneath(){
        return showUnderneath;
    }

}
