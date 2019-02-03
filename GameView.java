package com.example.r.dangver1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Board board;
    private android.graphics.Bitmap[] tileTypes;
    private final int TILE_SIZE = 64;
    private int time;

    public GameView(Context context, Board board) {
        super(context);
        this.board = board;

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        Resources res = getResources();
        tileTypes = new android.graphics.Bitmap[4];
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;

        //make less repetitive
        Bitmap type1_bitmap = BitmapFactory.decodeResource(res, R.drawable.blue_tiles);
        Bitmap type2_bitmap = BitmapFactory.decodeResource(res, R.drawable.pink_tiles);
        Bitmap type3_bitmap = BitmapFactory.decodeResource(res, R.drawable.green_tiles);

        tileTypes[0] = Bitmap.createBitmap(16, 16, conf);

        tileTypes[1] = type1_bitmap;
        tileTypes[2] = type2_bitmap;
        tileTypes[3] = type3_bitmap;

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

                    canvas.drawBitmap(tileTypes[board.getTile(i, j)], i * TILE_SIZE, j * TILE_SIZE, paint);
                }
            }

            paint.setTextSize(56);
            paint.setColor(Color.WHITE);
            canvas.drawText("TIME REMAINING: " + String.valueOf(time), 128, 1200, paint);
        }
    }
}