package com.buyk.crocompany.buyk_android.Fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.MainActivity;
import com.buyk.crocompany.buyk_android.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.auth.Session;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GalleryPermissionView extends Fragment {

    @BindView(R.id.galleryPermissionBtn)
    Button permissionBtn;

    public GalleryPermissionView() {
        // Required empty public constructor
    }

    public static GalleryPermissionView newInstance() {
        Bundle args = new Bundle();
        GalleryPermissionView fragment = new GalleryPermissionView();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_permission_view, container, false);
        ButterKnife.bind(this,view);

        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCheck();
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getContext().getPackageName()));
//                getContext().startActivity(intent);
            }
        });
        return view;
    }
    public void permissionCheck(){
        PermissionListener permissionListener= new PermissionListener() {

            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }

        };
        TedPermission.with(getContext())
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

}
