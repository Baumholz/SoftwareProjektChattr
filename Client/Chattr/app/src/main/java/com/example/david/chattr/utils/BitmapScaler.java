package com.example.david.chattr.utils;

import android.graphics.Bitmap;

/**
 * Created by david on 16.01.18.
 */

public final class BitmapScaler {

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio = (float) width / (float) height;
        int recizedWidth = 640;
        int recizedHeight = Math.round(recizedWidth/aspectRatio);

        return Bitmap.createScaledBitmap(bitmap, recizedWidth, recizedHeight ,false);
    }
}
