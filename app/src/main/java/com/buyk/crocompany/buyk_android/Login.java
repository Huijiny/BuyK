package com.buyk.crocompany.buyk_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;

import com.buyk.crocompany.buyk_android.util.SampleSignupActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class Login extends AppCompatActivity {
    SessionCallback callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        setupWindowAnimations();

    }


    public class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.e("세션","성공");
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, SampleSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NewApi")
    private void setupWindowAnimations() {
        Fade fade = null;
            fade = new Fade();
            fade.setDuration(1);
            getWindow().setEnterTransition(fade);
    }

}
