package com.pkhansen.gol;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.pkhansen.gol.Model.Rule;


public class GameViewer extends View {

    // TODO - Have GameViewer height and width in activity layout scale along with bmp and array
    // TODO - Reset GameViewer if a new String is passed trough
    // TODO - Reset Button
    // TODO - JAVADOC *BLERGH*
    // TODO - More dynamic mBoard
    // TODO - Drag screen
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
    private int mMargin;
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
    }

    private void setBmpSize() {

    }

    public void createBmp (byte[][] array) {
        mBitmap = Bitmap.createBitmap(array[0].length, array.length, Bitmap.Config.ARGB_8888);
        if (mRectWidth == 0) {
            mRectWidth = (mScreenWidth) / array.length;
        }
        //mMargin = (mScreenWidth - (array.length * mRectWidth)) / 2;
        //mBmpSize = mRectWidth * array[0].length;
        mBmpSize = mScreenWidth * 2;
        mMargin = -(mScreenWidth/2);

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

    public void drawArray(byte[][] array) {
        // Initialises the local mBoard variable as well as mOrgBoard
        if (mBoard == null) {
            mBoard = array;
            mOrgBoard = array;
        }
        createBmp(mBoard);
        invalidate();
    }

    public void reset() {
        if (mIsAnimating) {
            startStop();
        }
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
        canvas.drawBitmap(mScaledBmp, mMargin, mMargin, null);

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

    public void startStop() {
        if (this.mIsAnimating) {
            this.mIsAnimating = false;
        }
        else {
            this.mIsAnimating = true;
            invalidate();
        }
    }

}
