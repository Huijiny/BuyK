package com.buyk.crocompany.buyk_android.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buyk.crocompany.buyk_android.R;

public class CustomToast extends Toast {
    Context mContext;
    public CustomToast(Context context) {
        super(context); mContext = context;
    }
    public void showToast(String body, int duration){
         LayoutInflater inflater;
         View v;
         if(false) {
             Activity act = (Activity) mContext;
             inflater = act.getLayoutInflater();
             v = inflater.inflate(R.layout.toast_design, null);
         }
    else {
         inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.toast_design, null);
    }
    TextView text = (TextView) v.findViewById(R.id.toast_content);
    text.setText(body);
    show(this,v, duration);
}
private void show(Toast toast, View v, int duration){
    toast.setGravity(Gravity.TOP, 0, 0);
    toast.setDuration(duration); toast.setView(v); toast.show();
}
}

