package com.buyk.crocompany.buyk_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.Fragment.IsSellingCategory;
import com.buyk.crocompany.buyk_android.Fragment.SoldOutCategory;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerView.MyRegisteredBikeRecyclerAdapter;
import com.buyk.crocompany.buyk_android.util.UploadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;

public class MyRegisteredBike extends AppCompatActivity implements UploadView {


    private ViewPager viewPager;
    SegmentedGroup tabGroup;
    RadioButton isSellingTab;
    RadioButton soldOutTab;
    MyRegisteredBikeRecyclerAdapter myRegisteredBikeRecyclerAdapterSold;
    MyRegisteredBikeRecyclerAdapter myRegisteredBikeRecyclerAdapterSelling;

    Retrofit retrofit;
    RetrofitInterface restApiInterface;
    List<ResponseModelList> responseModelList;
    HashMap<String,String>headerMap;
    String token;
    String id;
    String uuid;
    Realm localUserDataRealm;
    TabPagerAdapter pagerAdapter;
    Boolean isEmptyInSelling = false;
    Boolean isEmptyInSold = false;
    TextView noResultTextInSoldView;
    ImageView noResultImageInSoldView;
    RecyclerView recyclerViewInSoldView;
    LayoutInflater sold;
    View soldOutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_registered_bike);
        GetRegisteredList getRegisteredList = new GetRegisteredList();
        getRegisteredList.execute();
        myRegisteredBikeRecyclerAdapterSold = new MyRegisteredBikeRecyclerAdapter(getApplicationContext(),1,this);
        myRegisteredBikeRecyclerAdapterSelling = new MyRegisteredBikeRecyclerAdapter(getApplicationContext(),0,this);

        initView();
        initTab();

       // setRecyclerView();
    }
    @Override
        protected void onResume() {
        super.onResume();

        Log.d("TestAppActivity", "onResume");
    }

    @OnClick(R.id.backToMyPageFromMyRegisteredBike)
    public void goToMyPage(View view){
        this.finish();
    }

    private void initView()
    {
        viewPager = (ViewPager)findViewById(R.id.pagerInRegisteredBikeView);
    }

    private void initTab()
    {
     tabGroup = (SegmentedGroup)findViewById(R.id.tabHostInMyRegisteredBike);
     isSellingTab=(RadioButton)findViewById(R.id.isSelling);
     soldOutTab=(RadioButton)findViewById(R.id.SoldOut);
     sold = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     soldOutView = sold.inflate(R.layout.fragment_sold_out,null);
     noResultTextInSoldView = soldOutView.findViewById(R.id.noResultGuideText);
     noResultImageInSoldView = soldOutView.findViewById(R.id.noResultImage);
     recyclerViewInSoldView = soldOutView.findViewById(R.id.myRegisteredBikeItemInSoldOut);


        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), 2,getApplicationContext(),myRegisteredBikeRecyclerAdapterSold,myRegisteredBikeRecyclerAdapterSelling);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new  ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        Log.e("isSelling", String.valueOf(position));
                        isSellingTab.setChecked(true);
                        soldOutTab.setChecked(false);
                        break;
                    case 1:
                        Log.e("soldOut", String.valueOf(position));
                        soldOutTab.setChecked(true);
                        isSellingTab.setChecked(false);
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
                    case R.id.isSelling:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.SoldOut:
                        viewPager.setCurrentItem(1, true);
                        break;
                    default:
                        Log.e("아무것도 클릭안됨", "err");
                        return;
                }
            }
        });
        }

    @Override
    public void onClick() {
        GetRegisteredList getRegisteredList = new GetRegisteredList();
        getRegisteredList.execute();
        Log.e("callback","실행");

    }

    public void checkIsEmpty()
    {
        View view = viewPager.getChildAt(0);
        View sold = viewPager.getChildAt(1);
        Log.e("view null?",String.valueOf(view));
        Log.e("isEmplyInselling",String.valueOf(isEmptyInSelling));
        Log.e("isEmptyinSold",String.valueOf(isEmptyInSold));

        try {
            if(isEmptyInSelling){
                view.findViewById(R.id.noResultGuideTextInSelling).setVisibility(View.VISIBLE);
                view.findViewById(R.id.noResultImageInSelling).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.noResultImageInSelling).setVisibility(View.GONE);
                view.findViewById(R.id.noResultGuideTextInSelling).setVisibility(View.GONE);
            }
            if(isEmptyInSold){
                sold.findViewById(R.id.noResultGuideTextInSoldView).setVisibility(View.VISIBLE);
                sold.findViewById(R.id.noResultImageInSoldView).setVisibility(View.VISIBLE);
            }else{
                sold.findViewById(R.id.noResultImageInSoldView).setVisibility(View.GONE);
                sold.findViewById(R.id.noResultGuideTextInSoldView).setVisibility(View.GONE);
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }


        viewPager.invalidate();
    }

    public void setRegisteredList(List<ResponseModelList> list,String code){
        if(code.equals("중지됨")) {
            Log.e("리스트갯수",String.valueOf(list.size()));
                if(list.size()>0){
                    myRegisteredBikeRecyclerAdapterSold.addLastModelData(list, 1);
                    isEmptyInSold = false;
                }else{
                    myRegisteredBikeRecyclerAdapterSold.addLastModelData(list, 1);
                    isEmptyInSold = true;
                        Log.e("매물없어?","00");
                    }
        }else{
            if(list.size()==0){
                myRegisteredBikeRecyclerAdapterSelling.addData(list, 0);
                isEmptyInSelling = true;
            }else {
                myRegisteredBikeRecyclerAdapterSelling.addData(list, 0);
                isEmptyInSelling = false;
            }
        }
        checkIsEmpty();
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
                        if(responseModelList.size()>=0){
//                            IsSellingCategory isSellingCategory2 = (IsSellingCategory)getSupportFragmentManager().findFragmentById(R.id.fragmentIsSelling);
                            setRegisteredList(responseModelList.stream().filter(e -> e.getDeal_status().contentEquals("판매중")).collect(Collectors.toList()),"판매중");
                            setRegisteredList( responseModelList.stream().filter(e -> e.getDeal_status().contentEquals("중지됨")).collect(Collectors.toList()),"중지됨");
                            Log.e("responseModellist","가져옴.");
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

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            id = userInfo.getString("user-id", null);
        }
    }

}


class TabPagerAdapter extends FragmentPagerAdapter {

    // Count number of tabs
    private int tabCount;
    Context context;
    MyRegisteredBikeRecyclerAdapter myRegisteredBikeRecyclerAdapterSold;
    MyRegisteredBikeRecyclerAdapter myRegisteredBikeRecyclerAdapterSelling;
    public TabPagerAdapter(FragmentManager fm, int tabCount, Context context,MyRegisteredBikeRecyclerAdapter sold,MyRegisteredBikeRecyclerAdapter selling) {
        super(fm);
        this.tabCount = tabCount;
        this.context = context;
        myRegisteredBikeRecyclerAdapterSold = sold;
        myRegisteredBikeRecyclerAdapterSelling = selling;
    }


    @SuppressLint("ResourceType")
    @Override
    public Fragment getItem(int position) {

        android.support.v4.app.Fragment fragments[] = {IsSellingCategory.newInstance(position,myRegisteredBikeRecyclerAdapterSelling), SoldOutCategory.newInstance(position,myRegisteredBikeRecyclerAdapterSold)};

        return fragments[position];
        // Returning the current tabs

    }

    @Override
    public int getCount() {
        return tabCount;
    }


}



