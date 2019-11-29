package com.buyk.crocompany.buyk_android.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ahdguialee on 2018. 8. 13..
 */

public class NoticeDialog extends Dialog {
    @BindView(R.id.content)
    TextView contentTextView;
    public NoticeDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_component);
        ButterKnife.bind(this);
    }

    public NoticeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected NoticeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public void setContent(String content){
        contentTextView.setText(content);
    }
    @OnClick(R.id.dismissText)
    public void onClickDismissText(){
        dismiss();
    }
}
