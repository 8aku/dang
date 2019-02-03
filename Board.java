package com.example.r.dangver1;
import java.util.Random;

public class Board {

    private int [][] tileArray;
    private int [][] markedTiles;
    final int GRID_SIZE = 16;
    private final int NUM_TYPES = 4;

    public Board() {
        tileArray = new int[GRID_SIZE][GRID_SIZE];
        markedTiles = new int[GRID_SIZE][GRID_SIZE];

        Random rand = new Random();
        int min = 1;
        int max = NUM_TYPES -1;

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
        return ((x < GRID_SIZE && x >=0) && (y < GRID_SIZE && y >=0));
    }

    //If the tile has neighbors of the same type, delete the tiles and increment surrounding tiles.
    public void boardClicked(int posX, int posY) {
        if (hasNeighbors(posX, posY)) {
            resetMarkedTiles();
            deleteTile(posX, posY, tileArray[posX][posY]);
            incrementTiles();
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
    private void incrementTile(int x, int y, int type){
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
        }
        else return false;
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
