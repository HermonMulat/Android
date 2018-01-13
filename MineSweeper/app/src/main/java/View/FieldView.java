package View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hermon.minesweeper.GameActivity;
import com.example.hermon.minesweeper.R;

import java.util.ArrayList;

import FieldSpot.FieldSpot;
import Model.GameModel;

public class FieldView extends View{

    private ArrayList<Bitmap> numbers = new ArrayList<Bitmap>();

    private Bitmap closedTile,flagTile,mineTile,mine2Tile;

    private int[] tile_id = {R.drawable.m0,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,
                             R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8};

    final Handler handler = new Handler();
    Runnable handlerRunnable;
    boolean runnableScheduled;
    private boolean TIMER_ON = false;

    private float firstX,firstY;


    public FieldView(Context context, AttributeSet attrib){
        super(context,attrib);

        // Number Tiles
        for (int i = 0; i < 9; i++) {
            numbers.add(i,BitmapFactory.decodeResource(getResources(), tile_id[i]));
        }

        // other Tiles
        closedTile = BitmapFactory.decodeResource(getResources(), R.drawable.closed_tile);
        flagTile = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        mineTile = BitmapFactory.decodeResource(getResources(), R.drawable.mine);
        mine2Tile = BitmapFactory.decodeResource(getResources(), R.drawable.mine2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ((GameActivity)getContext()).setMineCount(GameModel.getModel().getRemainingFlags());

        if (GameModel.getModel().GAMEOVER) {
            ((GameActivity) getContext()).changeImage(GameModel.getModel().WINCONDITION);
            ((GameActivity) getContext()).stopTimer();
            if (GameModel.getModel().WINCONDITION){
                GameModel.getModel().donNeedFlags();
            }
        }
        showGameState(canvas);
    }

    private void showGameState(Canvas canvas){
        int mines,gridSize = GameModel.getModel().getGridSize();
        float tileSizeY = getHeight()/gridSize,tileSizeX = getWidth()/gridSize;
        FieldSpot curPos;

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                curPos = GameModel.getModel().field(x,y);
                if (curPos.showUnderneath()){
                    if (curPos.isMine()){
                        canvas.drawBitmap(numbers.get(0),x*tileSizeX,y*tileSizeY,null);
                        canvas.drawBitmap(mine2Tile,x*tileSizeX,y*tileSizeY,null);
                    }
                    else {
                        mines = curPos.getMinesAround();
                        canvas.drawBitmap(numbers.get(mines), x * tileSizeX, y * tileSizeY, null);
                    }
                }
                else if (curPos.isMine() && GameModel.GAMEOVER){
                    canvas.drawBitmap(numbers.get(0),x*tileSizeX,y*tileSizeY,null);
                    canvas.drawBitmap(mineTile,x*tileSizeX,y*tileSizeY,null);
                }
                else{
                    if (curPos.hasFlag()) {
                        canvas.drawBitmap(flagTile,x*tileSizeX,y*tileSizeY,null);
                    }
                    else{
                        canvas.drawBitmap(closedTile,x*tileSizeX,y*tileSizeY,null);
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int gridSize = GameModel.getModel().getGridSize();
        float MV_THRESHOLD = getWidth()/gridSize;
        final int x = ((int)event.getX()) / (getWidth()/gridSize);
        final int y = ((int)event.getY()) / (getHeight()/gridSize);

        if (!GameModel.GAMEOVER){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                firstX = event.getX();
                firstY = event.getY();
                runnableScheduled = true;
                handlerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!GameModel.getModel().field(x, y).hasFlag()){
                            GameModel.getModel().foundMine();
                        }
                        else{
                            GameModel.getModel().removedFlag();
                        }
                        GameModel.getModel().field(x, y).placeFlag();
                        runnableScheduled = false;
                        invalidate();
                    }
                };
                // if player holds for half a second place a flag
                handler.postDelayed(handlerRunnable,500);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                handler.removeCallbacks(handlerRunnable);
                //meaning runnable hasn't executed yet
                if (runnableScheduled){
                    if (!GameModel.getModel().field(x,y).hasFlag()){
                        GameModel.getModel().stepOnField(x, y);
                    }
                    runnableScheduled = false;
                    invalidate();
                }
            }
            else{
                if (Math.abs(firstX-event.getX())> MV_THRESHOLD ||
                        Math.abs(firstY-event.getY()) > MV_THRESHOLD){
                    handler.removeCallbacks(handlerRunnable);
                    runnableScheduled = false;
                }
            }
            if (!TIMER_ON){
                TIMER_ON = true;
                ((GameActivity)getContext()).startTimer();
            }
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int gridSize = GameModel.getModel().getGridSize();
        int tileSizeX = getWidth()/gridSize;
        int tileSizeY = getHeight()/gridSize;

        //scale the images
        for (int i = 0; i < 9; i++) {
            numbers.set(i, Bitmap.createScaledBitmap(numbers.get(i),tileSizeX,tileSizeY,false));
        }
        // now scale the other tiles
        closedTile = Bitmap.createScaledBitmap(closedTile,tileSizeX,tileSizeY,false);
        flagTile = Bitmap.createScaledBitmap(flagTile,tileSizeX,tileSizeY,false);
        mineTile = Bitmap.createScaledBitmap(mineTile,tileSizeX,tileSizeY,false);
        mine2Tile = Bitmap.createScaledBitmap(mine2Tile,tileSizeX,tileSizeY,false);
    }

    public void reset(){
        GameModel.getModel().resetField();
        runnableScheduled = false;
        TIMER_ON = false;
        handler.removeCallbacks(handlerRunnable);
        ((GameActivity)getContext()).resetTimer();

        invalidate();

    }

}
