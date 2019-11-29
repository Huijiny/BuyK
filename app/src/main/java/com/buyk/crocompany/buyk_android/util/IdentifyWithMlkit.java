package com.buyk.crocompany.buyk_android.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.buyk.crocompany.buyk_android.Event.EndIdentifyImages;
import com.buyk.crocompany.buyk_android.Event.LoginEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.annotations.NonNull;

public class IdentifyWithMlkit {

    public ArrayList<Boolean> IdentifyWithMlkit(Context context, ArrayList<String> images) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (String image : images) {
            uris.add(getUriFromPath(context, image));
        }

        FirebaseVisionImage firebaseVisionimage;
        AtomicBoolean isMotorcycle  = new AtomicBoolean(false);
        ArrayList<Boolean> isMotocycles = new ArrayList<>();

        for(int i= 0; i <uris.size(); i++ ) {
            try {
                Log.e("-1","-1");
                firebaseVisionimage = FirebaseVisionImage.fromFilePath(context, uris.get(i));
                Log.e("0","0");

                FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                        .getVisionLabelDetector();
                Log.e("1","1");
                int finalI = i;
                Task<List<FirebaseVisionLabel>> result =
                        detector.detectInImage(firebaseVisionimage)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                            @Override
                                            public void onSuccess(List<FirebaseVisionLabel> labels) {
                                                labels.stream().forEach(label -> {
                                                    if(label.getLabel().equals("Motorcycle") && label.getConfidence() > 0.8) {
                                                        isMotocycles.add(true);
                                                        Log.e("label",label.getLabel());
//                                                        isMotorcycle.set(true);


                                                    }
                                                    if(finalI == uris.size()-1) {
                                                        Log.e("finaI",String.valueOf(finalI));
                                                        Log.e("uris.size",String.valueOf(uris.size()));

                                                        EventBus.getDefault().post(new EndIdentifyImages(isMotocycles));

                                                    }
                                                });
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                            }
                                        });
                Log.e("2","2");

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("3","3");

            }
        }
        return isMotocycles;

    }

    public Uri getUriFromPath(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }


}
