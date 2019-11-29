package com.buyk.crocompany.buyk_android.myPage;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerView.OfferingListAdapter;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
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

public class LikedList extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    RecyclerView offeringRecyclerView;
    //realm
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    Realm localUserDataRealm;
    LocalUserData localUserData;

    HashMap<String,String>headerMap;

    String token;
    String id;
    String uuid;

    List<ResponseModelList> responseLikedList;


    GetLikedItemList getLikedItemList;
    OfferingListAdapter zeemBikeAdapter;

    SkeletonScreen skeletonScreen;


    @BindView(R.id.listFilterAlertText) TextView listFilterAlertText;
    @BindView(R.id.listSearchModelName) TextView listSearchModelName;
    @BindView(R.id.listFilterRecylerView) RecyclerView listFilterRecyclerView;
    @BindView(R.id.filterIcon)ImageButton filterIcon;
    @BindView(R.id.modelPlusBtn)ImageButton plusBtn;
    @BindView(R.id.noResultView)LinearLayout noResultView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offering_list);
        ButterKnife.bind(this);
        responseLikedList = new ArrayList<>();
        RecyclerViewInit();
        settingOnLikedView();
        getLikedItemList = new GetLikedItemList();
        getLikedItemList.execute();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "찜 목록", null /* class override */);

    }



    public void RecyclerViewInit(){

        zeemBikeAdapter = new OfferingListAdapter();
        offeringRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_layout);

        skeletonScreen = Skeleton.bind(offeringRecyclerView)
                .adapter(zeemBikeAdapter)
                .count(3)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .load(R.layout.skeleton_offering_list_item)
                .show();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        offeringRecyclerView.setLayoutManager(layoutManager);

    }
    public void settingOnLikedView(){
        filterIcon.setVisibility(View.GONE);
        plusBtn.setVisibility(View.GONE);
        listSearchModelName.setText("찜한 바이크");
    }


    class GetLikedItemList extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            checkUserData();

            responseLikedList = new ArrayList<>();
            headerMap = new HashMap<>();
            headerMap.put("access-token",token);
            headerMap.put("user-uuid",uuid);
            headerMap.put("user-id",id);
            headerMap.put("buyk-api-key",buyk_api_key);
            zeemBikeAdapter.addHeaderMap(headerMap);
            Log.e("headerMap-",headerMap.get("access-token"));
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("headerMap--",headerMap.get("user-uuid"));
            Call<List<ResponseModelList>> getLikedItemList = restApiInterface.getLikeItemList(headerMap);
            getLikedItemList.enqueue(new Callback<List<ResponseModelList>>() {
                @Override
                public void onResponse(Call<List<ResponseModelList>> call, Response<List<ResponseModelList>> response) {
                    responseLikedList = response.body();
//                    Log.e("itemResponseList",itemResponseList.get(0).getDeal_area());
                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));
                    if(!ObjectUtils.isEmpty(responseLikedList)) {
                        if(responseLikedList.size()>0){
                            offeringRecyclerView.setVisibility(View.VISIBLE);
                            noResultView.setVisibility(View.GONE);
                            Collections.reverse(responseLikedList);
                            zeemBikeAdapter.addData(responseLikedList,0);
                        }
                        else {
                            offeringRecyclerView.setVisibility(View.GONE);
                            noResultView.setVisibility(View.VISIBLE);
                        }
                    }
                    skeletonScreen.hide();
                    offeringRecyclerView.setAdapter(zeemBikeAdapter);

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
