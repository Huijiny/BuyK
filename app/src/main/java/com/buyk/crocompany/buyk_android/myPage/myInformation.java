package com.buyk.crocompany.buyk_android.myPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.MainActivity;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinPost;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;
import com.buyk.crocompany.buyk_android.util.NoticeDialogTwoButtons;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;

public class myInformation extends Activity {
    Intent information;
    String name;
    String email;
    String phoneNumber;
    TextView kakaoNameInMyInfo;
    TextView kakaoEmailInMyInfo;
    TextView phoneNumberMyInfo;
    JoinResponse joinResponse;
    JoinPost joinPost;
    TextView joinOut;
    String uuid;
    ImageButton goTomodifyPhoneNum;
    NoticeDialogTwoButtons nd;
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    Realm localUserDataRealm;
    LocalUserData localUserData;
    JoinTask joinTask;
    LinearLayout emailView;
    LinearLayout phoneNumberView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        information = getIntent();
        ButterKnife.bind(this);
        kakaoNameInMyInfo = this.findViewById(R.id.myInfoName1);
        kakaoEmailInMyInfo = this.findViewById(R.id.myInfoEmail1);
        phoneNumberMyInfo = this.findViewById(R.id.myInfoPhoneNumber);
        joinOut = this.findViewById(R.id.joinOut);
        goTomodifyPhoneNum = this.findViewById(R.id.goToModifyPhoneNumberView);
        goTomodifyPhoneNum.setRotation(180);
        emailView = this.findViewById(R.id.emailViewGroup);
        phoneNumberView = this.findViewById(R.id.phoneNumberViewGroup);

        joinOutButton();

    }


    @Override
    public void onResume() {
        super.onResume();
        setMyInfor();
    }

    public void joinOutButton()
    {
        String data = "탈퇴하기";
        SpannableString content = new SpannableString(data);
        content.setSpan(new UnderlineSpan(),0,data.length(),0);
        joinOut.setText(content);


        joinOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("탈퇴하기", "click");
                nd = new NoticeDialogTwoButtons(myInformation.this, "탈퇴하실 경우, 'BuyK' 서비스 이용에 제한이 있을 수 있습니다.\n탈퇴하시겠습니까?", leftListener, rightListener, "취소", "탈퇴하기");
                nd.show();
            }

        });
    }
    public void setMyInfor() {
        SharedPreferences sharedPreferences = getSharedPreferences("kakaoInfo", MODE_PRIVATE);
        SharedPreferences phoneNum = getSharedPreferences("phoneNumber",MODE_PRIVATE);

        name = sharedPreferences.getString("nickName", "");
        email = sharedPreferences.getString("email", "");
        phoneNumber = phoneNum.getString("휴대폰번호", "");

        Log.i("이름", name);
        Log.i("이메일", email);
        Log.i("폰번호", phoneNumber);

        if(email.equals(""))emailView.setVisibility(View.GONE);
        if(phoneNumber.equals("")||phoneNumber.equals("010"))phoneNumberView.setVisibility(View.GONE);

        kakaoNameInMyInfo.setText(name);
        kakaoEmailInMyInfo.setText(email);
        phoneNumberMyInfo.setText(phoneNumber);
    }

    View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nd.dismiss();
        }
    };

    View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    Log.e("탈퇴","그렇다고");
                    joinTask = new JoinTask();
                    joinTask.execute();

                    Intent intent = new Intent();
                    intent.putExtra("Logout","true");

                    setResult(1,intent);
                    finish();
                }
            });
        }
    };

    @OnClick(R.id.backToMyPage)
    public void goToMyPage(View v){
       this. finish();
    }

    @OnClick(R.id.goToModifyPhoneNumberView)
    public void goToModifyPhonePage(View v){
        Intent gotoModifyPhoneNumView= new Intent(myInformation.this, PhoneNumberModifyView.class);
        startActivity(gotoModifyPhoneNumView);
    }


    @OnClick(R.id.logoutBtn)
    public void OnClickLogoutBtn(){
        Log.e("OnClickLogoutBtn","click");
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    Log.e("카톡 로그아웃","그렇다고");
                    getADIdThread.execute();
                    Intent intent = new Intent();
                    intent.putExtra("Logout","true");
                    setResult(1,intent);
                    finish();
                }
            });
    }


    class JoinTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            joinPost = new JoinPost();
            joinPost.setUuid(uuid);
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiInterface = retrofit.create(RetrofitInterface.class);
        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<JoinResponse> postJoin = restApiInterface.postJoin(joinPost);
            postJoin.enqueue(new Callback<JoinResponse>() {
                @Override
                public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                    joinResponse=response.body();
                    Log.e("joinResponse", String.valueOf(response.code()));
                    localUserDataInput(joinResponse);
                    Log.e("joinResponse", String.valueOf(joinResponse.getId()));
                    Log.e("joinResponse", String.valueOf(joinResponse.getToken()));
                }
                @Override
                public void onFailure(Call<JoinResponse> call, Throwable t) {
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
            removeAllPreferences();
        }

    }
    public void localUserDataInput(JoinResponse joinResponse){
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("access-token",joinResponse.getToken());
        editor.putString("user-uuid",uuid);
        editor.putString("user-id",String.valueOf(joinResponse.getId()));
        editor.putString("access-token",joinResponse.getToken());
        editor.putString("deadline",joinResponse.getDeadline());
        editor.commit();

    }
    private void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("kakaoInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfo.edit();
        editor.clear();
        userInfoEditor.clear();
        editor.commit();
        userInfoEditor.commit();
    }


    AsyncTask<Void, Void, String> getADIdThread = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String advertId = null;
            try{
                advertId = idInfo.getId();
                Log.e(advertId,advertId);
            }catch (Exception e){
                e.printStackTrace();
            }
            return advertId;
        }
        @Override
        protected void onPostExecute(String advertId) {
            uuid = advertId;
            JoinTask joinTask = new JoinTask();
            joinTask.execute();
        }
    };

}
