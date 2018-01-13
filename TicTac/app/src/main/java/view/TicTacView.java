package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hermon.tictac.MainActivity;
import com.example.hermon.tictac.R;

import java.util.ArrayList;
import java.util.List;

import model.TicTacModel;

public class TicTacView extends View {
    private Paint paintBg;
    private Paint paintLine;

    private Paint paintO;
    private Paint paintX;
    private Paint paintStrike;

    private Bitmap smiley;

    private int LAST_X = 0; // Most recent move's x coordinate
    private int LAST_Y = 0; // Most recent move's y coordinate

    private int TURN = 0 ;  // To keep track of who made the last move

    public TicTacView(Context context, AttributeSet attrs){
        super(context,attrs);
        // make paint object a class wide variable for efficiency
        paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL);
        System.out.println("-----------------------Hello----------------------------");
        paintO = new Paint();
        paintO.setColor(Color.parseColor("#ff8d64"));
        paintO.setStyle(Paint.Style.STROKE);
        paintO.setStrokeWidth(8);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(8);

        paintStrike = new Paint();
        paintStrike.setColor(Color.BLUE);
        paintStrike.setStyle(Paint.Style.STROKE);
        paintStrike.setStrokeWidth(10);

        paintX = new Paint();
        paintX.setColor(Color.parseColor("#31747d"));
        paintX.setStyle(Paint.Style.STROKE);
        paintX.setStrokeWidth(8);

        smiley = BitmapFactory.decodeResource(getResources(), R.drawable.smile);

    }

    private void drawGameGrid(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // two horizontal lines
        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 3, getWidth(),
                2 * getHeight() / 3, paintLine);

        // two vertical lines
        canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 3, 0, 2 * getWidth() / 3, getHeight(),
                paintLine);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        System.out.println("-----------------------Hello----------------------------");
        canvas.drawRect(0,0,getWidth(),getHeight(),paintBg);

        drawGameGrid(canvas);
        drawPlayers(canvas);
        if (TicTacModel.WINNER != TicTacModel.EMPTY){
            ((MainActivity)getContext()).stopTimer(1);
            ((MainActivity)getContext()).stopTimer(0);
            crossOut(canvas);
        }

        canvas.drawBitmap(smiley, 0, 0, null);
    }

    private void crossOut(Canvas canvas) {
        int startx = 0;
        int starty = 0;
        int endx = 2;
        int endy = 2;

        // board[x][0] == board[x][1] && board[x][1] == board[x][2]
        if (TicTacModel.WIN_DIRECTION == 0){
            startx = LAST_X;
            endx = LAST_X;
        }
        //board[0][y] == board[1][y] && board[1][y] == board[2][y]
        else if (TicTacModel.WIN_DIRECTION == 1){

            starty = LAST_Y;
            endy = LAST_Y;
        }
        // board[2][0]==board[1][1] && board[1][1]==board[0][2]
        else if (TicTacModel.WIN_DIRECTION == 3){
            startx = 2;
            endx = 0;
        }

        canvas.drawLine( startx * getWidth() / 3 + getWidth() / 6 ,
                         starty * getHeight() / 3 + getHeight() / 6,
                         endx * getWidth() / 3 + getWidth() / 6 ,
                         endy * getHeight() / 3 + getHeight() / 6 , paintStrike);
    }


    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TicTacModel.getGameModel().getField(i,j) == TicTacModel.CIRCLE) {

                    // draw a circle at the center of the field

                    // X coordinate: left side of the square + half width of the square
                    float centerX = i * getWidth() / 3 + getWidth() / 6;
                    float centerY = j * getHeight() / 3 + getHeight() / 6;
                    int radius = getHeight() / 6 - 16;

                    canvas.drawCircle(centerX, centerY, radius, paintO);

                } else if (TicTacModel.getGameModel().getField(i,j) == TicTacModel.CROSS) {

                    canvas.drawLine(i * getWidth() / 3 + 16, j * getHeight() / 3 + 16,
                            (i + 1) * getWidth() / 3 - 16 ,
                            (j + 1) * getHeight() / 3 - 16, paintX);

                    canvas.drawLine((i + 1) * getWidth() / 3 - 16, j * getHeight() / 3 + 16,
                            i * getWidth() / 3 + 16 , (j + 1) * getHeight() / 3 - 16, paintX);
                }
            }
        }
    }

    public void reset(){
        TicTacModel.getGameModel().resetBoard();
        TURN = 0;
        invalidate();
        ((MainActivity)getContext()).setMessage("O starts" );
        ((MainActivity)getContext()).restartTimer(0);
        ((MainActivity)getContext()).restartTimer(1);
        ((MainActivity)getContext()).startTimer(0);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){

            int x = ((int)event.getX()) / (getWidth()/3);
            int y = ((int)event.getY()) / (getHeight()/3);

            if (TicTacModel.getGameModel().getField(x,y) == TicTacModel.EMPTY &&
                    TicTacModel.WINNER == TicTacModel.EMPTY) {
                TicTacModel.getGameModel().setField(x, y, TicTacModel.getNextPlayer());
                TicTacModel.getGameModel().changePlayer();

                // We need this if statement because TicTacModel.CROSS/CIRCLE are numbers
                String next = "O";
                if (TicTacModel.getNextPlayer() == TicTacModel.CROSS) {
                    next = "X";
                }
                ((MainActivity) getContext()).setMessage("Next is " + next);

                LAST_X = x;
                LAST_Y = y;

                TicTacModel.getGameModel().checkWinner(x, y);

                if (TicTacModel.WINNER == TicTacModel.CIRCLE){
                    ((MainActivity)getContext()).setMessage("O wins!");
                }
                else if (TicTacModel.WINNER == TicTacModel.CROSS){
                    ((MainActivity)getContext()).setMessage("X wins!");
                }

                ((MainActivity)getContext()).stopTimer(TURN);
                TURN += 1;
                ((MainActivity) getContext()).startTimer(TURN);
                if (TURN == 9 && TicTacModel.WINNER == TicTacModel.EMPTY){
                    ((MainActivity) getContext()).stopTimer(TURN);
                    ((MainActivity) getContext()).setMessage("It's a Draw");
                }
                invalidate(); // can also invalidate only a specific part of a screen
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        smiley = Bitmap.createScaledBitmap(smiley, getWidth()/3, getHeight()/3, false);
    }
}
