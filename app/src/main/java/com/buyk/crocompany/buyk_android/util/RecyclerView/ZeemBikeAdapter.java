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

public class ZeemBikeAdapter extends RecyclerView.Adapter<ZeemBikeViewHolder> {

    ArrayList<ResponseModelList> zemmBikeItemPriceList = new ArrayList<>();
    private Context context;

    public void addData(List<ResponseModelList>zeemBikeList, int index){
        zemmBikeItemPriceList.addAll(zeemBikeList);
        Log.e("zemmBikeItemPriceList",zemmBikeItemPriceList.get(0).getDeal_area());
        notifyDataSetChanged();
    }

    @Override
    public ZeemBikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recyler_view_component, parent, false);
        context = parent.getContext();
        return new ZeemBikeViewHolder(view) {
        };
    }
    @Override
    public void onBindViewHolder(ZeemBikeViewHolder holder, int position) {
        ConvertKmOrPrice convertKmOrPrice = new ConvertKmOrPrice();
        DateCaculater dateCaculater = new DateCaculater();
        String distance = convertKmOrPrice.convertDistance(zemmBikeItemPriceList.get(position).getDriven_distance());
        String createdTime = zemmBikeItemPriceList.get(position).getCreated_at();
        long time = dateCaculater.timeConverterNow(createdTime);
        String yearNdistance = zemmBikeItemPriceList.get(position).getModel_year()+"년 | "+ distance ;
        Log.e("time", String.valueOf(time));
        String recentTime =dateCaculater.convertRecentTime(createdTime, time);
        Log.e("searchModelItemList.get", String.valueOf(zemmBikeItemPriceList.get(position)));
        String realTimeBikeItemPriceText = convertKmOrPrice.convertPrice(zemmBikeItemPriceList.get(position).getPrice());
        holder.zeembikePriceText.setText(realTimeBikeItemPriceText);
        holder.zeemBikeTimeText.setText(recentTime);
        holder.zeemYearNdistanceText.setText(yearNdistance);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent gotoModelDetialActivity = new Intent(context, OfferingDetail.class);
                gotoModelDetialActivity.putExtra("detailData", zemmBikeItemPriceList.get(position));
                context.startActivity(gotoModelDetialActivity);
            }
        });
        Transformation transformation = new RoundedCornersTransformation(10, 0,RoundedCornersTransformation.CornerType.TOP);
        if(zemmBikeItemPriceList.get(position).getImages().size()>0){
            Picasso.with(context).load(API_URL+zemmBikeItemPriceList.get(position).getImages().get(0).getSmall_image()).centerCrop().fit().transform(transformation).into(holder.zeemBikeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(zemmBikeItemPriceList.size() > 5) {
            return  5;
        }
        else{
            return zemmBikeItemPriceList.size();

        }
    }
}
