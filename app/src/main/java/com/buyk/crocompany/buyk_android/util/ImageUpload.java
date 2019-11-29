package com.buyk.crocompany.buyk_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.ImageRegistResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;

public class ImageUpload {

    ArrayList<File> resized_small_image;
    ArrayList<File> resized_large_image;
    int image_size=0;
    Context context;

    MultipartBody.Part[] small_image = new MultipartBody.Part[1];
    MultipartBody.Part[] large_image = new MultipartBody.Part[1];
    RequestBody item_id;
    RequestBody index;
    MultipartBody.Part reqSmallFile;
    MultipartBody.Part reqLargeFile;
    //String item_id;
    //String index;
    ImageRegistResponse imageRegistResponse;
    int itemID;
    Realm localUserDataRealm;
    String uuid;
    String token;
    String userId;
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    HashMap<String,String> headerMap;
    Map<String,RequestBody> requestDataMap;
    public ImageUpload (Context context,ArrayList<File> large, ArrayList<File> small)
    {
        resized_small_image = new ArrayList<>();
        resized_large_image = new ArrayList<>();

        resized_large_image = large;
        resized_small_image= small;
        image_size = resized_large_image.size();
        headerMap = new HashMap<>();
        imageRegistResponse = new ImageRegistResponse();

        Log.e("이미지 사이즈",String.valueOf(image_size));
        this.context = context;
    }

    public void putMultiPartFormData(int itemID) {
        Log.e("itemID", String.valueOf(itemID));


        for (int i = 0; i < image_size; i++) {

            reqSmallFile = MultipartBody.Part.createFormData("small_image", resized_small_image.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), resized_small_image.get(i)));
            reqLargeFile = MultipartBody.Part.createFormData("large_image", resized_large_image.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), resized_large_image.get(i)));
            item_id = RequestBody.create(MediaType.parse("text"),String.valueOf(itemID));
            index = RequestBody.create(MediaType.parse("text"),String.valueOf(i));
            Log.e("item_id",String.valueOf(itemID));
            Log.e("image_size",String.valueOf(image_size));
            //small_image[0] = MultipartBody.Part.createFormData("small_image", resized_small_image.get(i).getName(), reqSmallFile);
            //large_image[0] = MultipartBody.Part.createFormData("large_image", resized_large_image.get(i).getName(), reqLargeFile);

            ImageUploadRequest();
//            ImageUploadTask imageUploadTask = new ImageUploadTask();
//            imageUploadTask.execute();

            //requestDataMap.clear();
        }
    }

    public void ImageUploadRequest()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApiInterface = retrofit.create(RetrofitInterface.class);
        checkUserData();
        headerMap.put("buyk-api-key",buyk_api_key);
        headerMap.put("access-token",token);
        headerMap.put("user-id", String.valueOf(userId));
        headerMap.put("user-uuid",uuid);


        Call<ImageRegistResponse> call = restApiInterface.RegistingImage(headerMap,reqSmallFile,reqLargeFile,item_id,index);
        call.enqueue(new Callback<ImageRegistResponse>() {
            @Override
            public void onResponse(Call<ImageRegistResponse> call, Response<ImageRegistResponse> response) {
                imageRegistResponse = response.body();
                if(imageRegistResponse==null) Log.e("imageRegistResponse","null");

                Log.e("SERVERCODE", String.valueOf(response.code()));
                Log.e("responseMessage",response.message());
                Log.e("get image index",String.valueOf(imageRegistResponse.getIndex()));
            }

            @Override
            public void onFailure(Call<ImageRegistResponse> call, Throwable t) {
                Log.e("connectfail", t.toString());

            }
        });
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






