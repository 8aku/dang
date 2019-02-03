package com.example.r.dangver1;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.Random;

public class Board {

    private int[][] tileArray;
    private int[][] markedTiles;
    final int GRID_SIZE = 4;
    private final int NUM_TYPES = 4;
    MainActivity activity;

    public Board(MainActivity activity) {
        this.activity = activity;
        tileArray = new int[GRID_SIZE][GRID_SIZE];
        markedTiles = new int[GRID_SIZE][GRID_SIZE];

        Random rand = new Random();
        int min = 1;
        int max = NUM_TYPES - 1;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                tileArray[i][j] = rand.nextInt((max - min) + 1) + min;

                markedTiles[i][j] = 0;
            }
        }
    }

    public int getGridSize() {
        return GRID_SIZE;
    }


    public int getTile(int x, int y) {

        return tileArray[x][y];
    }

    //Checks to see if tile is on the board.
    private boolean isInBounds(int x, int y) {
        return ((x < GRID_SIZE && x >= 0) && (y < GRID_SIZE && y >= 0));
    }

    //If the tile has neighbors of the same type, delete the tiles and increment surrounding tiles.
    public void boardClicked(int x, int y) {
        if (hasNeighbors(x, y)) {
            resetMarkedTiles();
            deleteTile(x, y, tileArray[x][y]);
            incrementTiles();

            if (noMoreMoves()) {
                showEndDialog();
            }
        }
    }

    private void resetMarkedTiles() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                markedTiles[i][j] = 0;
            }
        }
    }

    //Increments tile in array, changing its "type".
    private void incrementTile(int x, int y, int type) {

        if (type == NUM_TYPES - 1)
            type -= 1;

        if (tileArray[x][y] != 0) {
            tileArray[x][y] += type;

            //loop back to the beginning of the number of type
            if (tileArray[x][y] >= NUM_TYPES) {
                tileArray[x][y] -= (NUM_TYPES - 1);
            }
        }
    }

    //Increments every marked tile, changing its "type".
    private void incrementTiles() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (markedTiles[i][j] > 0) {
                    incrementTile(i, j, markedTiles[i][j]);
                }
            }
        }
    }

    //Checks to see if tile has any neighbors of the same type.
    //If there are no neighbors of the same type, the tile will not delete.
    private boolean hasNeighbors(int x, int y) {
        if (isInBounds(x, y)) {
            int type = tileArray[x][y];

            return ((isInBounds(x - 1, y) && tileArray[x - 1][y] == type) ||
                    (isInBounds(x + 1, y) && tileArray[x + 1][y] == type) ||
                    (isInBounds(x, y - 1) && tileArray[x][y - 1] == type) ||
                    (isInBounds(x, y + 1) && tileArray[x][y + 1] == type));
        } else return false;
    }

    //Checks if there are any moves left on the board.
    private boolean noMoreMoves() {

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tileArray[i][j] != 0 && hasNeighbors(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void showEndDialog() {
        new AlertDialog.Builder(activity).setTitle("G A M E  O V E R")
                .setCancelable(false)
                .setPositiveButton("got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                })
                .show();
    }

    //"Deletes" the tiles.
    //If tile is not of the same type (which will be deleted), add the tile to markedTiles so that they can be incremented.
    private void deleteTile(int x, int y, int type) {

        if (isInBounds(x, y) && tileArray[x][y] != 0) {
            if (tileArray[x][y] == type) {
                tileArray[x][y] = 0;

                deleteTile(x - 1, y, type);
                deleteTile(x + 1, y, type);
                deleteTile(x, y - 1, type);
                deleteTile(x, y + 1, type);
            } else {
                markedTiles[x][y] = type;
            }
        }
    }
}
