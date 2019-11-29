package com.buyk.crocompany.buyk_android.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.buyk.crocompany.buyk_android.Event.CarClassificationEvent;
import com.buyk.crocompany.buyk_android.Event.LikeEvent;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinPost;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.LikeModel;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.CustomToast;
import com.buyk.crocompany.buyk_android.util.RecyclerView.ZeemBikeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;

/**
 * Created by ahdguialee on 2018. 8. 16..
 */

public class RequestMethod {
    Retrofit retrofit;
    RetrofitInterface restApiInterface;
    HashMap<String,String> headerMap;
    LikeModel likeModel;
    LikeModel likeCancelModel;
    PostLike postLike;
    PostLikeCancel postLikeCancel;
    List<ResponseModelList>itemResponseList;
    Context context;
    ZeemBikeAdapter zeemBikeAdapter;
    JoinResponse joinResponse;
    public void initLikeModelRequest(HashMap headerMap, LikeModel likeModel){
        this.headerMap = new HashMap<>();
        this.headerMap = headerMap;
        this.likeModel = new LikeModel();
        this.likeModel = likeModel;
        postLike  = new PostLike();
        postLike.execute();
        Log.e("likeModel", String.valueOf(likeModel.getItem()));
    }

    public void initLikeCancelRequest(HashMap headerMap, LikeModel likeModel){
        this.headerMap = new HashMap<>();
        this.headerMap = headerMap;
        this.likeCancelModel = new LikeModel();
        this.likeCancelModel = likeModel;
        postLikeCancel = new PostLikeCancel();
        postLikeCancel.execute();
        Log.e("likeModel", String.valueOf(likeModel.getItem()));
    }
    class PostLike extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            Log.e("headerMap",headerMap.get("access-token"));
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<LikeModel> postLike = restApiInterface.postLike(headerMap,likeModel);
            postLike.enqueue(new Callback<LikeModel>() {
                @Override
                public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                    likeModel = response.body();

                    if(response.isSuccessful()) {
                        EventBus.getDefault().post(new LikeEvent(true));
                        Log.e("like","like");
                    }
                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));


                }
                @Override
                public void onFailure(Call<LikeModel> call, Throwable t) {
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
    }
    class PostLikeCancel extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            Log.e("headerMap",headerMap.get("access-token"));
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<LikeModel> postLikeCancel = restApiInterface.postLikeCancel(headerMap,likeCancelModel);
            postLikeCancel.enqueue(new Callback<LikeModel>() {
                @Override
                public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                    likeModel = response.body();
                    if(response.isSuccessful()) {
                        EventBus.getDefault().post(new LikeEvent(true));
                    }
                    Log.e("SERVERCODE", String.valueOf(response.code()+response.message()));

                }
                @Override
                public void onFailure(Call<LikeModel> call, Throwable t) {
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


    }

}
