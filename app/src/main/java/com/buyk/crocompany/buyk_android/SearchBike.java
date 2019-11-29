package com.buyk.crocompany.buyk_android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel;
import com.buyk.crocompany.buyk_android.util.NoticeDialog;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.RecyclerItemClickListener;
import com.buyk.crocompany.buyk_android.util.RecyclerView.SearchModelListAdapterInSoldView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;


public class SearchBike extends Fragment {

    com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel searchModel =new com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel();
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    LinearLayoutManager mLayoutManager;
    List<SearchModel> searchModelResponData;
    RecyclerView searchModelRecyclerViewInSoldView;
    SearchModelListAdapterInSoldView mAdapteInSoldView;
    EditText searchModelEditText;
    int SearchEditTextSize;
    boolean afterSet;
    ImageView noSearchImage;
    TextView noSearchGuideText;
    ViewGroup rootView;
    LinearLayout noResult;
    InputMethodManager imm;
    LinearLayout cl;

    HashMap<String,String>headerMap;
    String token;
    String id;
    String uuid;
    List<ResponseModelList> responseModelList;

    public static SearchBike newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        SearchBike fragment = new SearchBike();
        fragment.setArguments(args);

        return fragment;
    }
    public SearchBike(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }
    @Override
    public void onResume() {

        super.onResume();

        imm =(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        rootView = (ViewGroup)layoutInflater.inflate(R.layout.activity_search_bike, viewGroup,false);
        searchModelEditText = (EditText)rootView.findViewById(R.id.etSearch);
        restApiInit();
        initView(rootView);
        editTextListener();
        removeKeyboard();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(((soldBikeView)getActivity())!=null) {
            if (isVisibleToUser) {
                if (afterSet) {
                    ((soldBikeView) getActivity()).setEnabled();
                } else {
                    ((soldBikeView) getActivity()).setDisabled();
                }
            }
        }
    }

    public void removeKeyboard()
    {
        cl = (LinearLayout)rootView.findViewById(R.id.searchBike);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(cl.getWindowToken(),0);
            }
        });
        searchModelRecyclerViewInSoldView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("뷰 클릭","00");
                imm.hideSoftInputFromWindow(searchModelRecyclerViewInSoldView.getWindowToken(),0);
            }
        });
        searchModelEditText.requestFocus();
    }

    public void initView(View v){
        searchModelEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                afterSet=false;
                searchModelEditText.setCursorVisible(true);
            }
        });
        searchModelRecyclerViewInSoldView= (RecyclerView)v.findViewById(R.id.recyclerViewInSoldView);
        searchModelRecyclerViewInSoldView.setHasFixedSize(true);
        mAdapteInSoldView = new SearchModelListAdapterInSoldView();
        searchModelRecyclerViewInSoldView.setAdapter(mAdapteInSoldView);
        searchModelRecyclerViewInSoldView.addOnItemTouchListener(
                new RecyclerItemClickListener(searchModelRecyclerViewInSoldView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ((soldBikeView)getActivity()).startAnim();
                        isAlreadyExist(searchModelResponData.get(position).getName(),position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {}
                })
        );
        mLayoutManager = new LinearLayoutManager(getActivity());
        searchModelRecyclerViewInSoldView.setLayoutManager(mLayoutManager);
        noSearchImage = (ImageView)v.findViewById(R.id.noResultImageInSoldView);
        noSearchGuideText= (TextView)v.findViewById(R.id.noResultGuideTextInSoldView);

        ((soldBikeView)getActivity()).setDisabled();
    }

    public void isAlreadyExist(String selectedModel,int position){

        GetRegisteredList getRegisteredList = new GetRegisteredList(selectedModel,position);
        getRegisteredList.execute();

    }

    public void restApiInit(){

        searchModelResponData = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApiInterface = retrofit.create(RetrofitInterface.class);
    }

    public void set(String name){
        searchModelEditText.setText(name);
        searchModelEditText.setCursorVisible(false);
        searchModelRecyclerViewInSoldView.setVisibility(View.GONE);
        afterSet=true;
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);


    }

    public void invisible()
    {
        noSearchGuideText.setVisibility(View.GONE);
        noSearchImage.setVisibility(View.GONE);
    }

    public void seachModelApiCall(String searchEditText){
        Call<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>> getSearchModel = restApiInterface.getSearchModel(searchEditText);
        getSearchModel.enqueue(new Callback<List<SearchModel>>() {
            @Override
            public void onResponse(Call<List<SearchModel>> call, Response<List<SearchModel>> response) {
                searchModelResponData=response.body();
                try {
                    noSearchImage.setVisibility(View.GONE);
                    noSearchGuideText.setVisibility(View.GONE);
                    searchModelRecyclerViewInSoldView.setVisibility(View.VISIBLE);
                    Log.e("searchapi테스트", searchModelResponData.get(0).getName());

                    mAdapteInSoldView.addData(searchModelResponData,searchEditText);

                }catch (Exception e){
                    e.printStackTrace();
                    searchModelRecyclerViewInSoldView.setVisibility(View.VISIBLE);
                    mAdapteInSoldView.addData(searchModelResponData,searchEditText);


                    if(SearchEditTextSize>0) {
                            noSearchImage.setVisibility(View.VISIBLE);
                            noSearchGuideText.setVisibility(View.VISIBLE);
                            searchModelRecyclerViewInSoldView.setVisibility(View.GONE);
                            if(afterSet==true){
                                invisible();
                            }
                        }
                    Log.e("searchapiCatch",".");
                }



            }

            @Override
            public void onFailure(Call<List<com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel>> call, Throwable t) {
                Log.d("connectfail", t.toString());
                noSearchImage.setVisibility(View.VISIBLE);
                noSearchGuideText.setVisibility(View.VISIBLE);
                searchModelRecyclerViewInSoldView.setVisibility(View.GONE);
            }

        });
    }



    @Nullable
    public void editTextListener(){
        searchModelEditText = (EditText)rootView.findViewById(R.id.etSearch);
        searchModelEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    searchModelEditText.setText("");
                    searchModelEditText.requestFocus();
                    searchModelEditText.setCursorVisible(true);
                }else{
                    noSearchGuideText.setVisibility(View.GONE);
                    noSearchImage.setVisibility(View.GONE);
                }
            }
        });
        searchModelEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("edittext값", s.toString());
                seachModelApiCall(s.toString());
                afterSet=false;
                SearchEditTextSize =s.length();

                if(SearchEditTextSize>0) {
                    noSearchImage.setVisibility(View.GONE);
                    noSearchGuideText.setVisibility(View.GONE);
                }


            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    class GetRegisteredList extends AsyncTask<Integer, Integer, Void> {
        String selectedModel;
        boolean isExist;
        int position;

        public GetRegisteredList(String selectedModel,int position) {
            this.selectedModel = selectedModel;
            this.position = position;
        }


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
                            isExist = responseModelList.stream().filter(e->e.getModel().equals(selectedModel) && !e.getDeal_status().equals("삭제됨")).count()>0;
                            if(isExist){
                                NoticeDialog nd;
                                String checkMes = "동일한 매물이 이미 회원님 계정으로 등록된 상태입니다.\nBuyK에서는 동일한 매물을 중복등록하실 수 없습니다.\n'수정하기'를 이용하시거나 기존 매물 삭제 후 재등록하시기 바랍니다.";

                                nd = new NoticeDialog(getContext());
                                nd.setContent(checkMes);
                                nd.show();
                                nd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        ((soldBikeView)getActivity()).stopAnim();
                                        searchModelEditText.setText(null);
                                        mAdapteInSoldView.addData(null,"");
                                        ((soldBikeView)getActivity()).setDisabled();
                                        searchModelEditText.requestFocus();
                                    }
                                });
                            }else{
                                Log.e("중복매물 아님","ㅇㅇ");
                                ((soldBikeView) getActivity()).itemPost.setModel_id(searchModelResponData.get(position).getId());
                                set(searchModelResponData.get(position).getBrand().getEnglish_name()+" - " + searchModelResponData.get(position).getName());
                                ((soldBikeView)getActivity()).stopAnim();
                                ((soldBikeView)getActivity()).setEnabled();
                            }
                            Log.e("responseModellist","가져옴.");
                        }
                    }
                    else {
                        ((soldBikeView) getActivity()).itemPost.setModel_id(searchModelResponData.get(position).getId());
                        set(searchModelResponData.get(position).getBrand().getEnglish_name()+" - " + searchModelResponData.get(position).getName());
                        ((soldBikeView)getActivity()).stopAnim();
                        ((soldBikeView)getActivity()).setEnabled();
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
        SharedPreferences userInfo =getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            id = userInfo.getString("user-id", null);
        }
    }





}
