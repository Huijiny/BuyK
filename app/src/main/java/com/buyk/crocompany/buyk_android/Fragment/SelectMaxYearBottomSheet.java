package com.buyk.crocompany.buyk_android.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.Filtter;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.util.RecyclerItemClickListener;
import com.buyk.crocompany.buyk_android.util.RecyclerView.BottomSheetAdapter;

import java.util.ArrayList;


public class SelectMaxYearBottomSheet extends android.support.design.widget.BottomSheetDialogFragment {

    RecyclerView bottomSheetRecyclerView;
    LinearLayoutManager mLayoutManager;
    BottomSheetAdapter bottomSheetAdapter;
    View view;
    View activityView;
    ArrayList<String> deviceYearMin = new ArrayList<String>();
    ArrayList<String> deviceYearMax = new ArrayList<>();

    boolean spinnerLeftClickCheck;
    boolean spinnerRightClickCheck;
    int minYearValue;
    public static SelectMaxYearBottomSheet getInstance() {
        return new SelectMaxYearBottomSheet();
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
            initView(view);
        activityView = inflater.inflate(R.layout.search_fliter, container, false);
        initView(view);
        return view;
    }

    public void initView(View view){
        bottomSheetRecyclerView = (RecyclerView)view.findViewById(R.id.bottomSheetRecyclerView);
        bottomSheetAdapter = new BottomSheetAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        bottomSheetRecyclerView.setLayoutManager(layoutManager);
        bottomSheetRecyclerView.setAdapter(bottomSheetAdapter);
        bottomSheetRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(bottomSheetRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(!bottomSheetAdapter.getData(position).equals("최대연식")){
                            checkAnotherSheet(Integer.parseInt(bottomSheetAdapter.getData(position)),position);
                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

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
            spinnerLeftClickCheck = ((Filtter)getActivity()).spinnerLeftClickCheck;
            spinnerRightClickCheck = ((Filtter)getActivity()).spinnerRightClickCheck;

            if(((Filtter)getActivity()).spinnerLeftClickCheck == true){
                minYearValue = ((Filtter)getActivity()).filterData.getFitterMinYear();
                Log.e("dd", String.valueOf(minYearValue));
            }
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
            Log.e("1","1");
            deviceYearMax.add("최대연식");
            for (int z=2019;z>=1950;z--){
                deviceYearMax.add(String.valueOf(z));
            }
        }
        if(spinnerLeftClickCheck== false && spinnerRightClickCheck == true){
            Log.e("1","1");
            deviceYearMax.add("최대연식");
            for (int z=2019;z>=1950;z--){
                deviceYearMax.add(String.valueOf(z));
            }
        }
        if(spinnerLeftClickCheck == true) {
            Log.e("2","2");
            deviceYearMax.add("최대연식");
            for(int z=2019; z>=minYearValue; z--){
                deviceYearMax.add(String.valueOf(z));
            }
        }

    }
    public void updateAdapterData(){
        bottomSheetAdapter.addMinYearData(deviceYearMax);
    }
    public void checkAnotherSheet(int maxYearValue, int position){

        ((Filtter)getActivity()).filterData.setFitterMaxYear(Integer.parseInt(bottomSheetAdapter.getData(position)));
        ((Filtter)getActivity()).setMaxYearBtnText(bottomSheetAdapter.getData(position));
        ((Filtter)getActivity()).spinnerRightClickCheck = true;
    }

}
