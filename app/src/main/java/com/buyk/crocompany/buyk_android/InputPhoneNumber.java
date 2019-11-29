package com.buyk.crocompany.buyk_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.buyk.crocompany.buyk_android.model.RemoteData.ItemPost;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InputPhoneNumber extends Fragment {

    @BindView(R.id.firstEdit)
    EditText edit1;
    @BindView(R.id.secondEdit)
    EditText edit2;
    ItemPost itemPost ;
    ViewGroup rootView;
    String phoneNumber;
    String temp1;
    String temp2;
    Boolean finishEdit1=false;
    Boolean finishEdit2=false;
    InputMethodManager imm;
    SharedPreferences sharedPreferences;
    public static InputPhoneNumber newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        InputPhoneNumber fragment = new InputPhoneNumber();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_input_phone_number, container, false);
        ButterKnife.bind(this,rootView);
        init_edit();
        edit1.requestFocus();
        imm =(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(rootView.getWindowToken(),0);
            }
        });
        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        String phoneNumber = "010";
        phoneNumber = phoneNumber.concat(edit1.getText().toString());
        phoneNumber = phoneNumber.concat(edit2.getText().toString());
        Log.e("phoneNumber", phoneNumber);

        if (phoneNumber.length() == 11) {
            ((soldBikeView) getActivity()).phoneNumberPatch.setPhone(phoneNumber);

            /*
            SharedPreferences phoneInformation = getContext().getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = phoneInformation.edit();
            editor.putString("휴대폰번호", phoneNumber);
            editor.commit();
            */
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(((soldBikeView)getActivity())!=null) {//두번째 방문
            if (!(finishEdit1 && finishEdit2)) {
                ((soldBikeView) getActivity()).setDisabled();
            }else{
                ((soldBikeView)getActivity()).setEnabled();
            }
            edit2.requestFocus();
        }
    }
    public void init_edit()
    {
        ((soldBikeView)getActivity()).setDisabled();
        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

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

                checkEnabled();
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
                checkEnabled();
            }
        });
    }

    private void checkEnabled()
    {
        if(finishEdit1&&finishEdit2){
            ((soldBikeView)getActivity()).setEnabled();
        }else{
            ((soldBikeView)getActivity()).setDisabled();
        }
    }




}
