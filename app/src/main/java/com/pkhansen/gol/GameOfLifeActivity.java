package com.pkhansen.gol;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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


    GameViewer mGameViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gol);

        // Creates a GameViewer object that is linked to the GameViewer from the layout
        mGameViewer = (GameViewer) findViewById(R.id.gameViewer);

        // Init of field variables

        // Connects the string that was created from the previous activity
        mString = getIntent().getExtras().getString("TextField");

        mScreenWidth = getScreenWidth();

        // Generates GameOfLifeActivity code and saves it as a Array.
        mArray = generateQRCode(mString);
        mArray = makeGameBoardBigger(mArray);
        mGameViewer.drawArray(mArray);

        mStart = (Button) findViewById(R.id.btn_is_animating);
        mReset = (Button) findViewById(R.id.reset);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameViewer.startStop();
                changeStartStopTxt();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameViewer.reset();
                changeStartStopTxt();
            }
        });



    }

    private void changeStartStopTxt() {
        if (mStart.getText().equals("Start") && mGameViewer.isAnimating()) {
            mStart.setText("Stop");
        }
        else {
            mStart.setText("Start");
        }
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
            System.out.println(topLeft.length);


            int top = topLeft[1];
            int left = topLeft[0];
            int count = 0;
            while (bitMatrix.get(left, top)) {
                count++;
                left++;
            }
            System.out.println(count);
            left = topLeft[0];

            // Gives how many pixels that makes up a cell.
            int cellSize = (count/7);
            System.out.println("Cellsize: " + cellSize);
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
            return new byte[20][20];
        }

    }

    private byte[][] makeGameBoardBigger(byte[][] arr) {
        int marginHeight = (int) (arr.length);
        int heightOfArray = arr.length + (marginHeight * 2);
        int marginWidth = (int) (arr[0].length);
        int widthOfArray = arr[0].length + (marginWidth * 2);

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
