package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public class SearchModelViewHolder extends RecyclerView.ViewHolder {
    public TextView searchModelText;


    public SearchModelViewHolder(View itemView) {
        super(itemView);

        searchModelText = (TextView)itemView.findViewById(R.id.searchModelName);
    }
}
