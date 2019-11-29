package com.buyk.crocompany.buyk_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;

public class OfferingImageDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offering_image_detail);
        mDemoSlider = (SliderLayout)findViewById(R.id.imageDatailSlider);

        imageSliderSet();

    }
    public void imageSliderSet(){
        Intent intent = getIntent();
        ArrayList<String>imageList = new ArrayList<>();
        imageList.addAll(intent.getStringArrayListExtra("imageList"));
        for(int i=0; i<imageList.size();i++){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(API_URL+imageList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener((this));

            //add your extra information
            textSliderView.bundle(new Bundle());
            mDemoSlider.addSlider(textSliderView);
        }


    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
