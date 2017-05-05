package com.pkhansen.gol.Model;

import java.util.Arrays;

/**
 * This class handles the rules for the application. A board object is
 * imported, manipulated and returned to the calling method.
 *
 * This is a stripped down version of the one we use in the actual project
 *
 */

public class Rule {

    ////INSTANCE VARIABLES
    public byte[][] currentBoard;
    public byte[][] ruledBoard;
    public byte[][] conwaysBoard;
    private static int[] survivor = {2, 3};
    private static int[] born = {3};

    public Rule (byte[][] currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     * Checks if a given cellState combined with number of neighbors means the cell will survive
     * @param neighbors  number of neighbors
     * @param cellState  current cellState (dead or alive)
     * @return  a byte of 1 or 0 where 1 is alive and 0 is dead.
     */
    public byte checkIfOnOrOff(int neighbors, int cellState) {
        if (cellState == 1) {
            for (int s : survivor) {
                if (s == neighbors) {
                    return 1;
                }
            }
        }
        else if (cellState == 0) {
            for (int b : born) {
                if (b == neighbors) {
                    return 1;
                }
            }
        }

        return 0;
    }

    /**
     * Returns a ruled 2d byte array
     * @return 2D byte arrey with elements of type byte
     */
    public byte[][] applyRules() {

        conwaysBoard = new byte[currentBoard.length][currentBoard[0].length];

        for (int y = 0; y < conwaysBoard.length; y++) {

            for (int x = 0; x < conwaysBoard.length; x++) {

                int cellState = currentBoard[y][x];

                conwaysBoard[y][x] = checkIfOnOrOff(countNeighbor(currentBoard, y, x), cellState);
            }
        }
        return conwaysBoard;

    }

    /**
     * Counts the number of neighbors surrounding the given cell
     * @param y  y coordinate
     * @param x  x coordinate
     * @return  the number of neighbors
     */
    public int countNeighbor(byte[][] board, int y, int x){

        board = currentBoard;

        int neighborsCounter = 0;

        if (neighborTopLeft(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborOver(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborTopRight(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborLeft(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborRight(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborBottomLeft(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborUnder(y,x)) {
            neighborsCounter += 1;
        }
        if (neighborBottomRight(y,x)) {
            neighborsCounter += 1;
        }

        return neighborsCounter;

    }

    private boolean neighborOver(int y, int x) {

        if (y - 1 != - 1) {

            if (currentBoard[y-1][x] == 1) {
                return true;
            }

        }
        return false;

    }

    private boolean neighborUnder(int y, int x) {

        int boardLength = currentBoard.length;

        if (y + 1 < boardLength) {

            if (currentBoard[y+1][x] == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean neighborLeft(int y, int x) {

        if (x - 1 != - 1) {

            if (currentBoard[y][x-1] == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean neighborRight(int y, int x) {

        int boardLength = currentBoard.length;

        if (x + 1 < boardLength) {

            if (currentBoard[y][x+1] == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean neighborTopLeft(int y, int x) {

        if ((y - 1 != - 1) && (x - 1 != - 1)) {

            if (currentBoard[y-1][x-1] == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean neighborTopRight(int y, int x) {

        int boardLength = currentBoard.length;

        if ((y - 1 != - 1) && (x + 1 < boardLength)) {

            if (currentBoard[y-1][x+1] == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean neighborBottomLeft(int y, int x) {

        int boardLength = currentBoard.length;

        if ((y + 1 < boardLength) && (x - 1 != - 1)) {

            if (currentBoard[y+1][x-1] == 1) {
                return true;
            }
        }

        return false;
    }

    private boolean neighborBottomRight(int y, int x) {

        int boardLength = currentBoard.length;

        if ((y + 1 < boardLength) && (x + 1 < boardLength)) {

            if (currentBoard[y+1][x+1] == 1) {
                return true;
            }
        }

        return false;
    }

}

