package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public class ZeemBikeViewHolder extends RecyclerView.ViewHolder {
    public TextView zeembikePriceText;
    public ImageView zeemBikeImageView;
    public TextView zeemBikeTimeText;
    public TextView zeemYearNdistanceText;
    public ZeemBikeViewHolder(View itemView) {
        super(itemView);

        zeembikePriceText = (TextView)itemView.findViewById(R.id.mainBikePrice);
        zeemBikeImageView = (ImageView)itemView.findViewById(R.id.mainBikeImage);
        zeemBikeTimeText = (TextView)itemView.findViewById(R.id.timeText);
        zeemYearNdistanceText = (TextView)itemView.findViewById(R.id.mainBikeYearnDistance);
    }
}
