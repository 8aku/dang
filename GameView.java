package com.example.r.dangver1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Board board;
    private int[] tileColors;
    private final int TILE_SIZE = 64;
    private int time;

    public GameView (Context context, Board board) {
        super(context);
        this.board = board;

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        tileColors = new int[4];

        tileColors[0] = Color.BLACK;
        tileColors[1] = Color.RED;
        tileColors[2] = Color.GREEN;
        tileColors[3] = Color.YELLOW;

        setFocusable(true);

        time = 60;

        new CountDownTimer((time * 1000), 1000) {

            public void onTick(long millisUntilFinished) {
                time--;
            }

            public void onFinish() {

            }

        }.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            retry = false;
        }
    }

    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            Paint paint = new Paint();
            for (int i = 0; i < board.getGridSize(); i++) {
                for (int j = 0; j < board.getGridSize(); j++) {
                    paint.setColor(tileColors[board.getTile(i,j)]);
                    canvas.drawRect((i * TILE_SIZE), (j * TILE_SIZE), ( i * TILE_SIZE) + TILE_SIZE, (j * TILE_SIZE) + TILE_SIZE, paint);
                }
            }

            paint.setTextSize(56);
            paint.setColor(Color.WHITE);
            canvas.drawText(String.valueOf(time), 128, 512, paint);
        }
    }
}