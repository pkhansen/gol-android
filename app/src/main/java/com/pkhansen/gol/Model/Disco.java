package com.pkhansen.gol.Model;
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

    /**
     * Returns a random color from the mColors array
     * @return - int representing a given color
     */
    public int getColor() {
        mNewColor = false;
        String color;
        int intColor = 1;

        // While loop that makes sure that the random color isn't the same as last one.
        // Makes for a more fluid animation as a whole.
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
