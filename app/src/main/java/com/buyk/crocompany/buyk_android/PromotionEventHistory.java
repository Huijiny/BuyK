package com.buyk.crocompany.buyk_android;

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

import com.buyk.crocompany.buyk_android.util.NoticeDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromotionEventHistory extends Fragment {
    @BindView(R.id.noPromotion)
    RadioButton negBtn;
    @BindView(R.id.yesPromotion)
    RadioButton posBtn;
    @BindView(R.id.promotion_event)
    EditText promotionEvent;
    @BindView(R.id.promotionYesOrNoLayout)
    LinearLayout promotionLayout;
    @BindView(R.id.promotion_event_history)
    LinearLayout promotionLinearLayout;
    InputMethodManager imm;

    public static PromotionEventHistory newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        PromotionEventHistory fragment = new PromotionEventHistory();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onResume() {

        super.onResume();
        imm =(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        ((soldBikeView)getActivity()).itemPost.setRepair_history(null);
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_promotion_or_event_view, container, false);
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
        String repairContent =promotionEvent.getText().toString();
        if(posOrneg==false)
        {
            ((soldBikeView)getActivity()).setDisabled();
        }else {
            if (pos) {
                promotionLayout.setVisibility(View.VISIBLE);

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
        promotionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(promotionLinearLayout.getWindowToken(),0);
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
        promotionEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (promotionEvent.getText().toString().length() > 0 ) {
                    ((soldBikeView) getActivity()).itemPost.setTuning_history(promotionEvent.getText().toString());
                }
                if(promotionEvent.getText().toString().length()>1024)
                {
                    init_dialog("입력글자는 1024자를 초과 할 수 없습니다.",promotionEvent);
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
                case R.id.yesPromotion:
                    Log.i("네", "ㅇㅇ");
                    posBtn.setChecked(true);
                    negBtn.setChecked(false);
                    promotionEvent.setText("");
                    promotionLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.noPromotion:
                    Log.i("아니오", "ㅇㅇ");
                    posBtn.setChecked(false);
                    negBtn.setChecked(true);
                    promotionLayout.setVisibility(View.INVISIBLE);
                    ((soldBikeView)getActivity()).itemPost.setTuning_history(null);
                    break;

            }
            checkEnable();
        }
    };
}
