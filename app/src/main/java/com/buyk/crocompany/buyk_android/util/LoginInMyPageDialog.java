package com.buyk.crocompany.buyk_android.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.AgreeWebViewActivity;
import com.buyk.crocompany.buyk_android.Event.LoginEvent;
import com.buyk.crocompany.buyk_android.PrivateInformationUsingAgreeWebView;
import com.buyk.crocompany.buyk_android.R;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

/**
 * Created by ahdguialee on 2018. 8. 13..
 */

public class LoginInMyPageDialog extends Activity {
    SessionCallback callback;
    Button loginBtn;
    ImageButton closeBtn;
    com.kakao.usermgmt.LoginButton kakaoBtn;
    TextView link;
    TextView link2;
    public String loginActivity = "myPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login_component);
        this.setFinishOnTouchOutside(true);

        initView();

        EventBus.getDefault().register(this);
        Session.getCurrentSession().addCallback(callback);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
    public void initView()

    {
        callback = new SessionCallback();
        link= findViewById(R.id.link);
        link2 = findViewById(R.id.link2);
        String text = "로그인 버튼을 누르면 이용약관 및";
        link.setText(text);
        String text2 = " 개인정보취급방침에 동의하신 것이 됩니다.";
        link2.setText(text2);
        setTermOfUseLink(text);
        setPrivateLink(text2);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        kakaoBtn = findViewById(R.id.kakaoLogin);
        closeBtn = findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoBtn.performClick();
            }
        });

    }

    public void setTermOfUseLink(String text)
    {  int start = text.indexOf("이용약관");
        int end = start+4;

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent goToAgreeRule = new Intent(LoginInMyPageDialog.this, AgreeWebViewActivity.class);
                startActivity(goToAgreeRule);
                overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
            }
        };

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(clickableSpan,start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        link.setText(spannableString);

        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }
    public void setPrivateLink(String text)
    {
        int start = text.indexOf("개인정보취급방침");
        int end = start + 8;

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent goToAgreeRule = new Intent(LoginInMyPageDialog.this, PrivateInformationUsingAgreeWebView.class);
                startActivity(goToAgreeRule);
                overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
            }
        };

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(clickableSpan,start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        link2.setText(spannableString);

        link2.setClickable(true);
        link2.setMovementMethod(LinkMovementMethod.getInstance());

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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        Log.e("어디서 왔냐",event.loginActivityName);
        if (event.loginActivityName.equals("main")) {
            loginActivity = event.loginActivityName;
        }
        else {
            loginActivity = "myPage";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        Intent intent = new Intent(this, SampleSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.e("loginActivity",loginActivity);
        intent.putExtra("loginActivity",loginActivity);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.closeBtn)
    public void onClickDismissBtn(){
        onBackPressed();
    }
}
