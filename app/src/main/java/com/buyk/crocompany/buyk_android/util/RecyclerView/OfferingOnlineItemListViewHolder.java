package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 5. 15..
 */

public class OfferingOnlineItemListViewHolder extends RecyclerView.ViewHolder {


    public TextView offeringOnlineItemYear,offeringOnlineItemPrice;

    public OfferingOnlineItemListViewHolder(View itemView) {
        super(itemView);

        offeringOnlineItemYear = (TextView)itemView.findViewById(R.id.offeringYear);
        offeringOnlineItemPrice = (TextView)itemView.findViewById(R.id.offeringPrice);
    }
}
