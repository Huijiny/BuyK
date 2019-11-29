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


public class repairHistory extends Fragment {


    @BindView(R.id.noRepair)
    RadioButton negBtn;
    @BindView(R.id.yesRepair)
    RadioButton posBtn;
    @BindView(R.id.repairYesOrNoLayout)
    LinearLayout repairLayout;
    @BindView(R.id.repair)
    EditText repair;
    @BindView(R.id.repairHistory)
    LinearLayout repairLinearLayout;
    InputMethodManager imm;


    public static repairHistory newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        repairHistory fragment = new repairHistory();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {

        super.onResume();
        imm =(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            checkEnable();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_repair_history, container, false);
        ButterKnife.bind(this, rootView);

        posBtn.setOnClickListener(myListener);
        negBtn.setOnClickListener(myListener);
        init_edit();
        hideKeyboard();
        return rootView;
    }

    private void checkEnable()
    {
        boolean pos = posBtn.isChecked();
        boolean posOrneg = posBtn.isChecked()||negBtn.isChecked();
        String repairContent =repair.getText().toString();
        if(posOrneg==false)
        {
            ((soldBikeView)getActivity()).setDisabled();
        }else {
            if (pos) {
                repairLayout.setVisibility(View.VISIBLE);

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
       repairLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(repairLinearLayout.getWindowToken(),0);
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
        repair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (repair.getText().toString().length() > 0){
                    ((soldBikeView)getActivity()).itemPost.setRepair_history(repair.getText().toString());
                }
                if(repair.getText().toString().length()>1024)
                {
                    init_dialog("입력글자는 1024자를 초과 할 수 없습니다.",repair);
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
                    case R.id.yesRepair:
                        Log.i("네", "ㅇㅇ");
                        posBtn.setChecked(true);
                        negBtn.setChecked(false);
                        repair.setText("");
                        repairLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.noRepair:
                        Log.i("아니오", "ㅇㅇ");
                        posBtn.setChecked(false);
                        negBtn.setChecked(true);
                        repairLayout.setVisibility(View.INVISIBLE);
                        break;

                }
                checkEnable();
            }
        };



}