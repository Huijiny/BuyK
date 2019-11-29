package com.buyk.crocompany.buyk_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.Event.LikeEvent;
import com.buyk.crocompany.buyk_android.Event.LoginEvent;
import com.buyk.crocompany.buyk_android.model.DummyData;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RealTimeBikeModel;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinPost;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.model.RequestMethod;
import com.buyk.crocompany.buyk_android.myPage.LikedList;
import com.buyk.crocompany.buyk_android.myPage.MyPageLogin;
import com.buyk.crocompany.buyk_android.myPage.myPage;
import com.buyk.crocompany.buyk_android.util.CustomToast;
import com.buyk.crocompany.buyk_android.util.DateCaculater;
import com.buyk.crocompany.buyk_android.util.LoginDialog;
import com.buyk.crocompany.buyk_android.util.NetworkChangeReceiver;
import com.buyk.crocompany.buyk_android.util.NoticeDialog;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerView.RealTimeBikeAdapter;
import com.buyk.crocompany.buyk_android.util.RecyclerView.ZeemBikeAdapter;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;


public class MainActivity extends BaseAcitivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    MyPageLogin.SessionCallback callback;

    RecyclerView realTimeBikeRecylerView;
    RecyclerView zeemBikeRecylerView;
    RealTimeBikeAdapter realTimeBikeAdapter;
    LinearLayoutManager realTimeLayoutManager;
    LinearLayoutManager zeemLayoutManager;
    ZeemBikeAdapter zeemBikeAdapter;
    //이미지 뷰 더미 이미지
    DummyData dummyData =new DummyData();
    String[] testUrl;
    int responseCode;


    JoinResponse joinResponse;
    JoinPost joinPost;

    String uuid;

    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    Realm localUserDataRealm;
    LocalUserData localUserData;


    DateCaculater dateCaculater;
    long currentDeadTime;

    Context context;

    NoticeDialog noticeDialog;
    boolean isLocalData;

    GetModelTask getModelTask;
    GetLikedItemList getLikedItemList;

    RequestMethod requestMethod;
    HashMap<String,String> headerMap;
    List<ResponseModelList> itemResponseList;
    HashMap<String,String> filterMap;
    List<ResponseModelList>realTimeItemList;

    boolean isAsyncRunning = false;

    @BindView(R.id.moreLikedList)
    TextView moreLikedListText;

    @BindView(R.id.moreRealTimeList)
    TextView moreRealTimeListText;

    @BindView(R.id.textView5)
    TextView likedBikeTitleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        restApiInit();

        //eventbus,butterKnife 등록
        ButterKnife.bind(this);

        noticeDialog = new NoticeDialog(this);
        dateCaculater = new DateCaculater();
        checkUserData();




        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "메인", null /* class override */);
        getAppKeyHash();

    }
    @Override
    public void onResume() {
        super.onResume();

        if(isLocalData == true){

        }
    }
    @Override
    public void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LikeEvent event) {
        Log.e("car", String.valueOf(event.like));
        if(event.like){
            checkUserData();
        }
    }


    public void initRecylerView() {
        realTimeBikeRecylerView = (RecyclerView)findViewById(R.id.realTimeBikeRecylerview);

        realTimeBikeAdapter = new RealTimeBikeAdapter();
        realTimeBikeRecylerView.setAdapter(realTimeBikeAdapter);
        realTimeLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        realTimeBikeRecylerView.setLayoutManager(realTimeLayoutManager);
        zeemBikeRecylerView = (RecyclerView)findViewById(R.id.zeemBikeRecylerView);
        zeemBikeAdapter = new ZeemBikeAdapter();
        zeemLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        zeemBikeRecylerView.setLayoutManager(zeemLayoutManager);
        zeemBikeRecylerView.setAdapter(zeemBikeAdapter);

        requestMethod = new RequestMethod();
            getModelTask = new GetModelTask();
            getLikedItemList = new GetLikedItemList();

            getModelTask.execute();
            getLikedItemList.execute();

    }
    public void addData(String text,String testUrl,int index){
        RealTimeBikeModel realTimeBikeModel =new RealTimeBikeModel();

        realTimeBikeModel.setPrice(text);
        realTimeBikeModel.setImageUrl(testUrl);
        Log.e("모델리스트에 데이터 추가","확인");


//        zeemBikeAdapter.addData(realTimeBikeModel,index);
    }



    public void gotoIntegrateSearch(View view) {
        Intent gotoIntegrateSearchButton = new Intent(MainActivity.this, IntegratedSerach.class);

        startActivity(gotoIntegrateSearchButton);
        overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);

    }
    public void gotoSoldBike(View view){
        Intent gotoSoldBikeButton = new Intent(MainActivity.this, soldBikeView.class);

        if(Session.getCurrentSession().checkAndImplicitOpen()){
            Log.e("카카오세션어플리케이션에서 체크","열려있음");
            startActivity(gotoSoldBikeButton);
        }else {
            Log.e("dasddas","dasdsa");
            Intent gotoLoginDialogButton = new Intent(MainActivity.this, LoginDialog.class);
            EventBus.getDefault().post(new LoginEvent("main"));
            startActivity(gotoLoginDialogButton);

        }
    }
    @OnClick(R.id.gotoSoldBike)
    public void goToSoldBike(){
        Intent gotoSoldBikeButton = new Intent(MainActivity.this, soldBikeView.class);

        if(Session.getCurrentSession().checkAndImplicitOpen()){
            Log.e("카카오세션어플리케이션에서 체크","열려있음");
            startActivity(gotoSoldBikeButton);
        }else {
            Log.e("씨발","dasdsa");
            Intent gotoLoginDialogButton = new Intent(MainActivity.this, LoginDialog.class);
            EventBus.getDefault().post(new LoginEvent("main"));
            startActivity(gotoLoginDialogButton);

        }
    }

    @OnClick(R.id.myPageButton)
    public void goToMyPage(View v){
        Intent goToMyPage = new Intent(MainActivity.this,myPage.class);
        Log.e("go","마이페이지");
        startActivity(goToMyPage);
    }
    @OnClick(R.id.moreLikedList)
    public void goToMoreLikedList(){
        Intent goToMoreLikedList = new Intent(MainActivity.this,LikedList.class);
        Log.e("go","찜한바이크");
        startActivity(goToMoreLikedList);
    }
    @OnClick(R.id.moreRealTimeList)
    public void goToMoreRealTimeList(){
        Intent goToMoreRealList = new Intent(MainActivity.this,RealTimeList.class);
        Log.e("go","실시간바이크");
        startActivity(goToMoreRealList);
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String GetDevicesUUID(Context mContext){
        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }
    public void restApiInit(){

        localUserData = new LocalUserData();
        joinResponse = new JoinResponse();
        joinPost = new JoinPost();
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApiInterface = retrofit.create(RetrofitInterface.class);
    }

    class JoinTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("여기계속 틀어짐", "JoinTask");

//            removeAllPreferences();
            joinPost.setUuid(uuid);

        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<JoinResponse> postJoin = restApiInterface.postJoin(joinPost);
            postJoin.enqueue(new Callback<JoinResponse>() {
                @Override
                public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                    joinResponse=response.body();
                    Log.e("joinResponse", String.valueOf(response.code()));

                        localUserDataInput(joinResponse);

                        finish();

                        startActivity(getIntent());

                        overridePendingTransition(0, 0);
//                    Log.e("joinResponse", String.valueOf(joinResponse.getId()));
//                    Log.e("joinResponse", String.valueOf(joinResponse.getToken()));
                }
                @Override
                public void onFailure(Call<JoinResponse> call, Throwable t) {
                    Log.d("connectfail", t.toString());
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
    class GetLikedItemList extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            itemResponseList = new ArrayList<>();
            Log.e("headerMap-",headerMap.get("access-token"));
        }
        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("headerMap--",headerMap.get("user-uuid"));
            Call<List<ResponseModelList>> getLikedItemList = restApiInterface.getLikeItemList(headerMap);
            getLikedItemList.enqueue(new Callback<List<ResponseModelList>>() {
                @Override
                public void onResponse(Call<List<ResponseModelList>> call, Response<List<ResponseModelList>> response) {
                    itemResponseList = response.body();
//                    Log.e("itemResponseList",itemResponseList.get(0).getDeal_area());
                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));
                    if(!ObjectUtils.isEmpty(itemResponseList)) {
                        Log.e("itemResponseList",String.valueOf(itemResponseList.size()));
                        if(itemResponseList.size()>0){
                            zeemBikeAdapter.addData(itemResponseList,0);
                            Log.e("responsezeembike","dd");
                            if(itemResponseList.size() > 4){
                                moreLikedListText.setVisibility(View.VISIBLE);
                            }
                            else {
                                moreLikedListText.setVisibility(View.GONE);
                            }
                            likedBikeTitleText.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        likedBikeTitleText.setVisibility(View.GONE);
                        moreLikedListText.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(Call<List<ResponseModelList>> call, Throwable t) {
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
            Log.e("isAsyncRunning",String.valueOf(isAsyncRunning));
            super.onPostExecute(aVoid);
        }


    }
    class GetModelTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            filterMap = new HashMap<>();
            filterMap.put("deal_status","O");
            realTimeItemList = new ArrayList<>();

        }
        @Override
        protected Integer doInBackground(Integer... params) {
            List<String>model = new ArrayList<>();
            Call<List<ResponseModelList>> GetModelList = restApiInterface.getModelList(headerMap,model,filterMap);
            GetModelList.enqueue(new Callback<java.util.List<ResponseModelList>>() {
                @Override
                public void onResponse(Call<List<ResponseModelList>> call, Response<List<ResponseModelList>> response) {

                    realTimeItemList=response.body();

                    Log.e("SERVERCODE", String.valueOf(responseCode+response.message()));
                    if(!ObjectUtils.isEmpty(realTimeItemList)){
                        if(realTimeItemList.size()>0){
                            realTimeBikeAdapter.addData(realTimeItemList,0);
                            realTimeBikeAdapter.notifyDataSetChanged();
                        }
                    }
                    if(response.code()==403){
                        Log.e("재가입","그렇다고");

                        CustomToast customToast = new CustomToast(getBaseContext());
                        customToast.showToast("다른 기기에서 로그인되어,\n비인증회원으로 로그인됩니다.\n잠시후에 다시 시도 해주세요.",Toast.LENGTH_SHORT);
                        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                removeAllPreferences();
                                getADIdThread.execute();
                            }
                        });

                    }


                }
                @Override
                public void onFailure(Call<List<ResponseModelList>> call, Throwable t) {
                    Log.e("connectfail", t.toString());
                }

            });
            return responseCode;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            super.onPostExecute(responseCode);
            Log.e("responseCode",String.valueOf(responseCode));


        }


    }
    public void localUserDataInput(JoinResponse joinResponse){

//        Log.e("localUserDataInput",joinResponse.getToken());
        Log.e("isAsyncRunning",String.valueOf(isAsyncRunning));

            SharedPreferences userInfo =  getApplicationContext().getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();
            editor.clear();

            try{
                editor.putString("access-token", joinResponse.getToken());
                editor.putString("user-uuid", uuid);
                editor.putString("user-id", String.valueOf(joinResponse.getId()));
                editor.putString("access-token", joinResponse.getToken());
                editor.putString("deadline", joinResponse.getDeadline());
                editor.apply();
                editor.commit();
                String access_token = userInfo.getString("access-token", null);
                Log.e("쉐어드 들가긴 하는거임?", access_token);
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    public void checkUserData(){
        headerMap = new HashMap<>();
            SharedPreferences userInfo =  getApplicationContext().getSharedPreferences("userInfo", MODE_PRIVATE);
            String access_token = userInfo.getString("access-token",null);
            if(access_token != null){
                headerMap.put("access-token",userInfo.getString("access-token",null));
                headerMap.put("user-uuid",userInfo.getString("user-uuid",null));
                headerMap.put("user-id",userInfo.getString("user-id",null));
                headerMap.put("buyk-api-key",buyk_api_key);
                Log.e("access-token",headerMap.get("access-token"));
                Log.e("user-uuid",headerMap.get("user-uuid"));
                Log.e("user-id",headerMap.get("user-id"));
                currentDeadTime = dateCaculater.Caculater(userInfo.getString("deadline",null));
                Log.e("유저 체크 시작","성공");
                initRecylerView();
            }
            else {
                Log.e("유저 체크 시작","실패");
                getADIdThread.execute(); //가입

            }



}
    public void getAppKeyHash() {


        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


    AsyncTask<Void, Void, String> getADIdThread = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String advertId = null;
            try{
                advertId = idInfo.getId();
                Log.e(advertId,advertId);
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.e("ad id","ㅇ");

            return advertId;
        }
        @Override
        protected void onPostExecute(String advertId) {
            Log.e("ad id",advertId);

            uuid = advertId;
                JoinTask joinTask = new JoinTask();

            joinTask.execute();
        }
    };

    private void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("kakaoInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfo.edit();
        editor.clear();
        userInfoEditor.clear();
        editor.commit();
        userInfoEditor.commit();
    }
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

        finish();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
