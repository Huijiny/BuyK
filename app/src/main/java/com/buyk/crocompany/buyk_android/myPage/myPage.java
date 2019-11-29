package com.buyk.crocompany.buyk_android.myPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.util.UploadView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class myPage extends AppCompatActivity implements UploadView{
    private String[] texts = {"로그인","내가 찜한 바이크","문의하기"};
    private String[] afterLogin = {"내가 찜한 바이크","내가 올린 바이크","문의하기"};
    private static final int LAYOUT = R.layout.recycle_list_my_page;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String kakaoNickName;
    private String kakaoEmail;
    private String kakaoProfile;
    Intent intent;
    private RelativeLayout whenLoigin;
    public boolean isLogin=false;
    TextView nameTextView;
    TextView emailTextView;
    ImageView profileImgView;
    Picasso picasso;
    ArrayList<listName> mlistNames=new ArrayList<>(0);
    private FirebaseAnalytics mFirebaseAnalytics;

    //kakao SharedPreference Info
    String email;
    String profileImage;
    String nickName;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        view = getLayoutInflater().from(this).inflate(R.layout.recycle_list_my_page,null);
        setContentView(view);

        initView();
        setData();
        getProfileData();

        setRecyclerView();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "마이페이지", null /* class override */);
    }
    @Override
    public void onRestart() {

        super.onRestart();
        Log.e("마이페이지 ","restart");

        view.postInvalidate();
        view.requestLayout();
    }
    @OnClick(R.id.backToMain)
    public void goBackToMain(View view){
        this.finish();
    }

    private void initView()
    {

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        profileImgView = findViewById(R.id.profileImage);
        whenLoigin = findViewById(R.id.whenLogin);
    }

    private void setRecyclerView(){

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter=new recycleViewAdapter(myPage.this,mlistNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick(R.id.myInfo)
    public void goToInfomationView(View v){
        Intent goToMyInformation = new Intent(myPage.this, myInformation.class);
        goToMyInformation.putExtra("kakaoNickName",nickName);
        goToMyInformation.putExtra("kakaoEmail",email);
        startActivityForResult(goToMyInformation,1111);

    }

    void setData(){
        mlistNames.clear();
        for(String name:texts){
            mlistNames.add(new listName(name));
        }

        if(isLogin){
            mlistNames.clear();
            for(String name:afterLogin){
                mlistNames.add(new listName(name));
            }
        }

    }

    public void getProfileData() {

        intent = getIntent();

        try {
            getPreferences();
            //여기 이정윤 코드 sharedpreference에서 받아온거 (앞으로 한번 로그인하면 계속 정보 남아있게함)
            Transformation transformation = new CropCircleTransformation();
            Log.e("email",email);
            Log.e("nickname",nickName);
            if(!nickName.equals("nickName-null")){
                nameTextView.setText(nickName);
                emailTextView.setText(email);

                Picasso.with(this).load(profileImage).transform(transformation).fit().into(profileImgView);
                isLogin=true;
                whenLoigin.setVisibility(View.VISIBLE);
            }else{
                isLogin=false;
                whenLoigin.setVisibility(View.GONE);
            }

            setData();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("에러뜸","카카오에서 get");
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode",String.valueOf(requestCode));
        Log.e("Result code", String.valueOf(resultCode));
        Log.e("data",String.valueOf(data));
        if(requestCode==1111) {
            if (resultCode == 1) {
                Log.e("isLogOut?", data.getStringExtra("Logout"));
                isLogin = false;
                whenLoigin.setVisibility(View.GONE);
                setData();
                setRecyclerView();
                view.invalidate();
              //  view.postInvalidate();
            }
        }
    }

    // 값 불러오기
    private void getPreferences(){
        SharedPreferences getKakaoInfo = getSharedPreferences("kakaoInfo", MODE_PRIVATE);
       email= getKakaoInfo.getString("email", "email-null");
       nickName = getKakaoInfo.getString("nickName", "nickName-null");
       profileImage = getKakaoInfo.getString("profileImage","profileimage-null");
    }


    @Override
    public void onClick() {
        isLogin=false;
    }
}
