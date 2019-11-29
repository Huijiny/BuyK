package com.buyk.crocompany.buyk_android;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mcamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mcamera = camera;
        if(camera==null){
            Log.e("camera가 null","dd");
        }


        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder =getHolder();
        if(mHolder==null){
            Log.e("mHolder가 null","dd");
        }
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



public void surfaceCreated(SurfaceHolder holder) {
    // The Surface has been created, now tell the camera where to draw the preview.

        try{
           mcamera.setPreviewDisplay(mHolder);
            mcamera.startPreview();
        }catch (Exception e){
            Log.e("CameraSurfaceView","Failed to set camera preview.",e);
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        if(mcamera!=null) {
            mcamera.stopPreview();
            mcamera.release();
            mcamera = null;
        }else{
            Log.e("camera 없어..","ㅇㅇ");
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            mcamera.setDisplayOrientation(90);

        }

        // stop preview before making changes
        try {
            mcamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mcamera.setPreviewDisplay(mHolder);
            mcamera.startPreview();

        } catch (Exception e){
            Log.d("dd", "Error starting camera preview: " + e.getMessage());
        }
    }

}
