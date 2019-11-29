package com.buyk.crocompany.buyk_android.myPage;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.model.RemoteData.PhoneNumberPatch;
import com.buyk.crocompany.buyk_android.model.RemoteData.PhoneNumberResponse;
import com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;

public class PhoneNumberModifyView extends AppCompatActivity {
    @BindView(R.id.firstEditInModify)
    EditText edit1;
    @BindView(R.id.secondEditInModify)
    EditText edit2;
    @BindView(R.id.finishBtn)
    Button finish;
    String temp1;
    String temp2;
    Boolean finishEdit1=false;
    Boolean finishEdit2=false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;

    PhoneNumberPatch phoneNumberPatch;
    PhoneNumberResponse phoneNumberResponse;
    PhoneNumberTask phoneNumberTask;
    Realm localUserDataRealm;
    String uuid;
    String token;
    String userId;
    RetrofitInterface restApiInterface;
    Retrofit retrofit;
    HashMap<String,String> headerMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_modify_view);
        ButterKnife.bind(this);
        headerMap = new HashMap<>();
        phoneNumberPatch = new PhoneNumberPatch();
        init_edit();


    }

    public void init_edit()
    {


        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4)
                {
                    edit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                temp1 = s.toString();
                if(temp1.length()==4)
                {
                    finishEdit1=true;
                }else{
                    finishEdit1=false;
                }
                if(finishEdit1&&finishEdit2){
                    finish.setEnabled(true);
                }
            }
        });
        edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                {
                    edit1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                temp2 = s.toString();
                if(temp2.length()==4){
                    finishEdit2=true;
                }else{
                    finishEdit2=false;
                }
                if(finishEdit1&&finishEdit2){
                    finish.setEnabled(true);
                }
            }
        });


    }

    @OnClick(R.id.backToMyInfo)
    public void goToMyInfo(View v){
        this.finish();
    }
    @OnClick(R.id.finishBtn)
    public void onClick(View v)
    {
        String phoneNumber="010" ;
        phoneNumber = phoneNumber.concat(edit1.getText().toString());
        phoneNumber = phoneNumber.concat(edit2.getText().toString());
        Log.e("phoneNumber",phoneNumber);
        sharedPreferences = getSharedPreferences("phoneNumber",MODE_PRIVATE);


        editor = sharedPreferences.edit();
        editor.putString("휴대폰번호",phoneNumber);
        editor.commit();
        phoneNumberPatch.setPhone(phoneNumber);
        Log.e("수정후 phoneNumber",phoneNumber);
        phoneNumberTask = new PhoneNumberTask();
        phoneNumberTask.execute();
        this.finish();
    }
    public void checkUserData(){

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token", null);
        if (access_token != null) {
            token = userInfo.getString("access-token", null);
            uuid = userInfo.getString("user-uuid", null);
            userId = userInfo.getString("user-id", null);

        }
    }

    class PhoneNumberTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            phoneNumberResponse = new PhoneNumberResponse();
        }
        @Override
        protected Void doInBackground(Integer... params) {

            Call<PhoneNumberResponse> patchResgistphone = restApiInterface.patchUpdatePhoneNumber(headerMap,phoneNumberPatch);
            patchResgistphone.enqueue(new Callback<PhoneNumberResponse>() {
                @Override
                public void onResponse(Call<PhoneNumberResponse> call, Response<PhoneNumberResponse> response) {
                    phoneNumberResponse = response.body();
                    Log.e("SERVERCODE", String.valueOf(response.code()));
                    Log.e("phoneNumber",String.valueOf(phoneNumberResponse.getPhone()));

                    //403처리
                }

                @Override
                public void onFailure(Call<PhoneNumberResponse> call, Throwable t) {
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
