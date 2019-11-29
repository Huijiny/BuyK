package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyk.crocompany.buyk_android.OfferingDetail;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.util.ConvertKmOrPrice;
import com.buyk.crocompany.buyk_android.util.DateCaculater;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public class RealTimeBikeAdapter extends RecyclerView.Adapter<RealTImeBikeViewHolder> {

    ArrayList<ResponseModelList> realTimeBikeItemPriceList = new ArrayList<>();
    private Context context;
    DateCaculater dateCaculater;

    public void addData(List<ResponseModelList> item, int index){
        realTimeBikeItemPriceList.addAll(item);
        notifyDataSetChanged();
    }

    @Override
    public RealTImeBikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recyler_view_component, parent, false);
        context= parent.getContext();
        return new RealTImeBikeViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RealTImeBikeViewHolder holder, int position) {
        ConvertKmOrPrice convertKmOrPrice = new ConvertKmOrPrice();
        dateCaculater = new DateCaculater();
        String distance = convertKmOrPrice.convertDistance(realTimeBikeItemPriceList.get(position).getDriven_distance());
        String yearNdistance = realTimeBikeItemPriceList.get(position).getModel_year()+ "년 | "+ distance;
        String createdTime = realTimeBikeItemPriceList.get(position).getCreated_at();
        long time = dateCaculater.timeConverterNow(createdTime);
        String recentTime =dateCaculater.convertRecentTime(createdTime, time);
        Log.e("searchModelItemList.get", String.valueOf(realTimeBikeItemPriceList.get(position)));
        String realTimeBikeItemPriceText = convertKmOrPrice.convertPrice(realTimeBikeItemPriceList.get(position).getPrice());
        holder.realTimeBikePrice.setText(realTimeBikeItemPriceText);
        holder.realTimeBindTimeText.setText(recentTime);
        holder.mainBikeYearnDistance.setText(yearNdistance);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent gotoModelDetialActivity = new Intent(context, OfferingDetail.class);
                gotoModelDetialActivity.putExtra("detailData", realTimeBikeItemPriceList.get(position));
                context.startActivity(gotoModelDetialActivity);
            }
        });
        Transformation transformation = new RoundedCornersTransformation(10, 0,RoundedCornersTransformation.CornerType.TOP);
        if(realTimeBikeItemPriceList.get(position).getImages().size() > 0 ){
            Picasso.with(context).load(API_URL +realTimeBikeItemPriceList.get(position).getImages().get(0).getSmall_image()).centerCrop().fit().transform(transformation).into(holder.realTimeBikeImage);
        }
    }

    @Override
    public int getItemCount() {
        if(realTimeBikeItemPriceList.size() > 5){
            return 5;
        }
        else {
            return realTimeBikeItemPriceList.size();
        }
    }
}
