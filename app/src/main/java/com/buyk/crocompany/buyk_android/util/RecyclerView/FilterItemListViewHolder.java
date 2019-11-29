package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 5. 15..
 */

public class FilterItemListViewHolder extends RecyclerView.ViewHolder {


    public TextView filterItemTextView;

    public FilterItemListViewHolder(View itemView) {
        super(itemView);

        filterItemTextView = (TextView)itemView.findViewById(R.id.list_filter_item_text);
    }
}
