package com.pkhansen.gol;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pkhansen.gol.Model.Rule;


public class GameViewer extends View {

    // TODO - Reset GameViewer if a new String is passed trough
    // TODO - JAVADOC *BLERGH*
    // TODO - More Controls
    // TODO - Create separate thread for bitmap processing


    private boolean mIsAnimating;

    // GAME BOARDS
    // Temp array for holding mBoard data
    private static byte[][] mBoard;
    // Array that holds the originally drawn board for resetting
    private static byte[][] mOrgBoard;

    // GRAPHIC VARIABLES
    // Temp array used as helper to update mRule
    private static Bitmap mBitmap;
    // Upscaled BitMap that is drawn
    private static Bitmap mScaledBmp;
    // With of the drawn recatngle of each cell

    // SUPPORT VARIABLES FOR DRAWING
    private int mXOffset;
    private int mYOffset;
    private int mDefYOffset;
    private int mDefXOffset;
    private int mTempX;
    private int mTempY;
    private int mScreenWidth;
    private int mBmpSize;
    private int mRectWidth;
    private int[] mRectWidths;

    // GAME SPEED VARIABLES
    private int mGameSpeed;
    private int[] mGameSpeeds;

    private Color cellColor;
    private Color backgroundColor;

    // GAME RULES
    private Rule mRule;

    public boolean isAnimating() {
        return mIsAnimating;
    }

    // FUN
    private Disco mDisco;
    private boolean mDiscoIsOn;
    private int mDiscoCounter;

    public GameViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGameViewer();

    }

    private void initGameViewer() {
        mRectWidth = 0;
        mScreenWidth = getScreenWidth();
        mIsAnimating = false;
        initGameSpeeds();
        mDiscoIsOn = false;
        mDisco = new Disco();
    }

    private void initGameSpeeds() {

        mGameSpeed = 80;
        mGameSpeeds = new int[4];

        // Sets up the different gamespeed based on the original speed.
        for (int i = 0; i < mGameSpeeds.length; i++) {
            double factor = ((0.5 * i) + 0.5);
            System.out.println(factor);
            double tempGameSpeed = factor * mGameSpeed;
            mGameSpeeds[mGameSpeeds.length - 1 - i] = (int) tempGameSpeed;
        }
    }

    private void setColors (Color cell, Color bg) {
        this.cellColor = cell;
        this.backgroundColor = bg;
    }

    // Sets up the starting offsets for drawing as well as offsets for the reset method
    private void setDefaultOffsets() {
        mYOffset = - (mScreenWidth / 4);
        mXOffset = mYOffset;
        mDefYOffset = mYOffset;
        mDefXOffset = mXOffset;


    }

    // Support method for resetting the board.
    // Sets the offsets used for drawing as the defaults from when the board was initialised
    private void resetOffsets() {
        mYOffset = mDefYOffset;
        mXOffset = mDefXOffset;
    }

    /**
     * Creates a Bitmap based on the array that represents the board.
     * This is then scaled and updates the member variable mScaledBmp
     *
     * @param array - Array that represents the game board
     */
    public void createBmp (byte[][] array) {
        mBitmap = Bitmap.createBitmap(array[0].length, array.length, Bitmap.Config.ARGB_8888);
        if (mRectWidth == 0) {
            initRectWith(array);
        }

        mBmpSize = (int) Math.floor((mRectWidth * array.length) * 1.5);

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if (array[y][x] == 1) {
                    mBitmap.setPixel(x, y, Color.BLACK);
                }
                else {
                }
            }
        }
        mScaledBmp = Bitmap.createScaledBitmap(mBitmap, mBmpSize, mBmpSize, false);
    }

    private void initRectWith(byte[][] array) {
        mRectWidth = (mScreenWidth / array.length);
        mRectWidths = new int[3];
        mRectWidths[0] = (int) (mRectWidth * 0.4);
        mRectWidths[1] = (int) (mRectWidth * 0.8);
        mRectWidths[2] = mRectWidth;
    }

    /**
     * Initialises and updates the bitmap.
     * The onDraw method is then called with invalidate() so that the bitmap is displayed.
     * @param array
     */
    public void initArray(byte[][] array) {
        // Initialises the local mBoard variable as well as mOrgBoard
        if (mBoard == null) {
            mBoard = array;
            mOrgBoard = array;
        }
        setDefaultOffsets();
        createBmp(mBoard);
        invalidate();
    }

    /**
     * Resets the bitmap to its original state
     */
    public void reset() {
        if (mIsAnimating) {
            startStop();
        }
        GameOfLifeActivity.resetSizeSeekBar();
        setRectWidth(2);
        resetOffsets();
        mBoard = mOrgBoard;
        createBmp(mBoard);
        invalidate();
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    // General drawing method that also contains a REALLY primitive animation function
    @Override
    protected void onDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mDiscoCounter += 1;
        canvas.drawBitmap(mScaledBmp, mXOffset, mYOffset, null);

        if (mDiscoIsOn && mDiscoCounter >= 5) {
            GameOfLifeActivity.setColor(mDisco.getColor());
            mDiscoCounter = 0;
        }

        if (mIsAnimating) {
            nextGeneration();
            createBmp(mBoard);

            SystemClock.sleep(mGameSpeed);
            invalidate();

        }
    }

    /**
     * Updates the mBoard with the standard conway rules
     */
    private void nextGeneration() {
        mRule = new Rule(mBoard);
        mBoard = mRule.conwaysBoardRules();
        createBmp(mBoard);
    }

    /**
     * Starts or stops the animation depending on the current state.
     */
    public void startStop() {
        if (this.mIsAnimating) {
            this.mIsAnimating = false;
        }
        else {
            this.mIsAnimating = true;
            invalidate();
        }
    }

    /**
     * Updates the mGameSpeed to the game speed found in the mGameSpeeds array with the given int
     * @param i - Value that defines what game speed to get. In this case it's coming from a seekBar.
     */
    public void setGameSpeed(int i) {
        mGameSpeed = mGameSpeeds[i];
    }

    public void setRectWidth(int i) {
        mRectWidth = mRectWidths[i];
    }

    /**
     * Method for supporting drag motions
     */
    public boolean onTouchEvent(MotionEvent event) {
        // Variables that keeps track the user touch
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Updates the temporary X and Y coordinate at first touch
                mTempX = touchX;
                mTempY = touchY;
            case MotionEvent.ACTION_MOVE:
                // Updates the member variables that offsets the drawing
                // according to the users drag movement
                mXOffset = mXOffset + (touchX - mTempX);
                mYOffset = mYOffset + (touchY - mTempY);

                //Updates the temporary X and Y coordinate
                // in case user drags in another direction
                mTempX = touchX;
                mTempY = touchY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void toggleDisco() {
        if (mDiscoIsOn) {
            mDiscoIsOn = false;
        }
        else {
            mDiscoIsOn = true;
        }
    }
}
