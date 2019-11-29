package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyk.crocompany.buyk_android.OfferingList;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.LocalData.RecentSearchData;
import com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.xeoh.android.texthighlighter.TextHighlighter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public class SearchModelListAdapter extends RecyclerView.Adapter<SearchModelViewHolder> {

    //realm
    Realm realmRecentSearchData;
    RecentSearchData recentSearchData;
    ArrayList<SearchModel> searchModelItemList = new ArrayList<>();
    String CompareSearchEditText;
    TextHighlighter textHighlighter = new TextHighlighter();
    String whereAreActivityGo;
    ArrayList<String>model__in = new ArrayList<>();


    Context context;
    Activity activity;
    public void addData(List<SearchModel> item, String searchEditText, String whereFromIntent, ArrayList<String> model__in){
        CompareSearchEditText = searchEditText;
        searchModelItemList.clear();
        whereAreActivityGo = whereFromIntent;
        if(!ObjectUtils.isEmpty(model__in)){
            this.model__in.addAll(model__in);
        }
        notifyItemRangeRemoved(0,searchModelItemList.size());
        try {
            searchModelItemList.addAll(item);

        }catch (Exception e){
         e.printStackTrace();
        }
        notifyDataSetChanged();

    }

    @Override
    public SearchModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_model_search_list_item, parent, false);
        Realm.init(parent.getContext());
        context = parent.getContext();
        activity = (Activity)context;

        return new SearchModelViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(SearchModelViewHolder holder, int position) {

        Log.e("searchModelItemList.get", String.valueOf(searchModelItemList.get(position)));
        String searchModelItem = searchModelItemList.get(position).getBrand().getEnglish_name()+ " - " + searchModelItemList.get(position).getName();
        holder.searchModelText.setText(searchModelItem);

        if(!TextUtils.isEmpty(CompareSearchEditText)) {
                textHighlighter.setForegroundColor(Color.rgb( 247, 94, 0));
                textHighlighter.setBold(true);
                textHighlighter.addTarget(holder.searchModelText);
                textHighlighter .highlight(CompareSearchEditText, TextHighlighter.CASE_INSENSITIVE_MATCHER);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!TextUtils.isEmpty(whereAreActivityGo)) {
                    Log.e("whereAreActivityGo",whereAreActivityGo);
                    if(whereAreActivityGo.equals("offeringList")) {
                        recentDataInputRealm(searchModelItemList.get(position));
                        Log.e("클릭한 모델 데이터", String.valueOf(searchModelItemList.get(position)));
                        Intent gotoOfferingListActivity = new Intent(context, OfferingList.class);
                        gotoOfferingListActivity.putExtra("model",searchModelItemList.get(position).getId());
                        gotoOfferingListActivity.putExtra("modelName",searchModelItemList.get(position).getName());
                        gotoOfferingListActivity.putExtra("whereFromIndent","plusModel");
                        model__in.add(String.valueOf(searchModelItemList.get(position).getId()));
                        gotoOfferingListActivity.putStringArrayListExtra("model__in",model__in);
                        gotoOfferingListActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        activity.startActivity(gotoOfferingListActivity);
                        activity.overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
                    }
                    else {
                        Log.e("whereAreActivityGo","없다");
                        recentDataInputRealm(searchModelItemList.get(position));
                        Log.e("클릭한 모델 데이터", String.valueOf(searchModelItemList.get(position).getName()));
                        Intent gotoOfferingListActivity = new Intent(context, OfferingList.class);
                        gotoOfferingListActivity.putExtra("model",searchModelItemList.get(position).getId());
                        gotoOfferingListActivity.putExtra("modelName",searchModelItemList.get(position).getBrand().getEnglish_name()+" - "+searchModelItemList.get(position).getName());
                        context.startActivity(gotoOfferingListActivity);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchModelItemList.size();
    }


    public void recentDataInputRealm(SearchModel onClickSeachModelData){
        realmRecentSearchData = Realm.getDefaultInstance();

        RealmResults<RecentSearchData> filterDataSize = realmRecentSearchData.where(RecentSearchData.class).findAll();
        //필터니까 객체초기화해야됨
        realmRecentSearchData.beginTransaction();
//        filterDataSize.deleteAllFromRealm();
        Log.e("realm객체삭제테스트", String.valueOf(filterDataSize));

        recentSearchData = realmRecentSearchData.createObject(RecentSearchData.class); // 새 객체 만들기
        recentSearchData.setId(onClickSeachModelData.getId());
        recentSearchData.setName(onClickSeachModelData.getName());
        recentSearchData.setBrandEnglishName(onClickSeachModelData.getBrand().getEnglish_name());
        recentSearchData.setBrandName(onClickSeachModelData.getBrand().getName());
        realmRecentSearchData.commitTransaction();
        filterDataSize = realmRecentSearchData.where(RecentSearchData.class).findAll();
        Log.e("realm데이터들어가는거테스트", String.valueOf(filterDataSize));
    }



}
