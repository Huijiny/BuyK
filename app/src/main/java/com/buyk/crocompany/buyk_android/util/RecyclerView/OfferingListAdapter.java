package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.OfferingDetail;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.RemoteData.LikeModel;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.model.RequestMethod;
import com.buyk.crocompany.buyk_android.util.ConvertKmOrPrice;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Retrofit;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;


/**
 * Created by ahdguialee on 2018. 5. 15..
 */

public class OfferingListAdapter extends AnimalsAdapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    List<ResponseModelList>offeringItemList = new ArrayList<>();
    ArrayList<Integer>_viewTypeList =new ArrayList<>();
    private Context context;
    int addDataIndex=0;
    HashMap<String,String>headerMap = new HashMap<>();
    Retrofit retrofit;
    RetrofitInterface restApiInterface;
    int modelId;
    LikeModel likeModel = new LikeModel();
    LikeModel likeCancelModel = new LikeModel();
    ConvertKmOrPrice convertKmOrPrice = new ConvertKmOrPrice();
    public void addData(List<ResponseModelList> item, int viewType){
        addDataIndex+=1;
        Log.e("item",item.get(0).getModel());
        offeringItemList.addAll(item);
        Log.e("offeringItemList",offeringItemList.get(0).getModel());

        item.forEach(e -> _viewTypeList.add(viewType));

        notifyDataSetChanged();
    }
    public void addHeaderMap(HashMap<String,String> headerMap){
        this.headerMap = headerMap;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        switch (_viewTypeList.get(position)) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offering_list_online_item, parent, false);
        context = parent.getContext();
        Log.e("viewtype", String.valueOf(viewType));
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offering_list_item, parent, false);
            Log.e("viewType","바이크등록매물");
            return new OfferingListViewHolder(v);
        } else if (viewType == 1){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offering_list_online_item , parent, false);
            Log.e("viewType","온라인등록매물");

            return new OfferingOnlineItemListViewHolder(v);
        }
        else {
            return new OfferingOnlineItemListViewHolder(view);
        }
    }


    @Override
    public int getItemCount() {
        return offeringItemList.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String toBindOfferingYear =  offeringItemList.get(position).getDeal_area()+ " | "+  offeringItemList.get(position).getModel_year()+"년식 | " +  convertKmOrPrice.convertDistance(offeringItemList.get(position).getDriven_distance());
        String toBindOfferingPrice =  convertKmOrPrice.convertPrice(offeringItemList.get(position).getPrice());

        if (holder instanceof OfferingListViewHolder) {
            ((OfferingListViewHolder)holder).offeringPrice.setText(toBindOfferingPrice);
            ((OfferingListViewHolder)holder).offeringYear.setText(toBindOfferingYear);
            Transformation transformation = new RoundedCornersTransformation(25, 0,RoundedCornersTransformation.CornerType.TOP);
            if( offeringItemList.get(position).getImages().size()>0){
                Picasso.with(context).load(API_URL+ offeringItemList.get(position).getImages().get(0).getLarge_image()).centerCrop().transform(transformation).centerCrop().fit().into(((OfferingListViewHolder)holder).offeringImage);
            }
            if(offeringItemList.get(position).isLiked()) {
                ((OfferingListViewHolder)holder).offeringZeemBtn.setChecked(true);
            }

        } else {
            ((OfferingOnlineItemListViewHolder)holder).offeringOnlineItemPrice.setText("3,000만원");
        }
        ((OfferingListViewHolder)holder).offeringZeemBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    modelId =  offeringItemList.get(position).getId();
                    likeModel.setItem(modelId);
                    Log.e("modelId", String.valueOf(modelId));
                    RequestMethod requestMethod;
                    requestMethod = new RequestMethod();
                    requestMethod.initLikeModelRequest(headerMap,likeModel);
                }
                else
                {
                    modelId =  offeringItemList.get(position).getId();
                    likeCancelModel.setItem(modelId);
                    Log.e("modelCancelId", String.valueOf(modelId));
                    RequestMethod requestMethod;
                    requestMethod = new RequestMethod();
                    requestMethod.initLikeCancelRequest(headerMap,likeCancelModel);
                }

            }
        });

        ((OfferingListViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                modelId =  offeringItemList.get(position).getId();
                Log.e("modelPosition", String.valueOf(modelId));
                Intent gotoModelDetialActivity = new Intent(context, OfferingDetail.class);
                gotoModelDetialActivity.putExtra("detailData", offeringItemList.get(position));
                context.startActivity(gotoModelDetialActivity);
            }
        });
    }

    @Override
    public long getHeaderId(int position) {
//        Log.e("getHeaderId", String.valueOf(getItem(position).charAt(0)));
        return getItem(position).charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offering_list_view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;

        if(getItem(position).charAt(0)!='a') {
            textView.setText("온라인 수집매물");
            holder.itemView.setBackgroundColor(Color.BLACK);
            textView.setTextColor(Color.WHITE);
//            holder.itemView.setBackgroundColor(getRandomColor());
        }
        else{
            textView.setText("Buyk 등록매물");
//            holder.itemView.setBackgroundColor(getRandomColor());

        }
    }




}
