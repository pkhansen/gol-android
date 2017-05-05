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

import com.pkhansen.gol.Model.Disco;
import com.pkhansen.gol.Model.Rule;


public class GameViewer extends View {


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
    private int mCellSize;
    private int[] mCellSizes;

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

    // Initializes some of the key variables used.
    private void initGameViewer() {
        mCellSize = 0;
        mScreenWidth = getScreenWidth();
        mIsAnimating = false;
        initGameSpeeds();
        mDiscoIsOn = false;
        mDisco = new Disco();
    }

    /**
     * Initializes the main game speed
     * and also sets up an array of other game speeds that can be selected through the GUI.
      */
    private void initGameSpeeds() {

        mGameSpeed = 50;
        mGameSpeeds = new int[4];

        // Sets up the different games peed based on the original speed.
        for (int i = 0; i < mGameSpeeds.length; i++) {
            double factor = ((0.5 * i) + 0.5);
            System.out.println(factor);
            double tempGameSpeed = factor * mGameSpeed;
            mGameSpeeds[mGameSpeeds.length - 1 - i] = (int) tempGameSpeed;
        }
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
        // Creates a bitmap that fits the size of the array that's going to be used to draw to it.
        mBitmap = Bitmap.createBitmap(array[0].length, array.length, Bitmap.Config.ARGB_8888);

        // Initializes the variable representing cell size if the method is running for the first time.
        if (mCellSize == 0) {
            initCellSize(array);
        }

        // Calculates the bitmap size based on the cellsize and size
        // (basically screen width in this case) for the array.
        mBmpSize = (int) Math.floor((mCellSize * array.length) * 1.5);

        // "Draws" to the bitmap using the array created from the QR code
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if (array[y][x] == 1) {
                    mBitmap.setPixel(x, y, Color.BLACK);
                }
                else {
                }
            }
        }
        // Scales the bitmap that is later drawn
        mScaledBmp = Bitmap.createScaledBitmap(mBitmap, mBmpSize, mBmpSize, false);
    }

    /**
     * Initializes the main cell size along with some of
     * the different sizes that can be selected in the GUI
     * @param array
     */
    private void initCellSize(byte[][] array) {
        mCellSize = (mScreenWidth / array.length);
        mCellSizes = new int[3];
        mCellSizes[0] = (int) (mCellSize * 0.4);
        mCellSizes[1] = (int) (mCellSize * 0.8);
        mCellSizes[2] = mCellSize;
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
     * Resets the gameviewer almost completely.
     * Speed and DISCO? will be unchanged.
     */
    public void reset() {
        if (mIsAnimating) {
            startStop();
        }
        GameOfLifeActivity.resetSizeSeekBar();
        setCellSize(2);
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

        // Paces out the change of color to make it a little less intrusive
        if (mDiscoIsOn && mDiscoCounter >= 5) {
            GameOfLifeActivity.setColor(mDisco.getColor());
            mDiscoCounter = 0;
        }

        // Checks if its animating and puts the computer to sleep based on the current game speed
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
        mBoard = mRule.applyRules();
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

    public void setCellSize(int i) {
        mCellSize = mCellSizes[i];
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

    /**
     * Toggles the rhytmic background change on/off
     */
    public void toggleDisco() {
        if (mDiscoIsOn) {
            mDiscoIsOn = false;
        }
        else {
            mDiscoIsOn = true;
        }
    }


}
