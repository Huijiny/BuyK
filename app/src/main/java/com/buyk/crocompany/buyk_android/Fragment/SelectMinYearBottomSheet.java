package com.buyk.crocompany.buyk_android.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.Filtter;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinResponse;
import com.buyk.crocompany.buyk_android.util.RecyclerItemClickListener;
import com.buyk.crocompany.buyk_android.util.RecyclerView.BottomSheetAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectMinYearBottomSheet extends android.support.design.widget.BottomSheetDialogFragment {

    RecyclerView bottomSheetRecyclerView;
    LinearLayoutManager mLayoutManager;
    BottomSheetAdapter bottomSheetAdapter;
    View view;
    View activityView;

    boolean spinnerClickCheck;
    boolean spinnerLeftClickCheck;
    boolean spinnerRightClickCheck;
    ArrayList<String> deviceYearMin = new ArrayList<String>();
    ArrayList<String> deviceYearMax = new ArrayList<>();
    int minYearValue;
    int maxYearValue;
    public static SelectMinYearBottomSheet getInstance() {
        return new SelectMinYearBottomSheet();
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
        bottomSheetRecyclerView = (RecyclerView)view.findViewById(R.id.bottomSheetRecyclerView);
        bottomSheetAdapter = new BottomSheetAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        bottomSheetRecyclerView.setLayoutManager(layoutManager);
        bottomSheetRecyclerView.setAdapter(bottomSheetAdapter);
        bottomSheetRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(bottomSheetRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(!bottomSheetAdapter.getData(position).equals("최소연식")){
                            checkAnotherSheet(Integer.parseInt(bottomSheetAdapter.getData(position)), position);
                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        spinnerLeftClickCheck = ((Filtter)getActivity()).spinnerLeftClickCheck;
        spinnerRightClickCheck = ((Filtter)getActivity()).spinnerRightClickCheck;

        if(((Filtter)getActivity()).spinnerRightClickCheck == true){
            maxYearValue = ((Filtter)getActivity()).filterData.getFitterMaxYear();
        }

        // 뷰에 데이터를 넣는 작업 등을 할 추가할 수 있음
    }
    @Override
    public void onStart() {
        super.onStart();
        InitView initView = new InitView();
        initView.execute();
    }
    class InitView extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected Void doInBackground(Integer... params) {
            initList();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateAdapterData();
            bottomSheetAdapter.notifyDataSetChanged();


            super.onPostExecute(aVoid);

        }
    }
    public void initList(){
        deviceYearMax.clear();
        deviceYearMin.clear();

        if(spinnerLeftClickCheck== false && spinnerRightClickCheck == false){
            deviceYearMin.add("최소연식");
            for (int z=1950;z<=2019;z++){
                deviceYearMin.add(String.valueOf(z));
            }
        }
        if(spinnerLeftClickCheck== true && spinnerRightClickCheck == false){
            deviceYearMin.add("최소연식");
            for (int z=1950;z<=2019;z++){
                deviceYearMin.add(String.valueOf(z));
            }
        }
        if(spinnerRightClickCheck == true) {

            deviceYearMin.add("최소연식");
            for(int z=1950; z<=maxYearValue; z++){
                deviceYearMin.add(String.valueOf(z));
            }
        }

    }
    public void updateAdapterData(){
        bottomSheetAdapter.addMinYearData(deviceYearMin);
    }
    public void checkAnotherSheet(int minYearValue, int position){


             ((Filtter)getActivity()).filterData.setFitterMinYear(Integer.parseInt(bottomSheetAdapter.getData(position)));
            ((Filtter)getActivity()).setMinYearBtnText(String.valueOf(minYearValue));
            ((Filtter)getActivity()).spinnerLeftClickCheck = true;
        }
}


