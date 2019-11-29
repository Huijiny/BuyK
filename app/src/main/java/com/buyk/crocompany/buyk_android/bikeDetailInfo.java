package com.buyk.crocompany.buyk_android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class bikeDetailInfo extends Fragment {
   @BindView(R.id.detailEdit)
   EditText detailEdit;
   @BindView(R.id.detailInfo)
   LinearLayout detailCL;
    InputMethodManager imm;
    public bikeDetailInfo(){

    }

    public static bikeDetailInfo newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        bikeDetailInfo fragment = new bikeDetailInfo();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {

        super.onResume();
        imm =(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
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
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bike_detail_info, container, false);
        ButterKnife.bind(this,rootView);
        init_edit();
        hideKeyboard();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            checkEnable();
        }
    }

    private void checkEnable()
    {
        String detailContent = detailEdit.getText().toString();

        if(detailContent.length()>30)
        {
            ((soldBikeView)getActivity()).setEnabled();
        }else{
            ((soldBikeView)getActivity()).setDisabled();
        }
    }
    public void hideKeyboard()
    {
        detailCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(detailCL.getWindowToken(),0);
            }
        });
    }

    public void init_edit()
    {
        detailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ((soldBikeView)getActivity()).itemPost.setDetail_information(detailEdit.getText().toString());
                checkEnable();
            }
        });
    }

}
