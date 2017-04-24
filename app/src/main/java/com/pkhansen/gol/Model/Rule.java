package com.pkhansen.gol.Model;

import java.util.Arrays;

/* This class handles the rules for the application. A board object is
 * imported, manipulated and returned to the calling method.
 *
 */

public class Rule {

    ////INSTANCE VARIABLES
    public byte[][] currentBoard;
    public byte[][] ruledBoard;
    public byte[][] conwaysBoard;
    private static int[] survivor = {2, 3};
    private static int[] born = {3};


    public static int[] getSurvivor() {
        return survivor;
    }

    public static int[] getBorn() {
        return born;
    }

    // Class constructor
    public Rule (byte[][] currentBoard) {
        this.currentBoard = currentBoard;
    }


    public byte[][] getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(byte[][] board) {
        this.currentBoard = board;
    }

    // Returns next generation values
    @Override
    public String toString(){
        String output = "";

        for (int row = 0; row < currentBoard.length; row++) {
            for (int col = 0; col < currentBoard[0].length; col++){
                output = output + currentBoard[row][col];
            }
        }
        return output;
    }

    // Rules to invert the board (dead becomes alive vice versa)
    public byte[][] invertBoard() {

        ruledBoard = new byte[currentBoard.length][currentBoard.length];

        for (int k = 0; k < ruledBoard.length; k++) {

            for (int l = 0; l < ruledBoard.length; l++ ) {
                if (currentBoard[k][l] == 1) {
                    ruledBoard[k][l] = 0;
                } else {
                    ruledBoard[k][l] = 1;
                }

            }
        }

        return ruledBoard;
    }


    public static void setRules(int[] s, int[] b) {
        survivor = s;
        born = b;
    }

    public void checkRule (int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println("");
    }

    public void checkRules () {
        checkRule(survivor);
        checkRule(born);
    }

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

    // Conways Game of life Rules (B3S23)
    public byte[][] conwaysBoardRules() {

        conwaysBoard = new byte[currentBoard.length][currentBoard[0].length];

        for (int y = 0; y < conwaysBoard.length; y++) {

            for (int x = 0; x < conwaysBoard.length; x++) {

                int cellState = currentBoard[y][x];

                conwaysBoard[y][x] = checkIfOnOrOff(countNeighbor(currentBoard, y, x), cellState);
            }
        }
        return conwaysBoard;

    }


    // Counts the neighbor of a cell and returns value
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


    // Methods that checks whether a cell has a neighbor or not
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

