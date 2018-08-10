package com.olukoye.hannah.movies_stage2;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by hannaholukoye on 11/08/2018.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}