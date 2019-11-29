package com.buyk.crocompany.buyk_android.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buyk.crocompany.buyk_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraPermissionView extends Fragment {
    @BindView(R.id.cameraPermissionBtn)
    Button cameraPermission;

    public void CameraPermissionView()
    {

    }

    public static CameraPermissionView newInstance() {
        Bundle args = new Bundle();
        CameraPermissionView fragment = new CameraPermissionView();
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
        View view = inflater.inflate(R.layout.camera_permission_view, container, false);
        ButterKnife.bind(this,view);

        cameraPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getContext().getPackageName()));
                getContext().startActivity(intent);
            }
        });
        return view;
    }
}
