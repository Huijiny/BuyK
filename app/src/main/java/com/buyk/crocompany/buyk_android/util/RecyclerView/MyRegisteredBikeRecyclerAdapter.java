package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.OfferingDetail;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.RemoteData.ItemResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.ItemUpdatePatch;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.ConvertKmOrPrice;
import com.buyk.crocompany.buyk_android.util.NoticeDialogTwoButtons;
import com.buyk.crocompany.buyk_android.util.UploadView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;

public class MyRegisteredBikeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public static final int VIEW_TYPE_A = 0;
    public static final int VIEW_TYPE_B = 1;
    private UploadView mCallback;
    private UploadView mCallback2;
    private UploadView Callback;

    Realm localUserDataRealm;
    String uuid;
    String token;
    String userId;
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    HashMap<String,String> headerMap;
    ItemResponse itemResponse;
    ItemUpdatePatch itemUpdatePatch;
    NoticeDialogTwoButtons nd;

    public ArrayList<ResponseModelList> registeredList = new ArrayList<>();
    public ArrayList<ResponseModelList> lastModels = new ArrayList<>();
    int viewIndex;
    String status=null;
    ConvertKmOrPrice convertKmOrPrice;

    public void addData(List<ResponseModelList> item, int viewIndex){
        if(item.size()!=0){
            registeredList.clear();
        registeredList.addAll(item);
        Log.e("판매중 모델 갯수",String.valueOf(registeredList.size()));
        }else{
            registeredList.clear();
            Toast.makeText(context,"모델없어(판매중)",Toast.LENGTH_SHORT);
        }
        this.viewIndex = viewIndex;
       this. notifyDataSetChanged();
    }
    public void addLastModelData(List<ResponseModelList> item, int veiwIndex)
    {
        if(item.size()!=0) {
            lastModels.clear();
            lastModels.addAll(item);
            Log.e("지난매물 갯수",String.valueOf(lastModels.size()));
        }else{
            lastModels.clear();
            Toast.makeText(context,"모델없어(지난매물)",Toast.LENGTH_SHORT);
        }
            this.viewIndex = veiwIndex;
           this. notifyDataSetChanged();

    }

    public  MyRegisteredBikeRecyclerAdapter(Context mcontext, int viewIndex,UploadView listener)
    {
        this.context=mcontext;
        this.viewIndex = viewIndex;
        this.mCallback = listener;
        Log.e("view index",String.valueOf(viewIndex));
    }

    @Override
    public int getItemViewType(int position) {
        if (viewIndex==0) {
            return VIEW_TYPE_A;
        } else {
            return VIEW_TYPE_B;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        context = parent.getContext();
        //Activity activity = parent.getRootView();
        convertKmOrPrice = new ConvertKmOrPrice();
        if(viewIndex == 0) {
           v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_my_registered_bike_item_on_selling,parent,false);
           Log.e("판매중 리사이클러뷰 띄움","ㅇㅇ");
           return new viewHolder(v);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_my_registered_bike_item_on_sold,parent,false);
            Log.e("판매완료 리사이클러 뷰 띄움","ㅇㅇ");
            return new viewHolder2(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof viewHolder) {
            String price = convertKmOrPrice.convertPrice(registeredList.get(position).getPrice());
            String model = registeredList.get(position).getModel();
            ((viewHolder)holder).priceText.setText(price);
            ((viewHolder)holder).modelName.setText(model);

            ((viewHolder) holder).onSelling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,OfferingDetail.class);
                    intent.putExtra("detailData", registeredList.get(position));
                    context.startActivity(intent);
                }
            });
            Transformation transformation = new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL);
            if (registeredList.get(position).getImages().size() > 0) {
                Picasso.with(context).load(API_URL + registeredList.get(position).getImages().get(0).getSmall_image()).centerCrop().fit().transform(transformation).into(((viewHolder)holder).registeredImg);
            }

        }else{
            String price = convertKmOrPrice.convertPrice(lastModels.get(position).getPrice());
            String model = lastModels.get(position).getModel();
            ((viewHolder2)holder).modelName.setText(model);
            ((viewHolder2)holder).priceText.setText(price);
            ((viewHolder2) holder).soldOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,OfferingDetail.class);
                    intent.putExtra("detailData", lastModels.get(position));
                    context.startActivity(intent);
                }
            });

            Transformation transformation = new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL);

            if(lastModels.get(position).getImages().size()>0){
                Picasso.with(context).load(API_URL + lastModels.get(position).getImages().get(0).getSmall_image()).centerCrop().fit().transform(transformation).into(((viewHolder2)holder).registeredImg);
            }
        }
    }



    @Override
    public int getItemCount() {
        Log.e("registeredListSize", String.valueOf(registeredList.size()));
        if(viewIndex==0) {
            return registeredList.size();
        }else{
            return lastModels.size();
        }
    }

    View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nd.dismiss();
        }
    };


    class viewHolder extends RecyclerView.ViewHolder {

        TextView modelName;
        TextView priceText;
        ImageView registeredImg;
        Button finish;
        ImageButton onSelling;
        UpdateItemTask updateItemTask;
        public viewHolder(View itemView) {
            super(itemView);
            modelName = (TextView) itemView.findViewById(R.id.modelName);
            priceText = (TextView)itemView.findViewById(R.id.registeredPrice);
            registeredImg = (ImageView)itemView.findViewById(R.id.registeredImg);
            finish  = (Button)itemView.findViewById(R.id.finishSold);
            onSelling =(ImageButton)itemView.findViewById(R.id.buttonOnSelling);

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nd = new NoticeDialogTwoButtons(context,"바이크 거래를 종료하시겠습니까?\n더 이상 이용자들에게 노출되지 않으며,\n'지난 매물'에서 확인 및 재등록하실 수 있습니다."
                    ,leftListener,rightListener);
                    nd.show();

                }

                View.OnClickListener rightListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemUpdatePatch = new ItemUpdatePatch();
                        itemUpdatePatch.setId(registeredList.get(getAdapterPosition()).getId());
                        itemUpdatePatch.setDeal_status("C");
                        Log.e("id",String.valueOf(itemUpdatePatch.getId()));
                        Log.e("setDeal_status",itemUpdatePatch.getDeal_status());

                        updateItemTask = new UpdateItemTask(registeredList.get(getAdapterPosition()).getId());
                        updateItemTask.execute();

                        nd.dismiss();

                    }
                };

            });
        }

    }

    class viewHolder2 extends RecyclerView.ViewHolder {

        TextView modelName;
        TextView priceText;
        ImageView registeredImg;
        UpdateItemTask updateItemTask;
        Button reRegist;
        Button delete;
        ImageButton soldOut;

        public viewHolder2(View itemView) {
            super(itemView);
            modelName = (TextView) itemView.findViewById(R.id.modelName);
            priceText = (TextView)itemView.findViewById(R.id.registeredPrice);
            registeredImg = (ImageView)itemView.findViewById(R.id.registeredImg);
            reRegist = (Button)itemView.findViewById(R.id.reRegist);
            delete = (Button)itemView.findViewById(R.id.delete);
            soldOut = (ImageButton)itemView.findViewById(R.id.buttonOnSold);

            reRegist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nd=new NoticeDialogTwoButtons(context,"바이크를 재등록하시겠습니까? \n '거래중' 상태로 전환되어 다시 이용자들에게 노출되고 연락을 받을 수 있습니다.",leftListener,rightListener);
                    nd.show();
                }

                View.OnClickListener rightListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemUpdatePatch = new ItemUpdatePatch();
                        itemUpdatePatch.setId(lastModels.get(getAdapterPosition()).getId());
                        itemUpdatePatch.setDeal_status("O");

                        updateItemTask = new UpdateItemTask(lastModels.get(getAdapterPosition()).getId());
                        updateItemTask.execute();
                        //notifyDataSetChanged();
                        nd.dismiss();
                    }
                };

            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nd = new NoticeDialogTwoButtons(context,"바이크를 삭제하시겠습니까? \n바이크를 재등록 및 조회할 수 없게됩니다.",leftListener,rightListener);
                    nd.show();


                }
                View.OnClickListener rightListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemUpdatePatch = new ItemUpdatePatch();
                        itemUpdatePatch.setId(lastModels.get(getAdapterPosition()).getId());
                        itemUpdatePatch.setDeal_status("D");

                        updateItemTask = new UpdateItemTask(lastModels.get(getAdapterPosition()).getId());
                        updateItemTask.execute();

                        nd.dismiss();
                    }
                };
            });
        }


    }

    class UpdateItemTask extends AsyncTask<Integer, Integer, Void> {
        String id;
        public UpdateItemTask(int item_id)
        {
            id = String.valueOf(item_id);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            headerMap = new HashMap<>();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            checkUserData();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id", userId);
            headerMap.put("user-uuid",uuid);

            Log.e("access token",String.valueOf(token));
            Log.e("user-id",String.valueOf(userId));
            Log.e("user-uuid",String.valueOf(uuid));

            itemResponse = new ItemResponse();
        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<ItemResponse> patchUpdateItem = restApiInterface.patchUpdateItem(id,headerMap,itemUpdatePatch);
            patchUpdateItem.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    itemResponse=response.body();
                    if(itemResponse==null)Log.e("Response<ItemResponse>","null");
                    Log.e("SERVERCODE", String.valueOf(response.code())+response.message());
                    Log.e("itemStatus", String.valueOf(itemResponse.getDeal_status()));
                    Log.e("진행중","oo");
                   mCallback.onClick();
                }
                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.e("connectfail", t.toString());
                }

            });

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void checkUserData(){

            SharedPreferences userInfo = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String access_token = userInfo.getString("access-token", null);
            if (access_token != null) {
                token = userInfo.getString("access-token", null);
                uuid = userInfo.getString("user-uuid", null);
                userId = userInfo.getString("user-id", null);

            }
        }

    }

}
