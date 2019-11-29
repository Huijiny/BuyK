package com.buyk.crocompany.buyk_android.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyk.crocompany.buyk_android.Event.BottomSheetEvent;
import com.buyk.crocompany.buyk_android.Filtter;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.soldBikeView;
import com.buyk.crocompany.buyk_android.util.RecyclerItemClickListener;
import com.buyk.crocompany.buyk_android.util.RecyclerView.BottomSheetAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class SelectLocationFromSoldBikeBottomSheet extends android.support.design.widget.BottomSheetDialogFragment {

    RecyclerView bottomSheetRecyclerView;
    LinearLayoutManager mLayoutManager;
    BottomSheetAdapter bottomSheetAdapter;
    View view;
    View activityView;
    String locationData[] = {"서울", "부산", "인천", "광주", "울산", "대전", "세종", "대구", "경기", "강원", "경남", "경북", "충북", "충남", "전북", "전남", "제주",""};

    ArrayList<String>test = new ArrayList<>();
    public static SelectLocationFromSoldBikeBottomSheet getInstance() {
        return new SelectLocationFromSoldBikeBottomSheet();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        initView();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        InitView initView = new InitView();
        initView.execute();
    }

    public void initView(){
        bottomSheetRecyclerView = (RecyclerView)view.findViewById(R.id.bottomSheetRecyclerView);
        bottomSheetAdapter = new BottomSheetAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        bottomSheetRecyclerView.setLayoutManager(layoutManager);
        bottomSheetRecyclerView.setAdapter(bottomSheetAdapter);
        bottomSheetRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(bottomSheetRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        change_code(bottomSheetAdapter.getData(position));
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }
    public void change_code(String region){
        String new_code="";
        switch (region){
            case "서울":
                new_code="SU";
                break;
            case "부산":
                new_code = "BS";
                break;
            case "인천":
                new_code="IC";
                break;
            case "광주":
                new_code="GJ";
                break;
            case "울산":
                new_code="US";
                break;
            case "대전":
                new_code="DJ";
                break;
            case "세종":
                new_code = "SJ";
                break;
            case "대구":
                new_code = "DG";
                break;
            case "경기":
                new_code = "GG";
                break;
            case "강원":
                new_code = "GW";
                break;
            case "경남":
                new_code = "GN";
                break;
            case "경북":
                new_code = "GB";
                break;
            case "충북":
                new_code = "CB";
                break;
            case "충남":
                new_code = "CN";
                break;
            case "전북":
                new_code = "JB";
                break;
            case"전남":
                new_code = "JN";
                break;
            case "제주" :
                new_code = "JJ";
                break;
        }

        EventBus.getDefault().post(new BottomSheetEvent(region,"location"));
        ((soldBikeView)getActivity()).itemPost.setDeal_area(new_code);
    }

    class InitView extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected Void doInBackground(Integer... params) {
            addData();
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
    public void addData() {
            bottomSheetAdapter.addLocationData(locationData);
            test.forEach(e->Log.e("test",e));
    }


}
