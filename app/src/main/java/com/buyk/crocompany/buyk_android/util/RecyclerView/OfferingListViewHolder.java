package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 5. 15..
 */

public class OfferingListViewHolder extends RecyclerView.ViewHolder {


    public ImageView offeringImage;
    public TextView offeringYear,offeringPrice;
    public ToggleButton offeringZeemBtn;

    public OfferingListViewHolder(View itemView) {
        super(itemView);

        offeringImage = (ImageView)itemView.findViewById(R.id.offeringImage);
        offeringYear = (TextView)itemView.findViewById(R.id.offeringYear);
        offeringPrice = (TextView)itemView.findViewById(R.id.offeringPrice);
        offeringZeemBtn = (ToggleButton)itemView.findViewById(R.id.offeringZeemBtn);
    }
}
