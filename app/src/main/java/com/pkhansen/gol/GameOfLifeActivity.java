package com.pkhansen.gol;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class GameOfLifeActivity extends AppCompatActivity{
    String mString;
    ImageView mQRImage;
    int mScreenWidth;
    private byte[][] mArray;
    private Button mStart;
    private Button mReset;
    private GameViewer mGameViewer;
    private View mLoadingSpinner;
    private int mAnimationDur;
    private SeekBar mSpeedBar;
    private Button mDisco;
    private static SeekBar mSizeBar;
    private static ConstraintLayout mConstraintLayout;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gol);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialises the different view objects
        mStart = (Button) findViewById(R.id.btn_is_animating);
        mReset = (Button) findViewById(R.id.btn_reset);
        mGameViewer = (GameViewer) findViewById(R.id.gameViewer);
        mLoadingSpinner = findViewById(R.id.loading_spinner);
        mSpeedBar = (SeekBar) findViewById(R.id.seekBar_Speed);
        mSizeBar = (SeekBar) findViewById(R.id.seekBar_Size);
        mDisco = (Button) findViewById(R.id.btn_disco);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.contraintLayout);

        mAnimationDur = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loadingAnimation();

        // Connects the string that was created from the previous activity
        mString = getIntent().getExtras().getString("TextField");

        mScreenWidth = getScreenWidth();

        // Generates GameOfLifeActivity code and saves it as a Array.
        mArray = generateQRCode(mString);
        mArray = makeGameBoardBigger(mArray);

        mGameViewer.initArray(mArray);


        // Listener for the Start/Stop button
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameViewer.startStop();
                changeStartStopTxt();
            }
        });

        // Listener for the reset button
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameViewer.reset();
                changeStartStopTxt();
            }
        });

        // Listener for the disco button
        mDisco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameViewer.toggleDisco();
                changeDiscoTxt();
            }
        });

        mSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGameViewer.setGameSpeed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGameViewer.setRectWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public static void resetSizeSeekBar() {
        mSizeBar.setProgress(2);
    }

    private void loadingAnimation() {
        View[] viewArr = {mStart, mGameViewer, mReset};

        for (View views : viewArr) {
            views.setAlpha(0f);
            views.setVisibility(View.VISIBLE);
        }

        for (View views: viewArr) {
            views.animate()
                    .alpha(1f)
                    .setDuration(mAnimationDur)
                    .setListener(null);
        }

        mLoadingSpinner.animate()
                .alpha(0f)
                .setDuration(mAnimationDur)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingSpinner.setVisibility(View.GONE);
                    }
                });
    }

    private void changeDiscoTxt() {
        if (mDisco.getText().equals("DISCO?")) {
            mDisco.setText("WHITE");
        }
        else {
            mDisco.setText("DISCO?");
            setColor(Color.WHITE);
        }
    }

    private void changeStartStopTxt() {
        if (mStart.getText().equals("Start") && mGameViewer.isAnimating()) {
            mStart.setText("Stop");
        }
        else {
            mStart.setText("Start");
        }
    }

    public static void setColor(int color) {
        mConstraintLayout.setBackgroundColor(color);
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Creates QR code an stores it as a 2D byte array.
     *
     * @param s - String that is converted to a QR code.
     * @return - 2D byte array that represents the QR code.
     */
    public byte[][] generateQRCode(String s) {

        QRCodeWriter writer = new QRCodeWriter();

        try {
            // Lager et BitMatrix med GameOfLifeActivity.
            BitMatrix bitMatrix = writer.encode(s, BarcodeFormat.QR_CODE, mScreenWidth, mScreenWidth);
            byte[][] array;

            int[] topLeft = bitMatrix.getTopLeftOnBit();


            int top = topLeft[1];
            int left = topLeft[0];
            int count = 0;
            while (bitMatrix.get(left, top)) {
                count++;
                left++;
            }
            left = topLeft[0];

            // Gives how many pixels that makes up a cell.
            int cellSize = (count/7);
            int numberOfCellsRow = (1080 - left) / cellSize;

            array = new byte[numberOfCellsRow][numberOfCellsRow];
            for (int y = 0; y < numberOfCellsRow; y++) {
                for (int x = 0; x < numberOfCellsRow; x++) {
                    if (bitMatrix.get((x*cellSize)+left, (y*cellSize)+top)){
                        array[y][x] = 1;
                    }
                    else {
                        array[y][x] = 0;
                    }
                }
            }

            return array;


        } catch (WriterException e) {
            //Log.e("GameOfLifeActivity ERROR", ""+e);
            return null;
        }

    }

    // Places the board in a bigger array.
    private byte[][] makeGameBoardBigger(byte[][] arr) {
        int marginHeight = (int) (arr.length * 0.5);
        int heightOfArray = (int) Math.floor(arr.length + (marginHeight * 2));
        int widthOfArray = heightOfArray;
        int marginWidth = marginHeight;

        byte[][] newArr = new byte[heightOfArray][widthOfArray];

        for (int y = 0; y < arr.length; y++) {
            for (int x = 0; x < arr[y].length; x++) {
                newArr[y+marginHeight][x+marginWidth] = arr[y][x];
            }
        }
        return newArr;
    }

    private void draw (Canvas canvas, Paint pain) {

    }

}
