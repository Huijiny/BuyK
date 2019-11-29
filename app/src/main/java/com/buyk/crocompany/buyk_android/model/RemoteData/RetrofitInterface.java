package com.buyk.crocompany.buyk_android.model.RemoteData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public interface RetrofitInterface {

    public static String API_URL="http://115.68.226.54:8080/";
    public static String buyk_api_key = "wearecro1@#";
    @Headers("buyk-api-key: wearecro1@#")
    @GET("search/{word}")
    Call<List<SearchModel>> getSearchModel(@Path("word")String word);

    @Headers("buyk-api-key: wearecro1@#")
    @POST("account/join/")
    Call<JoinResponse>postJoin(@Body JoinPost joinPost);

    @PUT("account/authorize/")
    Call<AuthorizeResponse>putKakaoAuth(@HeaderMap Map<String,String>map, @Body KaKaoAuthorize kaKaoAuthorize);
    
    @POST("item/register/")
    Call<ItemResponse>postRegisterItem(@HeaderMap Map<String,String>map,@Body ItemPost itemPost);

    @PATCH("account/update-phone/")
    Call<PhoneNumberResponse>patchUpdatePhoneNumber(@HeaderMap Map<String,String>map,@Body PhoneNumberPatch phoneNumberPatch);

    @GET("item/list/filtered/")
    Call<List<ResponseModelList>>getModelList(@HeaderMap Map<String,String>map, @Query("model__in") List<String>model, @QueryMap(encoded=true) Map<String,String> queryMap);

    @POST("item/like/")
    Call<LikeModel>postLike(@HeaderMap Map<String,String>map, @Body LikeModel likeModel);

    @HTTP(method = "DELETE", path = "/item/like/cancel/", hasBody = true)
    Call<LikeModel>postLikeCancel(@HeaderMap Map<String,String>map, @Body LikeModel likeModel);

    @GET("item/list/liked/")
    Call<List<ResponseModelList>>getLikeItemList(@HeaderMap Map<String,String>map);

    @GET("item/list/registered/")
    Call<List<ResponseModelList>>getRegisteredList(@HeaderMap Map<String,String>map);

    @Multipart
    @POST("item/image/register/")
    Call<ImageRegistResponse> RegistingImage(
            @HeaderMap Map<String,String>map,
            @Part MultipartBody.Part small_image,
            @Part MultipartBody.Part large_image,
            @Part("item") RequestBody id,
            @Part("index") RequestBody index
    );

    @PATCH("item/{path}/update/")
    Call<ItemResponse>patchUpdateItem(@Path(value="path",encoded = true)String path,@HeaderMap Map<String,String>map,@Body ItemUpdatePatch itemUpdatePatch);

            /*
            @Part("small_image\"; filename=\"small_image.jpg\" ") RequestBody  small_image,
            @Part ("large_image\"; filename=\"large_image.jpg\" ") RequestBody large_image,
            @Part("item_id") RequestBody item_id,
            @Part("index") RequestBody index);
*/

}
