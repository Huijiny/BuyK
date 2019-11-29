package com.buyk.crocompany.buyk_android.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

    public class NoticeDialogTwoButtons  extends Dialog {


        private TextView mContentView;
        private TextView mLeftButton;
        private TextView mRightButton;
        private String mContent;
        private String leftText;
        private String rightText;

        private View.OnClickListener mLeftClickListener;
        private View.OnClickListener mRightClickListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // 다이얼로그 외부 화면 흐리게 표현
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.diaolg_component_two_buttons);


            mContentView = (TextView) findViewById(R.id.textContent);
            mLeftButton = findViewById(R.id.cancel);
            mRightButton =  findViewById(R.id.Ok);

            if(leftText!=null&&rightText!=null){
                mLeftButton.setText(leftText);
                mRightButton.setText(rightText);
                mLeftButton.setTextSize(20);
                mRightButton.setTextSize(20);
            }

            // 제목과 내용을 생성자에서 셋팅한다.

            mContentView.setText(mContent);

            // 클릭 이벤트 셋팅
            if (mLeftClickListener != null && mRightClickListener != null) {
                mLeftButton.setOnClickListener(mLeftClickListener);
                mRightButton.setOnClickListener(mRightClickListener);
            } else if (mLeftClickListener != null
                    && mRightClickListener == null) {
                mLeftButton.setOnClickListener(mLeftClickListener);
            } else {

            }
        }

        // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
        public NoticeDialogTwoButtons (Context context, String title,
                            View.OnClickListener singleListener) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);

            this.mLeftClickListener = singleListener;
        }

        // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
        public NoticeDialogTwoButtons (Context context,
                            String content, View.OnClickListener leftListener,
                            View.OnClickListener rightListener) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);

            this.mContent = content;
            this.mLeftClickListener = leftListener;
            this.mRightClickListener = rightListener;
        }
        public NoticeDialogTwoButtons(Context context,String content, View.OnClickListener leftListener, View.OnClickListener rightListener, String leftText, String rightText)
        {
            super(context,android.R.style.Theme_Translucent_NoTitleBar);
            this.mContent = content;
            this.mLeftClickListener = leftListener;
            this.mRightClickListener = rightListener;
            Log.e("뭐가 널이지?",String.valueOf(this.mLeftButton));
            this.leftText = leftText;
            this.rightText = rightText;

        }
    }



