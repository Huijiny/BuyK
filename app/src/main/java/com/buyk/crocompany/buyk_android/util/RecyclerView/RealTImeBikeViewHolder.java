package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public class RealTImeBikeViewHolder extends RecyclerView.ViewHolder {
    public TextView realTimeBikePrice;
    public ImageView realTimeBikeImage;
    public TextView realTimeBindTimeText;
    public TextView mainBikeYearnDistance;
    public RealTImeBikeViewHolder(View itemView) {
        super(itemView);

        realTimeBikePrice = (TextView)itemView.findViewById(R.id.mainBikePrice);
        realTimeBikeImage = (ImageView)itemView.findViewById(R.id.mainBikeImage);
        realTimeBindTimeText = (TextView)itemView.findViewById(R.id.timeText);
        mainBikeYearnDistance = (TextView)itemView.findViewById(R.id.mainBikeYearnDistance);

    }
}
