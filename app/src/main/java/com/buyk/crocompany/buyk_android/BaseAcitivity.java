package com.buyk.crocompany.buyk_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.buyk.crocompany.buyk_android.model.RemoteData.AuthorizeResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.KaKaoAuthorize;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import io.realm.Realm;

public class BaseAcitivity extends AppCompatActivity {

    SessionCallback callback;
    KaKaoAuthorize kaKaoAuthorize;
    AuthorizeResponse authorizeResponse;
    //realm
    Realm localUserDataRealm;
    String user_id;
    String token;
    String uuid;

    String kakaoID;
    String kakao_access_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Session.getCurrentSession().checkAndImplicitOpen()){
            Log.e("카카오세션어플리케이션에서 체크","열려있음 in baseActivity");
            requestMe();

        }
    }
    public class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.e("세션","성공");
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("sessionopenfail","fail");
            if(exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_main); // 세션 연결이 실패했을때
        }

    }
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
//                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
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

                checkUserToken();
            }
        });
    }
    public void checkUserToken(){


        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token",null);
        if(access_token != null){
            token = userInfo.getString("access-token",null);
            uuid = userInfo.getString("user-uuid",null);
            user_id = userInfo.getString("user-id",null);
            changeAccessToken(kakao_access_token);

        }
        else {
        }

    }
    public void changeAccessToken(String kakao_access_token){
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userInfo.edit().putString("access-token",kakao_access_token).commit();

    }


}
