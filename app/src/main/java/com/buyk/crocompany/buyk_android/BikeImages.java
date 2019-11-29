package com.buyk.crocompany.buyk_android;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.buyk.crocompany.buyk_android.Fragment.CameraCategory;
import com.buyk.crocompany.buyk_android.Fragment.CameraPermissionView;
import com.buyk.crocompany.buyk_android.Fragment.GalleryCategory;
import com.buyk.crocompany.buyk_android.Fragment.GalleryPermissionView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.android.segmented.SegmentedGroup;


public class BikeImages extends Fragment {

    RecyclerViewAdapterInImageView adapter;
    private static Uri mImageCaptureUri;
    public ArrayList<Bitmap> listBikeBitmap=new ArrayList<>(0);

    public static ArrayList<String>selected = new ArrayList<String>(0);
    String mCurrentPhotoPath;
    File file =null;
    ViewGroup rootView;
    private ArrayList<Bitmap> seletedImages;
    private String currentPhotoPath;
    String mImageCaptureName;
    private Activity activity;
    int viewId;
    protected boolean mIsVisibleToUser;
    ArrayList<File> resizedSmallImage = new ArrayList<>();
    ArrayList<File> resizedLargeImage = new ArrayList<>();

    @BindView(R.id.imageViewPager)ViewPager viewPager;
    @BindView(R.id.cameraTab)RadioButton cameraTab;
    @BindView(R.id.gallaryTab)RadioButton gallaryTab;
    @BindView(R.id.tabGroupInImage)SegmentedGroup tabGroup;
    @BindView(R.id.image_recycler)RecyclerView recyclerView;
    public static BikeImages newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        BikeImages fragment = new BikeImages();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof Activity){
            activity=(Activity)context;

        }
}

    @Override
    public void onStart() {
        super.onStart();
       // fragment have created
    }

    @Override
    public void onResume() {
        super.onResume();
       permissionCheck();
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    public static ArrayList<String> getSelected() {
        return selected;
    }

    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }

    public void onVisible(Boolean gallery,Boolean camera) {

            Adapter adapter = new Adapter(getChildFragmentManager(),gallery,camera);
            adapter.notifyDataSetChanged();
            viewPager.setAdapter(adapter);

    }

    public void onInVisible() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;

        if (isResumed()) { // fragment have created
            if (mIsVisibleToUser) {
                permissionCheck();
                ((soldBikeView)getActivity()).setDisabled();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, viewGroup, savedInstanceState);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.activity_bike_images, viewGroup, false);

        if (mIsVisibleToUser) {
            permissionCheck();
        }

        ButterKnife.bind(this,rootView);
        initWidget();
        setRecyclerView();
        return rootView;
    }
    public void permissionCheck()
    {
        int galleryPermissionCheck = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        boolean camera;
        boolean gallery;

        if(galleryPermissionCheck== PackageManager.PERMISSION_DENIED) {
            gallery = false;
            //onVisible(false);
        }else{
            gallery = true;
        }
        if(cameraPermissionCheck==PackageManager.PERMISSION_DENIED){
            camera = false;
        }else{
            camera = true;
        }
        onVisible(gallery,camera);
    }
    public void initWidget(){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Log.e("gallarytab", String.valueOf(position));
                        gallaryTab.setChecked(true);
                        cameraTab.setChecked(false);
                        break;
                    case 1:
                        Log.e("cameraTab", String.valueOf(position));
                        cameraTab.setChecked(true);
                        gallaryTab.setChecked(false);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.gallaryTab:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.cameraTab:
                        viewPager.setCurrentItem(1, true);
                        break;
                    default:
                        Log.e("아무것도 클릭안됨","err");
                        return;
                }
            }
        });
    }


    private void setRecyclerView(){

        recyclerView = (RecyclerView)rootView.findViewById(R.id.image_recycler);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        adapter=new RecyclerViewAdapterInImageView (getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    private class Adapter extends FragmentStatePagerAdapter {

        boolean cameraIsPermitted;
        boolean galleryIsPermitted;
        private android.support.v4.app.FragmentManager manager;
        public Adapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        public Adapter(android.support.v4.app.FragmentManager fm,Boolean gallery,Boolean camera)
        {
            super(fm);
            this.manager = fm;
            this.cameraIsPermitted = camera;
            this.galleryIsPermitted = gallery;
        }

        public android.support.v4.app.Fragment[] decidePermission(Boolean gallery, Boolean camera){
            android.support.v4.app.Fragment[] fragments = new android.support.v4.app.Fragment[2];
            if(gallery){
                fragments[0]=GalleryCategory.newInstance();
            }else{
                fragments[0]=GalleryPermissionView.newInstance();
            }
            if(camera){
                fragments[1]= CameraCategory.newInstance();
            }else{
                fragments[1]=CameraPermissionView.newInstance();
            }

            return fragments;
        }
        @Override
        public Fragment getItem(int position) {
           // android.support.v4.app.Fragment fragments[] = {GalleryCategory.newInstance(),CameraCategory.newInstance()};
            //android.support.v4.app.Fragment fragment2[] = {GalleryPermissionView.newInstance(),CameraCategory.newInstance()};
            android.support.v4.app.Fragment fragments[] =  decidePermission(galleryIsPermitted,cameraIsPermitted);
            return fragments[position];
            /*
            if(isPermitted==true)
            {
                Log.e("첫번째 프레그먼트 나옴","첫번째");
                return fragments[position];

            }else{
                Log.e("두번째 프레그먼트 나옴","두번째");
                return fragment2[position];
            }
*/
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



}