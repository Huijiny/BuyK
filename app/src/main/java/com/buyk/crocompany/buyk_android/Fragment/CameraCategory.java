package com.buyk.crocompany.buyk_android.Fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.RecyclerViewAdapterInImageView;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraCategory extends Fragment {
    private Bitmap[] bitmaps={};
    View view;
    View parentView;
    RecyclerView recyclerView;
    RecyclerViewAdapterInImageView recyclerViewAdapterInImageView;
    @BindView(R.id.camera)
    CameraView camera;
    ImageView imageView ;
    Camera mcamera;
    ArrayList<Bitmap> listBikeBitmap=new ArrayList<>(0);

    public static CameraCategory newInstance() {

        Bundle args = new Bundle();
        CameraCategory fragment = new CameraCategory();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerViewAdapterInImageView = new RecyclerViewAdapterInImageView(getContext());
        view = inflater.inflate(R.layout.fragment_camera_category, container, false);
        ButterKnife.bind(this,view);
        //setUpCameraView();

        camera.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {}

            @Override
            public void onError(CameraKitError cameraKitError) {}

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Log.i("testing-image",String.valueOf(cameraKitImage.getBitmap()));
                Bitmap bitmap = cameraKitImage.getBitmap();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 2, bytes);
                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
                Log.e("camera path",path);

                recyclerViewAdapterInImageView = new RecyclerViewAdapterInImageView(getContext());
                String realFilePath = getRealPathFromURI(Uri.parse(path));
                recyclerViewAdapterInImageView.add(String.valueOf(realFilePath));
                recyclerViewAdapterInImageView.notifyDataSetChanged();

                recyclerView = (RecyclerView)getActivity().findViewById(R.id.image_recycler);
                recyclerView.setAdapter(recyclerViewAdapterInImageView);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {}

        });
        return view;

    }


   @OnClick(R.id.saveBtn)
   public void takeAphoto() {
    camera.captureImage();
   }



    @Override
    public void onResume() {
        super.onResume();
        camera.start();
    }

        @Override
        public void onPause() {
            camera.stop();
            super.onPause();
        }
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}
