package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.RemoteData.SearchModel;
import com.xeoh.android.texthighlighter.TextHighlighter;

import java.util.ArrayList;
import java.util.List;

public class SearchModelListAdapterInSoldView extends RecyclerView.Adapter<SearchModelViewHolder> {
    ArrayList<SearchModel> searchModelItemList = new ArrayList<>();
    Context context;
    String name;
    TextView text;
    View view;
    String CompareSearchEditText;
    TextHighlighter textHighlighter = new TextHighlighter();

    public void addData(List<SearchModel> item,String searchEditText){
        CompareSearchEditText = searchEditText;
        searchModelItemList.clear();
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

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_model_search_list_item, parent, false);
        context = parent.getContext();

        Log.e("context",String.valueOf(context));
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


    }

    @Override
    public int getItemCount() {
        return searchModelItemList.size();
    }


}
