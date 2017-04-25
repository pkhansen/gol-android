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

    // TODO - Have GameViewer height and width in activity layout scale along with bmp and array
    // TODO - Reset GameViewer if a new String is passed trough
    // TODO - JAVADOC *BLERGH*
    // TODO - More dynamic mBoard
    // TODO - Controls



    private boolean mIsAnimating;
    // Temp array for holding mBoard data
    private static byte[][] mBoard;
    // Array that holds the originally drawn board for resetting
    private static byte[][] mOrgBoard;
    // Temp array used as helper to update mRule
    private static Bitmap mBitmap;
    // Upscaled BitMap that is drawn
    private static Bitmap mScaledBmp;
    // With of the drawn recatngle of each cell
    private int mRectWidth;
    // Current X position when drawing
    private int mXPoint;
    // Current Y position when drawing
    private int mYPoint;
    // Starting margin when drawing
    private int mXOffset;
    private int mYOffset;
    private int mTempX;
    private int mTempY;
    private int mScreenWidth;
    private int mBmpSize;
    private int mGameSpeed;

    private Rule mRule;

    public boolean isAnimating() {
        return mIsAnimating;
    }

    public GameViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGameViewer();
    }

    private void initGameViewer() {
        mRectWidth = 0;
        mXPoint = 0;
        mYPoint = 0;
        mScreenWidth = getScreenWidth();
        mIsAnimating = false;
        mGameSpeed = 100;
        setDefaultOffsets();
    }

    private void setDefaultOffsets() {
        mXOffset = -(mScreenWidth/2);
        mYOffset = -(mScreenWidth/2);
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
            mRectWidth = (mScreenWidth) / array.length;
        }

        mBmpSize = mScreenWidth * 2;

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if (array[y][x] == 1) {
                    mBitmap.setPixel(x, y, Color.BLACK);
                }
                else {
                }
            }
        }
        // TODO - Fix so that bitmap scales according to previous shape.
        mScaledBmp = Bitmap.createScaledBitmap(mBitmap, mBmpSize, mBmpSize, false);
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
        setDefaultOffsets();
        mBoard = mOrgBoard;
        createBmp(mBoard);
        invalidate();
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawBitmap(mScaledBmp, mXOffset, mYOffset, null);

        if (mIsAnimating) {
            nextGeneration();
            SystemClock.sleep(mGameSpeed);
            invalidate();
        }
    }

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

    // Implements touch controls
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTempX = touchX;
                mTempY = touchY;
            case MotionEvent.ACTION_MOVE:
                mXOffset = mXOffset + (touchX - mTempX);
                mYOffset = mYOffset + (touchY - mTempY);
                mTempX = touchX;
                mTempY = touchY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //invalidate();
                break;
        }
        return true;
    }
}
