package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 8. 10..
 */

public class BottomSheetViewHolder extends RecyclerView.ViewHolder {
    TextView bottomSheetTextView;
    public BottomSheetViewHolder(View itemView) {
        super(itemView);
        bottomSheetTextView = (TextView)itemView.findViewById(R.id.bottomSheetItemText);
    }
}
