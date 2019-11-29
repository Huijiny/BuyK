package com.buyk.crocompany.buyk_android.util;

/**
 * Created by leeyun on 2018. 5. 12..
 */

/*
  Copyright 2014-2017 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.buyk.crocompany.buyk_android.model.RemoteData.AuthorizeResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.KaKaoAuthorize;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.myPage.myPage;
import com.buyk.crocompany.buyk_android.soldBikeView;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;

/**
 * 유효한 세션이 있다는 검증 후
 * me를 호출하여 가입 여부에 따라 가입 페이지를 그리던지 Main 페이지로 이동 시킨다.
 */
public class SampleSignupActivity extends Activity {
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    Retrofit retrofit;
    RetrofitInterface restApiInterface;
    KaKaoAuthorize kaKaoAuthorize;
    AuthorizeResponse authorizeResponse;
    //realm
    Realm localUserDataRealm;
    String user_id;
    String token;
    String uuid;

    String kakaoID;
    String kakao_access_token;
    Map<String,String> headersMap;
    AuthTask authTask;

    Intent intent;
    Intent goToAcitivityIntent;
    String whereTogoAcitivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kaKaoAuthorize = new KaKaoAuthorize();
        authorizeResponse = new AuthorizeResponse();
        intent = getIntent();
        whereTogoAcitivity = intent.getStringExtra("loginActivity");
        if (whereTogoAcitivity.equalsIgnoreCase("main")) {
            goToAcitivityIntent = new Intent(this,soldBikeView.class);
            requestMe();

        }
        else if(whereTogoAcitivity.equalsIgnoreCase("myPage")){
            goToAcitivityIntent = new Intent(this,myPage.class);
            requestMe();
        }

    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e("로그인실패!!!",message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                Log.e("카톡회원아니다","그렇다고");
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Log.e("로그인성공","그렇다고");
                Log.e("UserProfile", userProfile.toString());
                kakaoID = String.valueOf(userProfile.getId()); // userProfile에서 ID값을 가져옴
                String kakaoNickname = userProfile.getNickname();// Nickname 값을 가져옴
                String kakaoEmail=userProfile.getEmail();
                String kakaoImageUrl = userProfile.getProfileImagePath();
                Log.e("KakaoId", String.valueOf(kakaoID));
                 kakao_access_token = Session.getCurrentSession().getAccessToken();
                Log.e("accessToken",kakao_access_token);
                authTask = new AuthTask();
                authTask.execute();
                savePreferences(kakaoNickname,kakaoEmail,kakaoImageUrl,kakaoID,kakao_access_token);

                goToAcitivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToAcitivityIntent);
                finish();
            }
        });
    }


    protected void redirectLoginActivity() {
        Log.e("카톡세션 닫힘","그렇다고");
        final Intent intent = new Intent(this, myPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    private void savePreferences(String nickName, String email, String profileImage,String kakaoId, String access_token){
        SharedPreferences kakaoInfo = getSharedPreferences("kakaoInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = kakaoInfo.edit();
        editor.putString("nickName",nickName);
        editor.putString("email", email);
        editor.putString("profileImage", profileImage);
        editor.putString("kakaoId", kakaoId);
        editor.putString("access_token", access_token);
        editor.commit();
    }
    public void savePhoneNumber(String phoneNumber){
        SharedPreferences kakaoInfo = getSharedPreferences("kakaoInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = kakaoInfo.edit();
        editor.putString("휴대폰번호",phoneNumber);
        editor.commit();
    }

    class AuthTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
            headersMap= new HashMap<>();
            checkUserData();
            Log.e("token,user_id",token+","+user_id);
            headersMap.put("buyk-api-key","wearecro1@#");
            headersMap.put("access-token",token);
            headersMap.put("user-id",user_id);
            headersMap.put("user-uuid",uuid);
            Log.e("카카오id", String.valueOf(kakaoID));
            Log.e("카카오에세스토큰",kakao_access_token);
            kaKaoAuthorize.setAccess_token(kakao_access_token);
            kaKaoAuthorize.setExternal_id(kakaoID);
            kaKaoAuthorize.setLogin_type("K");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<AuthorizeResponse> putKakaoAuth = restApiInterface.putKakaoAuth(headersMap,kaKaoAuthorize);
            putKakaoAuth.enqueue(new Callback<AuthorizeResponse>() {
                @Override
                public void onResponse(Call<AuthorizeResponse> call, Response<AuthorizeResponse> response) {
                    response.code();
                    authorizeResponse = response.body();
                    Log.e("authResponse", String.valueOf(response.code()));
//                    Log.e("authResponse",authorizeResponse.getExternal_id()+authorizeResponse.getLogin_type());
                    if(!ObjectUtils.isEmpty(authorizeResponse)) {
                        changeAccessToken(kakao_access_token,authorizeResponse.getId());
                        savePhoneNumber(authorizeResponse.getPhone());
                    }
                }
                @Override
                public void onFailure(Call<AuthorizeResponse> call, Throwable t) {
                    Log.d("connectfail", t.toString());
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
    public void checkUserData(){


        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token",null);
        if(access_token != null){
            token = userInfo.getString("access-token",null);
            uuid = userInfo.getString("user-uuid",null);
            user_id = userInfo.getString("user-id",null);

        }
        else {
            Log.e("userdata","Null");
        }
    }
    public void changeAccessToken(String kakao_access_token,int user_id){
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userInfo.edit().putString("access-token",kakao_access_token).commit();
        userInfo.edit().putString("user-id",String.valueOf(user_id)).commit();
    }
}
