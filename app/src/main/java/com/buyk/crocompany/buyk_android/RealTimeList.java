package com.buyk.crocompany.buyk_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerView.OfferingListAdapter;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class RealTimeList extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    OfferingListAdapter offeringListAdapter ;
    RecyclerView offeringRecyclerView;
    LinearLayoutManager offeringLayoutManager;
    RecyclerView listFilterItemRecyclerView;
    LinearLayoutManager filterItemLayoutManager;


    //realm

    int model;
    String modelName;
    String model__types__id;

    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    Realm localUserDataRealm;
    LocalUserData localUserData;

    HashMap<String,String>filterQueryMap;
    HashMap<String,String>headerMap;

    SkeletonScreen skeletonScreen;

    String token;
    String id;
    String uuid;

    @BindView(R.id.listFilterAlertText) TextView listFilterAlertText;
    @BindView(R.id.listSearchModelName) TextView listSearchModelName;
    @BindView(R.id.listFilterRecylerView) RecyclerView listFilterRecyclerView;
    @BindView(R.id.filterIcon)ImageButton filterIcon;
    @BindView(R.id.modelPlusBtn)ImageButton plusBtn;
    List<ResponseModelList> responseModelList;

    GetModelTask getModelTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offering_list);
        ButterKnife.bind(this);
        responseModelList = new ArrayList<>();
        RecyclerViewInit();
        settingOnLikedView();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "매물 목록", null /* class override */);
        Intent intent = getIntent();
         getModelTask = new GetModelTask();
         getModelTask.execute();

    }

    public void settingOnLikedView(){
        filterIcon.setVisibility(View.GONE);
        plusBtn.setVisibility(View.GONE);
        listSearchModelName.setText("실시간 바이크");
    }

    public void RecyclerViewInit(){
        offeringListAdapter = new OfferingListAdapter();
        offeringRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_layout);
        skeletonScreen = Skeleton.bind(offeringRecyclerView)
                .adapter(offeringListAdapter)
                .count(3)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .load(R.layout.skeleton_offering_list_item)
                .show();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        offeringRecyclerView.setLayoutManager(layoutManager);

        offeringRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                offeringRecyclerView.invalidateItemDecorations();
            }
        });




    }

//    public void addData(List<ResponseModelList> responseModelList, int i){

//        if(i>=10) {
//            offeringListAdapter.add("b");
//            offeringListAdapter.addData(responseModelList,1);
//            Log.e("b","b");
//
//        }
//       if(i<10) {
//            responseModelList.forEach((e) -> offeringListAdapter.add("a"));
//
//            Log.e("모델리스트뷰adddatamethod",responseModelList.get(0).getModel());
//            offeringListAdapter.addData(responseModelList,0);

//        }
//    }



    class GetModelTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            filterQueryMap = new HashMap<>();
            filterQueryMap.put("deal_status","O");
            checkUserData();
            headerMap = new HashMap<>();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id",id);
            headerMap.put("user-uuid",uuid);
            offeringListAdapter.addHeaderMap(headerMap);
            Log.e("localUserData",token+id);
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Integer... params) {
            List<String>model = new ArrayList<>();
            Call<List<ResponseModelList>> GetModelList = restApiInterface.getModelList(headerMap,model,filterQueryMap);
            GetModelList.enqueue(new Callback<List<ResponseModelList>>() {
                @Override
                public void onResponse(Call<List<ResponseModelList>> call, Response<List<ResponseModelList>> response) {
                    responseModelList=response.body();

                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));
                    if(!ObjectUtils.isEmpty(responseModelList)){
                        if(responseModelList.size()>0){
                            for (int i = 0; i<=10; i++) {
                                offeringListAdapter.add("a");
                            }
                            responseModelList.forEach((e) -> offeringListAdapter.add("a"));
                            offeringListAdapter.addData(responseModelList,0);
                        }
                    }
                    skeletonScreen.hide();
                    offeringRecyclerView.setAdapter(offeringListAdapter);


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
            super.onPostExecute(aVoid);


        }


    }

    @OnClick(R.id.backBtn)
    public void onClickBackBtn(){
        this.finish();
    }


    public void checkUserData(){

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            id = userInfo.getString("user-id", null);

        }

    }
}

