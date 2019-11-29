package com.buyk.crocompany.buyk_android;


import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.Event.CarClassificationEvent;
import com.buyk.crocompany.buyk_android.Event.EnableSoldViewNextButton;
import com.buyk.crocompany.buyk_android.Event.LoginEvent;
import com.buyk.crocompany.buyk_android.Event.SetAnimationEvent;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.ItemPost;
import com.buyk.crocompany.buyk_android.model.RemoteData.ItemResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.PaymentResponseMethod;
import com.buyk.crocompany.buyk_android.model.RemoteData.PhoneNumberPatch;
import com.buyk.crocompany.buyk_android.model.RemoteData.PhoneNumberResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.myPage.myPage;
import com.buyk.crocompany.buyk_android.util.CustomToast;
import com.buyk.crocompany.buyk_android.util.ImageUpload;
import com.buyk.crocompany.buyk_android.util.NoticeDialogTwoButtons;
import com.buyk.crocompany.buyk_android.util.ResizeAndWaterMark;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.miracleshed.hpb.HorizontalProgressBar;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;


public class soldBikeView extends AppCompatActivity  {
    private FirebaseAnalytics mFirebaseAnalytics;

    private final int[] percent ={10,60,360,420,560,620,740,940};
    private final int[] progressPercent = {11,22,33,44,61,72,83,100};
    public  static boolean firstAccess=true;
    private HorizontalProgressBar progressBar;
    public ViewPager pager;
    private SwipeViewPager swipeViewPager;
    @BindView(R.id.backBtnInSoldView)
    ImageButton backBtn;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.cons1)
    LinearLayout lay1;
    @BindView(R.id.pager)
    android.support.v4.view.ViewPager lay2;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.progressPercent1)
    EvaporateTextView progressPercentText;

    final int pbBackGroundColor = Color.rgb(15,15,15);
    final int pbBarColor = Color.rgb(247,94,0);
    public int mutex=0;
    int value =0;
    int add=1;
    Boolean firstRegist=true;
    CustomAdapter pagerAdapter;
    public ItemPost itemPost;
    PhoneNumberPatch phoneNumberPatch;
    PhoneNumberResponse phoneNumberResponse;
    PhoneNumberTask phoneNumberTask;
    Realm localUserDataRealm;
    String uuid;
    String token;
    String userId;
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    HashMap<String,String>headerMap;
    ItemResponse itemResponse;
    LocalUserData localUserData;
    ItemTask itemTask;
    InputMethodManager imm;
    SoftKeyboard softKeyboard;
    SoftKeyboardDectectorView softKeyboardDectectorView;
    NoticeDialogTwoButtons nd;
    ArrayList<File> resizedSmallImage = new ArrayList<>();
    ArrayList<File> resizedLargeImage = new ArrayList<>();
    InputMethodManager mgr;
    View view;
    Context context;
    PaymentResponseMethod paymentResponseMethod;
    Intent intent;

    CustomToast customToast;

    Boolean isUploadStart = false;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().from(this).inflate(R.layout.sold_bike_view,null);
        setContentView(view);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        customToast = new CustomToast(getBaseContext());
        initView();
    }

    void initView()
    {
        headerMap = new HashMap<>();
        phoneNumberPatch = new PhoneNumberPatch();
        itemPost = new ItemPost();
        imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);

        nextBtn.setOnClickListener(this::onNextButtonClicked);
        backBtn.setOnClickListener(this::onBackButtonClicked);

        progressBar=(HorizontalProgressBar) findViewById(R.id.progressBar3);
        progressBar.setMaxProgress(1000);
        progressBar.setBackgroundColor(pbBackGroundColor);
        progressBar.setProgressColor(pbBarColor);

        pager = (ViewPager)this.findViewById(R.id.pager);
        pagerAdapter = new CustomAdapter(getSupportFragmentManager(),getApplicationContext());
        pager.setAdapter(pagerAdapter);

        context = getApplicationContext();

        nextBtn.setVisibility(View.VISIBLE);
        setDisabled();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(percent[position]);
                progressPercentText.animateText(String.valueOf(progressPercent[position]) + "%");
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "매물 등록", null /* class override */);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(CarClassificationEvent event) {
        Log.e("car",event.classificate);
        if(event.classificate.equals("new")){
            pagerAdapter.removeCarView(pager.getCurrentItem());
            refresh();
            pagerAdapter.addNewCarView(pager.getCurrentItem());
            refresh();
        }
        else {
            pagerAdapter.removeCarView(pager.getCurrentItem());
            refresh();
            pagerAdapter.addUsedCarView(pager.getCurrentItem());
            refresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEnableNextButton(EnableSoldViewNextButton event) {
        if(event.isEnable) {
            setEnabled();
        }
        else {
            setDisabled();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setAnimationEvent(SetAnimationEvent event) {
        Log.e("aniStart","dd");
        if(event.animationStart) {
            nextBtn.setVisibility(View.GONE);
            startAnim();
            Log.e("aniStart","start");

        }
        else {
            stopAnim();
            Log.e("aniStart","end");

        }
    }

    @Override
    public void onBackPressed(){

        if(isUploadStart) {

        } else {
            onBackButtonClicked(view);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onClickedLayout(View v){
        imm.hideSoftInputFromWindow(lay1.getWindowToken(),0);
    }
    public void onClickedLayout1(View v){
        imm.hideSoftInputFromWindow(lay2.getWindowToken(),0);
    }

    private void askStopBikeSold()
    {
        nd = new NoticeDialogTwoButtons(this,
                "바이크 내놓기를 그만하시겠습니까?\n"+
        "입력 내용은 저장되지 않습니다.",
                leftListener,
                rightListener);
        nd.show();
        intent = new Intent(this, MainActivity.class);

    }

    public void refresh()
    {
        pagerAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
           nd.dismiss();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            EventBus.getDefault().post(new LoginEvent("soldView"));
            finish();
        }
    };

    public void resizeAndWaterMarking() {

        BikeImages bikeImages = new BikeImages();
        Bitmap decodeBitmap = null;

        ArrayList<String> selected = new ArrayList<>();

        selected = bikeImages.adapter.getSelectedImages();

        try{
            if (selected != null) {
                ResizeAndWaterMark resizeAndWaterMark = new ResizeAndWaterMark(selected, getApplicationContext());
                for (int i = 0; i < selected.size(); i++) {
                    Log.e("selected안에 들어가있는 갯수", String.valueOf(selected.size()));
                    Log.e("selected 안에 들어가있는 것", String.valueOf(selected.get(i)));
                    Uri tmp = getUriFromPath(selected.get(i));
                    try {
                        decodeBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), tmp);
                        Log.e("width", String.valueOf(decodeBitmap.getWidth()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap result = resizeAndWaterMark.addWatermark(decodeBitmap);

                }

                resizedLargeImage = resizeAndWaterMark.getLarge_images();
                resizedSmallImage = resizeAndWaterMark.getSmall_images();

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = this.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        Uri uri = null;
        cursor.moveToNext();
        try {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            return uri;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    public void onNextButtonClicked(View v) {
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Log.i("position", String.valueOf(pager.getCurrentItem()));
        int cur;
        int pageSize;

        pagerAdapter.notifyDataSetChanged();
        cur = pager.getCurrentItem();
        pageSize = pagerAdapter.getCount();

        Log.e("pageSize",String.valueOf(pageSize));
        Log.e("현재뷰",String.valueOf(cur));

                if (cur < pageSize) {
                    pager.invalidate();
                    pager.setCurrentItem(cur+1 , true);
            }
                if(pageSize>5) {
                    if (cur+2 == pageSize) {
                        nextBtn.setText("완료");
                    }else if(cur+1==pageSize){
                        setItemTask();
                    }

                }

            mgr.hideSoftInputFromWindow(v.getWindowToken(),0);


    }

    public void startAnim()
    {
        avi.bringToFront();
        avi.setEnabled(false);
        nextBtn.setVisibility(View.GONE);
        nextBtn.setText("");
        avi.smoothToShow();
//        setViewInvalidate(avi,nextBtn);


    }

    public void stopAnim()
    {
        nextBtn.bringToFront();
        nextBtn.setText("다음");
        avi.smoothToHide();
        nextBtn.setVisibility(View.VISIBLE);

//        setViewInvalidate(avi,nextBtn);


    }

    private void setViewInvalidate(View... views) {

        for (View v : views) {
            v.invalidate();
        }

    }

    public void setDisabled()
    {
        nextBtn.setEnabled(false);
        nextBtn.bringToFront();
        nextBtn.setVisibility(View.VISIBLE);
    }

    public void setEnabled()
    {
        nextBtn.setEnabled(true);
        nextBtn.bringToFront();
        nextBtn.setVisibility(View.VISIBLE);

    }
    public void setItemTask(){

        isUploadStart = true;
       itemTask = new ItemTask();
       itemTask.execute();

        if(phoneNumberPatch.getPhone()!=null){
            phoneNumberTask = new PhoneNumberTask();
            phoneNumberTask.execute();
        }
    }


    public void onBackButtonClicked(View v){
        int pageSize;
        int cur;
        int zeroCount=0;

        pageSize = pagerAdapter.fragment.size();
        cur = pager.getCurrentItem();

        if (cur > 0) {
            pager.setCurrentItem(cur - 1, true);
        }else if (cur == 0) {
            zeroCount++;
            if (zeroCount > 0) askStopBikeSold();
        }

        if(cur<=pageSize-1)nextBtn.setText("다음");
    }

    public void setModelId(int model_id){
        itemPost.setModel_id(model_id);
    }

    public void checkUserData(){

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            userId = userInfo.getString("user-id", null);

        }
    }

    private void afterRegist()
    {
        finish();
        Intent myPage = new Intent(context,myPage.class);
        startActivity(myPage);
        Intent myRegistModel = new Intent(getApplicationContext(),MyRegisteredBike.class);
        startActivity(myRegistModel);

        Intent detail = new Intent(getApplicationContext(),OfferingDetail.class);
        itemResponse.setLike(false);

        ResponseModelList responseModelList = new ResponseModelList();
        responseModelList.setCrawled_url(itemResponse.getCrawled_url());
        responseModelList.setCreated_at(itemResponse.getCreated_at());
        responseModelList.setDeal_area(itemResponse.getDeal_area());
        responseModelList.setDeal_status(itemResponse.getDeal_status());
        responseModelList.setDetail_information(itemResponse.getDetail_information());
        responseModelList.setDocument_status(itemResponse.getDocument_status());
        responseModelList.setDriven_distance(itemResponse.getDriven_distance());
        responseModelList.setId(itemResponse.getId());
        responseModelList.setImages(itemResponse.getImages());
        responseModelList.setLiked(itemResponse.getLike());
        responseModelList.setModel(itemResponse.getModel());
        responseModelList.setModel_year(itemResponse.getModel_year());
        responseModelList.setPayment_method(itemResponse.getPayment_method());
        responseModelList.setPrice(itemResponse.getPrice());
        responseModelList.setRepair_history(itemResponse.getRepair_history());
        responseModelList.setTuning_history(itemResponse.getTuning_history());
        responseModelList.setUpdated_at(itemResponse.getUpdated_at());
        responseModelList.setUser(itemResponse.getUser());
        detail.putExtra("detailData", responseModelList);
        startActivity(detail);

    }


    class ItemTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setDisabled();


            startAnim();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            checkUserData();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id", userId);
            headerMap.put("user-uuid",uuid);
            itemResponse = new ItemResponse();

        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<ItemResponse> postResgistItem = restApiInterface.postRegisterItem(headerMap,itemPost);
            postResgistItem.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    try {
                        itemResponse=response.body();
                        customToast.showToast("등록 완료.",Toast.LENGTH_SHORT);

                    } catch (Exception e) {
                        e.printStackTrace();
                        customToast.showToast("등록 실패.",Toast.LENGTH_SHORT);

                    }
                    if(itemResponse==null)Log.e("Response<ItemResponse>","null");
//                    Log.e("SERVERCODE", String.valueOf(response.code()));
//                    Log.e("itemRegist", String.valueOf(itemResponse.getId()+" "+response.message()));

                    try {
                        resizeAndWaterMarking();
                        ImageUpload imageUpload = new ImageUpload(getApplicationContext(),resizedLargeImage,resizedSmallImage);
                        imageUpload.putMultiPartFormData(itemResponse.getId());
                        customToast.showToast("이미지 등록 완료.",Toast.LENGTH_SHORT);

                        stopAnim();
                        afterRegist();
                    } catch (Exception e) {
                        e.printStackTrace();
                        customToast.showToast("이미지 등록 실패.",Toast.LENGTH_SHORT);
                    }


                }
                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.e("connectfail", t.toString());
                }

            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }



    }
    class PhoneNumberTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            checkUserData();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id", userId);
            headerMap.put("user-uuid",uuid);

            phoneNumberResponse = new PhoneNumberResponse();
        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<PhoneNumberResponse> patchResgistphone = restApiInterface.patchUpdatePhoneNumber(headerMap,phoneNumberPatch);
            patchResgistphone.enqueue(new Callback<PhoneNumberResponse>() {
                @Override
                public void onResponse(Call<PhoneNumberResponse> call, Response<PhoneNumberResponse> response) {
                    try {
                        phoneNumberResponse = response.body();
                        if(phoneNumberResponse==null)Log.e("Response<PhoneNumberResponse>","null");


                        //api성공시 로컬에 저장
                        String phoneNumber = phoneNumberPatch.getPhone();
                        SharedPreferences phoneInformation = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = phoneInformation.edit();
                        editor.putString("휴대폰번호", phoneNumber);
                        editor.commit();

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<PhoneNumberResponse> call, Throwable t) {
                    Log.e("connectfail", t.toString());
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }



    }
}



