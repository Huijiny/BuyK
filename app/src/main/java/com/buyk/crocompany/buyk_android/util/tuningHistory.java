package com.buyk.crocompany.buyk_android.util;


import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.soldBikeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class tuningHistory extends Fragment {


    @BindView(R.id.yesTuning)
    RadioButton posBtn;
    @BindView(R.id.noTuning)
    RadioButton negBtn;
    @BindView(R.id.tuningYesOrNoLayout)
    LinearLayout tuningLayout;
    @BindView(R.id.tuning)
    EditText tuning;
    @BindView(R.id.TuningHistory)
    LinearLayout tuningHistoryLayout;
    InputMethodManager imm;

    public static  tuningHistory newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        tuningHistory fragment = new  tuningHistory();
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

        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tuning_history, container, false);
        ButterKnife.bind(this, rootView);
        posBtn.setOnClickListener(myListener);
        negBtn.setOnClickListener(myListener);
        init_edit();
        hideKeyboard();
        return rootView;
    }
    @Override
    public void onStop() {

        super.onStop();

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
        boolean pos = posBtn.isChecked();
        boolean posOrneg = posBtn.isChecked()||negBtn.isChecked();
        String repairContent =tuning.getText().toString();
        if(posOrneg==false)
        {
            ((soldBikeView)getActivity()).setDisabled();
        }else {
            if (pos) {
                tuningLayout.setVisibility(View.VISIBLE);

                if (repairContent.length() > 0) {
                    ((soldBikeView) getActivity()).setEnabled();
                } else {
                    ((soldBikeView) getActivity()).setDisabled();
                }
            } else {
                ((soldBikeView) getActivity()).setEnabled();
            }
        }
    }

    public void hideKeyboard()
    {
        tuningHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(tuningHistoryLayout.getWindowToken(),0);
            }
        });
    }

    public void init_dialog(String content,EditText editText)
    {
        NoticeDialog noticeDialog = new NoticeDialog(getContext());

        noticeDialog.setContent(content);
        noticeDialog.show();
        noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String text = editText.getText().toString();
                text = text.substring(0,1023);
                editText.setText(text);
            }
        });
    }


    public void init_edit() {
        tuning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tuning.getText().toString().length() > 0) {
                    ((soldBikeView)getActivity()).itemPost.setTuning_history(tuning.getText().toString());
                }
                if(tuning.getText().toString().length()>1024)
                {
                    init_dialog("입력글자는 1024자를 초과 할 수 없습니다.",tuning);
                }
                checkEnable();
            }
        });
    }
    View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i("사고수리이력 탭 클릭", "ㅇㅇ");
            switch (v.getId()) {
                case R.id.yesTuning:
                    Log.i("네", "ㅇㅇ");
                    posBtn.setChecked(true);
                    negBtn.setChecked(false);
                    tuning.setText("");
                    tuningLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.noTuning:
                    Log.i("아니오", "tuning ㅇㅇ");
                    posBtn.setChecked(false);
                    negBtn.setChecked(true);
                    tuningLayout.setVisibility(View.INVISIBLE);
                    break;

            }
            checkEnable();
        }
    };


}
