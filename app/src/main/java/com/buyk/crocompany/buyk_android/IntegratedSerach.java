package com.buyk.crocompany.buyk_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.buyk.crocompany.buyk_android.Fragment.SearchCategory;
import com.buyk.crocompany.buyk_android.Fragment.SearchModel;
import com.buyk.crocompany.buyk_android.util.ClearEditText;

import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

public class IntegratedSerach extends AppCompatActivity  {

    ViewPager pager;
    ClearEditText clearEditText;
    EditText searchEditText;
    SegmentedGroup tabGroup;
    RadioButton modelTab;
    RadioButton categroyTab;
    public static String searchModelText;
    InputMethodManager inputMethodManager;
    SegmentedGroup segmentedGroup;

    String whereFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integrated_serach);
        initView();
        ButterKnife.bind(this);
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        Log.e("intent", String.valueOf(getIntent()));
    }


    public void initView(){
        searchEditText= findViewById(R.id.searchEditText);
        Intent intent = getIntent();
        whereFromIntent= String.valueOf(intent.getSerializableExtra("whereFromIntent"));
        Log.e("dd",whereFromIntent);
        if(whereFromIntent.equals("offeringList")) {
            segmentedGroup = findViewById(R.id.tabHost);
            segmentedGroup.setVisibility(View.GONE);
        }
        editTextListener();
        initTab();
    }

    public void editTextListener(){
        clearEditText = new ClearEditText(this);
    }
    public void initTab(){
        tabGroup = (SegmentedGroup) this.findViewById(R.id.tabHost);
        modelTab = (RadioButton)this.findViewById(R.id.modelTab);
        categroyTab=(RadioButton)this.findViewById(R.id.categoryTab);
        Log.e("active","true");


        pager = (ViewPager) this.findViewById(R.id.pager);
        // init view pager
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), 2);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        Log.e("modeltab", String.valueOf(position));
                        modelTab.setChecked(true);
                        categroyTab.setChecked(false);
                        //키보드 띄우기
                        inputMethodManager.showSoftInput(searchEditText, 0);

                        break;
                    case 1:
                        Log.e("categoryTab", String.valueOf(position));
                        categroyTab.setChecked(true);
                        modelTab.setChecked(false);
                        searchEditText.setText("");
                        //키보드 내리기
                        inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(),0);
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
                String result;
                switch (checkedId) {
                    case R.id.modelTab:
                        pager.setCurrentItem(0, false);
                        break;
                    case R.id.categoryTab:
                        pager.setCurrentItem(1, false);
                        break;
                    default:
                        Log.e("아무것도 클릭안됨","err");
                        return;

                }
            }
        });


    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @OnClick(R.id.backBtn)
    public void onClickBackBtn(){
        this.finish();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);

    }
    @OnClick(R.id.searchEditText)
    public void onTouchSearchEditText() {
        modelTab.setChecked(true);
        categroyTab.setChecked(false);

    }



    public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }


    @SuppressLint("ResourceType")
    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return new SearchModel();
            case 1:
                return new SearchCategory();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}



}
