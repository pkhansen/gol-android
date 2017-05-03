package com.pkhansen.gol;
import android.graphics.Color;
import java.util.Random;

public class Disco {
    boolean mNewColor;
    int mLastPick = 0;

    private String[] mColors = {
            "#E98B8B",
            "#FFDF8E",
            "#A7C8E4",
            "#8C9EA9",
            "#93BDB8",
            "#E7DFBC",
            "#FAF6BA",
            "#E4F1C5",
            "#CFE7CE",
            "#D2E6DD"
    };

    public int getColor() {
        mNewColor = false;
        String color;
        int intColor = 1;
        while (!mNewColor) {
            Random randomGenerator = new Random();
            int rand = randomGenerator.nextInt(mColors.length);
            color = mColors[rand];
            intColor = Color.parseColor(color);
            if (intColor != mLastPick) {
                mNewColor = true;
            }
        }
        mLastPick = intColor;

        return intColor;
    }

}
