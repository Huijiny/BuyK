package com.buyk.crocompany.buyk_android.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.MyRegisteredBike;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerView.MyRegisteredBikeRecyclerAdapter;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;

public class IsSellingCategory extends android.support.v4.app.Fragment {
    View view;
    private RecyclerView recyclerViewInIsSelling;
    private MyRegisteredBikeRecyclerAdapter adapter;
    Retrofit retrofit;
    RetrofitInterface restApiInterface;
    List<ResponseModelList> responseModelList;
    HashMap<String,String>headerMap;
    String token;
    String id;
    String uuid;
    Realm localUserDataRealm;
    LocalUserData localUserData;
    SoldOutCategory soldOutCategory;
    IsSellingCategory isSellingCategory;
    GetRegisteredList getRegisteredList;
    TextView noResultText;
    ImageView noResultImage;
    List<ResponseModelList> firstList;
    MyRegisteredBike myRegisteredBike;

    SkeletonScreen skeletonScreen;
    public IsSellingCategory (){

    }
    @SuppressLint("ValidFragment")
    public IsSellingCategory(MyRegisteredBikeRecyclerAdapter adapter)
    {
        this.adapter = adapter;
        Log.e("selling 어댑터 잘 장착",String.valueOf(this.adapter));
    }

    public static IsSellingCategory newInstance(int position,MyRegisteredBikeRecyclerAdapter adapter) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        IsSellingCategory fragment = new IsSellingCategory(adapter);
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // GetRegisteredList getRegisteredList = new GetRegisteredList();
       // getRegisteredList.execute();

    }
    @Override
    public void onResume() {

        super.onResume();
        GetRegisteredList getRegisteredList = new GetRegisteredList();
        getRegisteredList.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_is_selling, container, false);
        recyclerViewInIsSelling =view.findViewById(R.id.myRegisteredBikeItemInIsSelling);

        setRecyclerView();


        return view;
    }


    private void setRecyclerView(){

        recyclerViewInIsSelling.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewInIsSelling.setLayoutManager(manager);

        recyclerViewInIsSelling.setItemAnimator(new DefaultItemAnimator());
        skeletonScreen = Skeleton.bind(recyclerViewInIsSelling)
                .adapter(adapter)
                .count(3)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .load(R.layout.skeleton_recycle_my_registered_bike_item_on_selling)
                .show();
    }

    public void setRegisteredList(List<ResponseModelList> list){
        noResultImage = view.findViewById(R.id.noResultImageInSelling);
        noResultText = view.findViewById(R.id.noResultGuideTextInSelling);
        adapter.addData(list, 0);
            if(list.size()==0)
            {
                skeletonScreen.hide();

                noResultImage.setVisibility(View.VISIBLE);
                noResultText.setVisibility(View.VISIBLE);
                recyclerViewInIsSelling.setAdapter(adapter);

            }else {
               // Log.e("list 첫번째", list.get(0).getModel());
                skeletonScreen.hide();

                noResultText.setVisibility(View.GONE);
                noResultImage.setVisibility(View.GONE);
                recyclerViewInIsSelling.setAdapter(adapter);

            }
       // adapter.addData(list, 0);
    }

    class GetRegisteredList extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            responseModelList = new ArrayList<>();
            headerMap = new HashMap<>();
            checkUserData();
            headerMap = new HashMap<>();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id",id);
            headerMap.put("user-uuid",uuid);
            Log.e("localUserData",token+id);


            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<List<ResponseModelList>> GetRegisteredList = restApiInterface.getRegisteredList(headerMap);
            GetRegisteredList.enqueue(new Callback<List<ResponseModelList>>() {
                @Override
                public void onResponse(Call<List<ResponseModelList>> call, Response<List<ResponseModelList>> response) {
                    responseModelList=response.body();

                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));
                    if(!ObjectUtils.isEmpty(responseModelList)){
                        if(responseModelList.size()>0){
//                            IsSellingCategory isSellingCategory2 = (IsSellingCategory)getSupportFragmentManager().findFragmentById(R.id.fragmentIsSelling);
                            setRegisteredList(responseModelList.stream().filter(e -> e.getDeal_status().contentEquals("판매중")).collect(Collectors.toList()));
                            //soldOutCategory.setRegisteredList( responseModelList.stream().filter(e -> e.getDeal_status().contentEquals("중지됨")).collect(Collectors.toList()));
                        }
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
            super.onPostExecute(aVoid);

        }


    }
    public void checkUserData(){
        SharedPreferences userInfo = this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            id = userInfo.getString("user-id", null);

        }

    }
}
