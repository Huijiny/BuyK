package com.buyk.crocompany.buyk_android.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.LocalData.RecentSearchData;
import com.buyk.crocompany.buyk_android.model.RemoteData.Brand;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerView.SearchModelListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;


public class SearchModel extends Fragment {

    RecyclerView searchModelRecyclerView;
    LinearLayoutManager mLayoutManager;
    SearchModelListAdapter mAdapter;
    EditText searchModelEditText;
    ImageView noSearchImage;
    TextView noSearchGuideText;
    TextView recentSearchList;
    TextView recentDataDelete;
    TextView noRecentResultGuideText;
    int SearchEditTextSize;

    //restapi
    com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel searchModel =new com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel();
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel> searchModelResponData;
    String searchEditText;
    EditText searchEditTextView;


    //realm
    Realm realmRecentSearchData;
    RecentSearchData recentSearchData;

    //매물리스트에서 오는지 바로 검색인지
    String whereFromIntent;

    Intent intent;
    ArrayList<String>model__in = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View resultExist = inflater.inflate(R.layout.fragment_model_search, container, false);

        View noResult = inflater.inflate(R.layout.fragment_model_search_no_result,container, false);
        restApiInit();
        initView(resultExist);
        Realm.init(getContext());
        intent = getActivity().getIntent();
        whereFromIntent= String.valueOf(intent.getSerializableExtra("whereFromIntent"));
        model__in = intent.getStringArrayListExtra("model__in");

        recentDataInputRealm();
        editTextListener();
        ButterKnife.bind(this, resultExist);

        return resultExist;

    }

    public void addData(List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel> searchModelResponData, String searchEditText){
//        SearchModelResultModel searchModelResultModel = new SearchModelResultModel();
//        searchModelResultModel.setBikeName(text);
        Log.e("모델리스트에 데이터 추가","확인");

        mAdapter.addData(searchModelResponData,searchEditText,whereFromIntent,model__in);
    }


    public void initView(View v){
        searchModelRecyclerView = (RecyclerView)v.findViewById(R.id.search_recyler_view_layout);

        searchModelRecyclerView.setHasFixedSize(true);
        mAdapter = new SearchModelListAdapter();
        searchModelRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        searchModelRecyclerView.setLayoutManager(mLayoutManager);
        noSearchImage = (ImageView)v.findViewById(R.id.noResultImage);
        noSearchGuideText= (TextView)v.findViewById(R.id.noResultGuideText);
        recentSearchList = (TextView)v.findViewById(R.id.recentSearchList);
        recentDataDelete = (TextView)v.findViewById(R.id.recentDataDelete);
        noRecentResultGuideText= (TextView)v.findViewById(R.id.noRecentResultGuideText);
    }



    public void restApiInit(){

        searchModelResponData = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApiInterface = retrofit.create(RetrofitInterface.class);
    }

    public void seachModelApiCall(String searchEditText){
        Call<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>> getSearchModel = restApiInterface.getSearchModel(searchEditText);
        getSearchModel.enqueue(new Callback<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>>() {
            @Override
            public void onResponse(Call<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>> call, Response<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>> response) {
                searchModelResponData=response.body();
                try {
                    noSearchImage.setVisibility(View.GONE);
                    noSearchGuideText.setVisibility(View.GONE);
                    searchModelRecyclerView.setVisibility(View.VISIBLE);
                    Log.e("searchapi테스트", searchModelResponData.get(0).getName());

                    addData(searchModelResponData,searchEditText);

                }catch (Exception e){
                    e.printStackTrace();
                    searchModelRecyclerView.setVisibility(View.VISIBLE);
                    addData(searchModelResponData,searchEditText);
                    recentDataInputRealm();

                    if(SearchEditTextSize>0) {
                        noSearchImage.setVisibility(View.VISIBLE);
                        noSearchGuideText.setVisibility(View.VISIBLE);
                        searchModelRecyclerView.setVisibility(View.GONE);
                        noRecentResultGuideText.setVisibility(View.GONE);
                        recentDataDelete.setVisibility(View.GONE);
                    }
                    Log.e("searchapiCatch",".");
                }



            }

            @Override
            public void onFailure(Call<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>> call, Throwable t) {
                Log.d("connectfail", t.toString());
            }

        });
    }
    @Nullable
    public void editTextListener(){
        searchModelEditText = (EditText)getActivity().findViewById(R.id.searchEditText);
        searchModelEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("edittext값", s.toString());
                seachModelApiCall(s.toString());
                SearchEditTextSize =s.length();
                if(s.length()>0){
                    recentSearchList.setVisibility(View.GONE);
                    recentDataDelete.setVisibility(View.GONE);
                    noRecentResultGuideText.setVisibility(View.GONE);
                }else {
                    noSearchImage.setVisibility(View.GONE);
                    noSearchGuideText.setVisibility(View.GONE);
                    recentSearchList.setVisibility(View.VISIBLE);
                    recentDataDelete.setVisibility(View.VISIBLE);
                    noRecentResultGuideText.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,

                                          int after) {
            }



            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }
    public void recentDataInputRealm(){
        realmRecentSearchData = Realm.getDefaultInstance();
        RealmResults<RecentSearchData> filterDataSize = realmRecentSearchData.where(RecentSearchData.class).findAll();
        Brand brand = new Brand();

        filterDataSize = realmRecentSearchData.where(RecentSearchData.class).findAll().distinct("id");
        searchModelResponData = new ArrayList<>();
        for(int i=0; i<filterDataSize.size();i++){
            searchModel = new com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel();
            searchModel.setId(filterDataSize.get(i).getId());
            searchModel.setName(filterDataSize.get(i).getName());
            brand.setEnglish_name(filterDataSize.get(i).getBrandEnglishName());
            brand.setName(filterDataSize.get(i).getBrandName());
            searchModel.setBrand(brand);
            searchModelResponData.add(searchModel);
            Log.e(String.valueOf(filterDataSize.get(i)),"sibal");
            noRecentResultGuideText.setVisibility(View.GONE);
            recentSearchList.setText("최근 검색목록");
            recentDataDelete.setVisibility(View.VISIBLE);
        }
        if(ObjectUtils.isEmpty(filterDataSize)){
            noRecentResultGuideText.setVisibility(View.VISIBLE);
            recentSearchList.setText("최근 검색목록이 없습니다");
            recentDataDelete.setVisibility(View.GONE);
        }
        addData(searchModelResponData,searchEditText);
        Log.e("최근검색목록", String.valueOf(searchModelResponData));
        Log.e("realm데이터들어가는거테스트", String.valueOf(filterDataSize));
    }
    public void recentAllDataDeleteRealm(){
        realmRecentSearchData = Realm.getDefaultInstance();

        RealmResults<RecentSearchData> filterDataSize = realmRecentSearchData.where(RecentSearchData.class).findAll();
        //필터니까 객체초기화해야됨
        realmRecentSearchData.beginTransaction();
        filterDataSize.deleteAllFromRealm();
        Log.e("realm객체삭제테스트", String.valueOf(filterDataSize));

        realmRecentSearchData.commitTransaction();
        searchModelResponData= new ArrayList<>();
        noRecentResultGuideText.setVisibility(View.VISIBLE);
        recentSearchList.setText("최근 검색목록이 없습니다");
        recentDataDelete.setVisibility(View.GONE);
        addData(searchModelResponData,searchEditText);


    }

    @OnClick(R.id.recentDataDelete)
    public void onClickRecentDataDelete(){
        Log.e("눌림","눌림");
        recentAllDataDeleteRealm();

    }
}


