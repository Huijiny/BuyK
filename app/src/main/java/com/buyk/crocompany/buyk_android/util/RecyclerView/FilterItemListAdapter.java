package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.LocalData.FilterData;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.Translater;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by ahdguialee on 2018. 5. 15..
 */

public class FilterItemListAdapter extends RecyclerView.Adapter<FilterItemListViewHolder> {

    ArrayList<String> filterItemText = new ArrayList<>();
    FilterData filterDataAdapter = new FilterData();
    private Context context;
    ObjectUtils objectUtils = new ObjectUtils();
    HashMap<String,String>queryMap;
    Translater translater = new Translater();
    boolean isFilter = true;

    public void addData(boolean IsfilterData,FilterData filterData){
        queryMap = new HashMap<>();
        nullCheck(IsfilterData,filterData);
        notifyDataSetChanged();
    }

    @Override
    public FilterItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_filter_item, parent, false);
        context= parent.getContext();
        return new FilterItemListViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull FilterItemListViewHolder holder, int position) {
        holder.filterItemTextView.setText(filterItemText.get(position));
    }

    @Override
    public int getItemCount() {
        return filterItemText.size();
    }

    public void nullCheck(Boolean IsFilterData, FilterData filterData){
        Log.e("dd", String.valueOf(filterData.isTuning()));

        try{
            if(IsFilterData==false){
                filterItemText.add("필터없음");
            }
            if(objectUtils.isEmpty(filterData)){
                filterItemText.add("필터없음");
            }
            else if(IsFilterData==true) {
                Log.e("필터데이터 있음", "");
                if (filterData.isFilterNewcar() == false) {
                    Log.e("isFilterNewcar", "false");
                } else if(filterData.isFilterNewcar()== true && filterData.isFilterOldcar() == false){
                    filterItemText.add("신차");
                    queryMap.put("driven_distance__lte",String.valueOf(0));
                    isFilter = false;
                }
                if (filterData.isFilterOldcar() == false) {
                    Log.e("isFilterOldcar", "false");
                } else if(filterData.isFilterOldcar() == true && filterData.isFilterNewcar() ==false){
                    filterItemText.add("중고차");
                    isFilter = false;
                }
                if (TextUtils.isEmpty(filterData.getFilterRangeTotalPrice())) {

                } else {
                    filterItemText.add(filterData.getFilterRangeTotalPrice());
                    String gte = filterData.getFitterRangePriceMin();
                    String lte = filterData.getFitterRangePriceMax();
                    Log.e("price__lte+gte",lte+gte);

                    if(!TextUtils.isEmpty(gte)){
                        Log.e("price__gte",gte);
                        queryMap.put("price__gte",gte);
                        if(!TextUtils.isEmpty(lte) && !TextUtils.isEmpty(gte)) {
                            queryMap.put("price__lte",lte);
                        }
                    }
                    else if(!TextUtils.isEmpty(lte)) {
                        queryMap.put("price__lte",lte);
                        Log.e("price__lte",lte);
                        if(!TextUtils.isEmpty(lte) && !TextUtils.isEmpty(gte)) {
                            queryMap.put("price__gte",gte);
                        }
                    }

                }
                if (TextUtils.isEmpty(filterData.getFilterRangeTotalDistance())) {

                } else {
                    String gte = filterData.getFitterRangeDistanceMin();
                    String lte = filterData.getFitterRangeDistanceMax();
                    Log.e("gte,lte",gte+lte);
                    filterItemText.add(filterData.getFilterRangeTotalDistance());
                    if(!lte.equals("제한없음")&&!TextUtils.isEmpty(lte)){
                        Log.e("driven_distance__lte",lte);
                        queryMap.put("driven_distance__lte",lte);
                        if(!TextUtils.isEmpty(gte) && !TextUtils.isEmpty(lte)) {
                            queryMap.put("driven_distance__gte",gte);
                        }

                    }
                    else if(!gte.equals("0")&&!TextUtils.isEmpty(gte)) {
                        Log.e("driven_distance__gte",gte);
                        queryMap.put("driven_distance__gte",gte);
                        if(!TextUtils.isEmpty(gte) && !TextUtils.isEmpty(lte) && !lte.equals("제한없음")) {
                            queryMap.put("driven_distance__lte",lte);
                        }
                    }
                }
                if (TextUtils.isEmpty(filterData.getFilterLocation())) {
                    Log.e("location", "isNull");
                } else {
                    filterItemText.add(filterData.getFilterLocation());
                    queryMap.put("deal_area",translater.locationTranslate(filterData.getFilterLocation()));
                }
                String min = String.valueOf(filterData.getFitterMinYear());
                Log.e("min", min);
                String max = String.valueOf(filterData.getFitterMaxYear());
                if (!min.equals("0") || !max.equals("0")) {
                    if (min.equals("0") && !max.equals("0")) {
                        filterItemText.add(max);
                        queryMap.put("model_year__lte",max);
                    } else if (!min.equals("0") && max.equals("0")) {
                        filterItemText.add(min);
                        queryMap.put("model_year__gte",min);
                    } else {
                        filterItemText.add(min + "~" + max);
                        Log.e("필터연식", min + "~" + max);
                        queryMap.put("model_year__lte",max);
                        queryMap.put("model_year__gte",min);
                    }
                }
                else {

                }
                if (filterData.isNoAccident() == false) {
                    Log.e("payment_method","무사고false");

                } else {
                    filterItemText.add("무사고");
                    Log.e("payment_method","무사고true");

                }
                if (filterData.isChangeIsOk() == false) {

                } else {
                    filterItemText.add("대차/교환가능");
                    queryMap.put("payment_method__trade", String.valueOf(true));
                }
                if (filterData.isInstallmentIsOk() == false) {
                    Log.e("할부/카드/리스가능","false");
                } else {
                    filterItemText.add("할부/카드/리스가능");
                    Log.e("할부/카드/리스가능","true");
                    queryMap.put("payment_method__card", String.valueOf(true));
                    queryMap.put("payment_method__cash", String.valueOf(true));
                    queryMap.put("payment_method__lease", String.valueOf(true));
                    Log.e("payment_method",queryMap.get("payment_method__lease"));
                }
                if (filterData.isTuning() == false) {
                    Log.e("isTuning", "false");
                } else {
                    filterItemText.add("순정");
                }
                if(queryMap.size()<1 && isFilter ==true){
                    filterItemText.add("필터없음");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public HashMap<String, String> getQueryMap() {
        return queryMap;
    }
}