package com.buyk.crocompany.buyk_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.model.DummyData;
import com.buyk.crocompany.buyk_android.model.LocalData.FilterData;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerItemClickListener;
import com.buyk.crocompany.buyk_android.util.RecyclerView.FilterItemListAdapter;
import com.buyk.crocompany.buyk_android.util.RecyclerView.OfferingListAdapter;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

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

public class OfferingList extends BaseAcitivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    OfferingListAdapter offeringListAdapter ;
    RecyclerView offeringRecyclerView;
    LinearLayoutManager offeringLayoutManager;
    DummyData dummyData =new DummyData();
    String[] testUrl;
    RecyclerView listFilterItemRecyclerView;
    FilterItemListAdapter filterItemListAdapter;
    LinearLayoutManager filterItemLayoutManager;


    //realm
    FilterData filterData;
    Realm realmFilterData;

    int model;
    String modelName;
    String model__types__id;

    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    Realm localUserDataRealm;
    LocalUserData localUserData;

    HashMap<String,String>filterQueryMap;
    HashMap<String,String>headerMap;

    String token;
    String id;
    String uuid;

    @BindView(R.id.listFilterAlertText)
    TextView listFilterAlertText;

    @BindView(R.id.listSearchModelName)
    TextView listSearchModelName;

    @BindView(R.id.modelPlusBtn)
    ImageButton modelPlusBtn;

    @BindView(R.id.noResultView)
    LinearLayout noResultView;
    @BindView(R.id.recycler_view_layout)
    RecyclerView skeletonOfferingListView;

    SkeletonScreen skeletonScreen;

    List<ResponseModelList> responseModelList;

    GetModelTask getModelTask;

    ArrayList<String>model__in = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offering_list);
        ButterKnife.bind(this);
        responseModelList = new ArrayList<>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "매물 목록", null /* class override */);

        RecyclerViewInit();

         getModelTask = new GetModelTask();
         getModelTask.execute();
    }



    public void RecyclerViewInit(){
        offeringListAdapter = new OfferingListAdapter();
        offeringRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_layout);
        skeletonScreen = Skeleton.bind(offeringRecyclerView)
                .adapter(offeringListAdapter)
                .count(10)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .load(R.layout.skeleton_offering_list_item)
                .show();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        offeringRecyclerView.setLayoutManager(layoutManager);
        listFilterItemRecyclerView = (RecyclerView)findViewById(R.id.listFilterRecylerView);

        filterItemListAdapter = new FilterItemListAdapter();
        listFilterItemRecyclerView.setAdapter(filterItemListAdapter);
        filterItemLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        listFilterItemRecyclerView.setLayoutManager(filterItemLayoutManager);
        listFilterItemRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(listFilterItemRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent gotoFilter = new Intent(OfferingList.this, Filtter.class);
                        gotoFilter.putExtra("filterData",filterData);
                        gotoFilter.putExtra("modelName",modelName);
                        if(model__in.size()>0){
                            Log.e("model__in3",model__in.get(0));
                            gotoFilter.putStringArrayListExtra("model__in",model__in);
                            startActivity(gotoFilter);
                        }
                        if(!TextUtils.isEmpty(model__types__id)){
                            Log.e("model__types__id",model__types__id);
                            gotoFilter.putExtra("model__types__id",model__types__id);
                            startActivity(gotoFilter);
                        }
                        overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        initLocalData();
        testUrl=dummyData.getTestUrlList();

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(offeringListAdapter);
        offeringRecyclerView.addItemDecoration(headersDecor);

        offeringRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                offeringRecyclerView.invalidateItemDecorations();
                headersDecor.invalidateHeaders();
            }
        });



      // filterItemListAdapter.addData("바이크바이크",filterData);

    }
    public void initLocalData(){
        Intent intent = getIntent();
        filterData = new FilterData();
        if((FilterData)intent.getSerializableExtra("filterData")!=null) {
            filterData = (FilterData) intent.getSerializableExtra("filterData");
            if(ObjectUtils.isEmpty(filterData)){
                Log.e("필터데이터 없음","");
            }else {
                filterItemListAdapter.addData(true,filterData);
            }

        }else {
            filterItemListAdapter.addData(false,filterData);
        }
        if(!ObjectUtils.isEmpty(intent.getStringArrayListExtra("model__in"))){
            model__in.addAll(intent.getStringArrayListExtra("model__in"));
            Log.e("model__in1",model__in.get(0));
        }
        else {
            addModelIn((intent.getIntExtra("model",0)));
        }
        modelName = intent.getStringExtra("modelName");
        model__types__id = intent.getStringExtra("model__types__id");
        if(!TextUtils.isEmpty(model__types__id)) {
            modelPlusBtn.setVisibility(View.GONE);
        }
        else
        {
            modelPlusBtn.setVisibility(View.VISIBLE);
        }
        filterQueryMap = new HashMap<>();
        filterQueryMap.put("model__types__id",model__types__id);
        if(model__in.size()<2) {
            listSearchModelName.setText(modelName);
        }
        else{
            Log.e("modelsize",String.valueOf(model__in.stream().distinct().count()-1));
            model__in.forEach(e->Log.e("modelList",e));
            listSearchModelName.setText(modelName +" 외 "+String.valueOf(model__in.stream().distinct().count()-1)+"모델");
        }
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
          filterQueryMap = filterItemListAdapter.getQueryMap();
          filterQueryMap.put("deal_status","O");
          Log.e("filterQueryMap", String.valueOf(filterQueryMap));
          if(!TextUtils.isEmpty(model__types__id)){
              filterQueryMap.put("model__types__id", model__types__id);
              Log.e("model__types__id",model__types__id);
          }
            checkUserData();
            headerMap = new HashMap<>();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id",id);
            headerMap.put("user-uuid",uuid);
            offeringListAdapter.addHeaderMap(headerMap);
            Log.e("localUserData",token+id);
            Log.e("filterQueryMap", String.valueOf(filterQueryMap));
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<List<ResponseModelList>> GetModelList = restApiInterface.getModelList(headerMap,model__in,filterQueryMap);
            GetModelList.enqueue(new Callback<java.util.List<ResponseModelList>>() {
                @Override
                public void onResponse(Call<List<ResponseModelList>> call, Response<List<ResponseModelList>> response) {
                    responseModelList=response.body();

                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));
                    if(!ObjectUtils.isEmpty(responseModelList)){
                        if(responseModelList.size()>0){
                            noResultView.setVisibility(View.GONE);
                            offeringRecyclerView.setVisibility(View.VISIBLE);
                            responseModelList.forEach((e) -> offeringListAdapter.add("a"));
                            offeringListAdapter.addData(responseModelList,0);
                        }
                    }
                    else {
                        noResultView.setVisibility(View.VISIBLE);
                        offeringRecyclerView.setVisibility(View.GONE);
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
            Log.e("모델리스트로딩끝","ㅇㅇ");
            super.onPostExecute(aVoid);

        }


    }
    @OnClick(R.id.listFilterRecylerView)
    public void OnClicklistFilterRecylerView(){

    }

    @OnClick(R.id.filterIcon)
    public void onClickFilterIcon(){
        Intent gotoFilter = new Intent(OfferingList.this, Filtter.class);
        gotoFilter.putExtra("filterData",filterData);
        gotoFilter.putExtra("modelName",modelName);
        if(model__in.size()>0){
            Log.e("model__in3",model__in.get(0));
            gotoFilter.putStringArrayListExtra("model__in",model__in);
            startActivity(gotoFilter);
        }
        if(!TextUtils.isEmpty(model__types__id)){
            Log.e("model__types__id",model__types__id);
            gotoFilter.putExtra("model__types__id",model__types__id);
            startActivity(gotoFilter);
        }

        overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);

    }
    @OnClick(R.id.backBtn)
    public void onClickBackBtn(){
        this.finish();
    }

    @OnClick(R.id.modelPlusBtn)
    public void onClickModelPlusBtn(){
        Intent gotoPlusAndSearchModel = new Intent(OfferingList.this, IntegratedSerach.class);
        gotoPlusAndSearchModel.putExtra("whereFromIntent","offeringList");
        gotoPlusAndSearchModel.putStringArrayListExtra("model__in",model__in);
        startActivity(gotoPlusAndSearchModel);
        overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
    }

    public void addModelIn(int modelId){
        if(modelId>0){
            model__in.add(String.valueOf(modelId));
        }
    }
    public void checkUserData() {

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            id = userInfo.getString("user-id", null);

        }
    }
}

