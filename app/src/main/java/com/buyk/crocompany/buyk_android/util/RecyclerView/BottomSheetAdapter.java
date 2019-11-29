package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyk.crocompany.buyk_android.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ahdguialee on 2018. 8. 10..
 */

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetViewHolder> {

    ArrayList<String>inputDataList = new ArrayList<>();

    @Override
    public BottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_bottom_sheet, parent, false);
        return new BottomSheetViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetViewHolder holder, int position) {
        holder.bottomSheetTextView.setText(inputDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return inputDataList.size();
    }

    public void addLocationData(String[] inputData) {
        Arrays.stream(inputData).forEach(e -> inputDataList.add(e));

        inputDataList.forEach(s -> Log.e("inputDataList",inputDataList.get(inputDataList.indexOf(s))));
        Log.e("d","location");
    }
    public void addMinYearData(ArrayList<String> inputMinYear) {
        inputDataList.addAll(inputMinYear);
        notifyDataSetChanged();
        inputDataList.forEach(s -> Log.e("inputDataList",inputDataList.get(inputDataList.indexOf(s))));
        Log.e("d","min");
    }

    public String getData(int position) {

        return inputDataList.get(position);
    }
}
