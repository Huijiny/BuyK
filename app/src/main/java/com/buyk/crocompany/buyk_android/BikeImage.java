package com.buyk.crocompany.buyk_android;

import android.graphics.Bitmap;

public class BikeImage {
        private Bitmap bike;
        BikeImage(Bitmap bitmap)
        {
            bike= bitmap;
        }

    public Bitmap getBike() {
        return bike;
    }
    public void setBike(Bitmap bitmap)
    {
        bike=bitmap;
    }

}
