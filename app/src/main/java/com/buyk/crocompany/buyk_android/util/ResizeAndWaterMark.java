package com.buyk.crocompany.buyk_android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

import com.buyk.crocompany.buyk_android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResizeAndWaterMark extends Activity{
    ArrayList<String> imageList = new ArrayList<String>(0);
    Context context;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hh:mm:ss");
    String getTime = sdf.format(date);
    ArrayList<File> small_images = new ArrayList<File>();
    ArrayList<File> large_images = new ArrayList<File>();

    public ArrayList<File> getSmall_images() {
        return small_images;
    }

    public ArrayList<File> getLarge_images() {
        return large_images;
    }

    public ResizeAndWaterMark(ArrayList<String> list, Context context)
    {
        this.context = context;
        imageList = list;
    }

    public Bitmap addWatermark( Bitmap source) {
        int w, h;
        Canvas c,wmCanvas;
        Paint paint,wmPaint;
        Bitmap bmp, watermark;
        Matrix matrix;
        float scale;
        RectF r;
        File large;
        w = source.getWidth();
        Log.e("가로길이",String.valueOf(w));
        h = source.getHeight();
        Log.e("세로길이",String.valueOf(h));
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Log.e("비트맵 생성",String.valueOf(bmp));
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);

        // Copy the original bitmap into the new one
        c = new Canvas(bmp);
        c.drawBitmap(source, 0, 0, paint);
        // Load the watermark
        watermark = BitmapFactory.decodeResource(context.getResources(), R.drawable.watermark);
        watermark = watermark.copy(Bitmap.Config.ARGB_8888,true);
        watermark.setHasAlpha(true);

        watermark = adjustOpacity(watermark,80);
       // wmCanvas = new Canvas(watermark);
        //wmCanvas.drawColor(100,);
        //wmPaint = new Paint();
        //wmPaint.setAlpha(100);

        //wmCanvas.drawBitmap(watermark,0,0,wmPaint);
        //onDraw(wmCanvas,watermark);
         Log.e("워터마크 생성",String.valueOf(watermark));
        // Scale the watermark to be approximately 40% of the source image height
        scale = (float) (((float) w*0.24) / (float) watermark.getHeight());
        // Create the matrix
        matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Determine the post-scaled size of the watermark
        r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
        Log.e("width",String.valueOf(r.width()));
        Log.e("height",String.valueOf(r.height()));
        matrix.mapRect(r);
        // Move the watermark to the bottom right corner
        matrix.postTranslate(w /2-(r.width()/2), h /2-(r.height()/2));
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint);

        // Free up the bitmap memory
        watermark.recycle();
        resizeToSmall(bmp);
        resizeToMiddle(bmp);
        return bmp;
    }

    private Bitmap adjustOpacity(Bitmap bitmap, int opacity)
    {
        Bitmap mutableBitmap = bitmap.isMutable()
                ? bitmap
                : bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }
    public Bitmap resizeToMiddle(Bitmap bitmap)
    {
        File large;
        Log.e("resizingToSmall","dd");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        int width = 700; // 축소시킬 너비
        int height = 700; // 축소시킬 높이
        float bmpWidth = bitmap.getWidth();
        float bmpHeight = bitmap.getHeight();
        Log.e("리사이징되기 전 사진 width",String.valueOf(bmpWidth));
        Log.e("리사이징 되기 전 사진 height",String.valueOf(bmpHeight));

        if (bmpWidth > width) {
            // 원하는 너비보다 클 경우의 설정
            float mWidth = bmpWidth / 100;
            float scale = width/ mWidth;
            bmpWidth *= (scale / 40);
            bmpHeight *= (scale / 40);
        } else if (bmpHeight > height) {
            // 원하는 높이보다 클 경우의 설정
            float mHeight = bmpHeight / 100;
            float scale = height/ mHeight;
            bmpWidth *= (scale / 40);
            bmpHeight *= (scale / 40);
        }

        Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmap, (int) bmpWidth, (int) bmpHeight, true);
        Log.e("리사이징된 사진 width",String.valueOf(resizedBmp));
        Log.e("리사이징된 사진 height",String.valueOf(resizedBmp.getHeight()));
        large = saveBitmaptoJpeg(resizedBmp,"buyK","BUYK"+getTime+"_small"+String.valueOf(Math.random()));
        large_images.add(large);
        if(large_images.size()!=0)
        {
            for(int i=0;i<large_images.size();i++)
            {
                Log.e("large image",String.valueOf(large_images.get(i)));
            }
        }
        return resizedBmp;
    }
    public Bitmap resizeToSmall(Bitmap bitmap)
    {
        File small;
        Log.e("resizingToSmall","dd");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        int width = 300; // 축소시킬 너비
        int height = 300; // 축소시킬 높이
        float bmpWidth = bitmap.getWidth();
        float bmpHeight = bitmap.getHeight();
        Log.e("리사이징되기 전 사진 width",String.valueOf(bmpWidth));
        Log.e("리사이징 되기 전 사진 height",String.valueOf(bmpHeight));

        if (bmpWidth > width) {
            // 원하는 너비보다 클 경우의 설정
            float mWidth = bmpWidth / 100;
            float scale = width/ mWidth;
            bmpWidth *= (scale / 100);
            bmpHeight *= (scale / 100);
        } else if (bmpHeight > height) {
            // 원하는 높이보다 클 경우의 설정
            float mHeight = bmpHeight / 100;
            float scale = height/ mHeight;
            bmpWidth *= (scale / 100);
            bmpHeight *= (scale / 100);
        }

        Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmap, (int) bmpWidth, (int) bmpHeight, true);
        Log.e("리사이징된 사진 width",String.valueOf(resizedBmp));
        Log.e("리사이징된 사진 height",String.valueOf(resizedBmp.getHeight()));
        small = saveBitmaptoJpeg(resizedBmp,"buyK","BUYK"+getTime+"_small"+String.valueOf(Math.random()));
        small_images.add(small);
        if(small_images.size()!=0)
        {

            for(int i=0;i<small_images.size();i++)
            {
                Log.e("small image",String.valueOf(small_images.get(i)));
            }
        }
        return resizedBmp;
    }

    public static File saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath(); // Get Absolute Path in External Sdcard
         String foler_name = "/"+folder+"/";
         String file_name = name+".jpg";
         String string_path = ex_storage+foler_name+file_name;
         File file_path=null;

             file_path = new File(string_path);
        try {
            if(!file_path.createNewFile())
            {
                Log.e("파일 안만들어짐","This file is already exist " + file_path.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("fileEx","TLqkf");
        }
        if(!file_path.isDirectory()) {
                 file_path.mkdirs();
             }


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(string_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

             //Log.e("파일로 만들어짐?",file_path.getAbsolutePath());

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  file_path;
    }



}
