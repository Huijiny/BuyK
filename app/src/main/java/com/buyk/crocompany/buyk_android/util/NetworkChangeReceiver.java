package com.buyk.crocompany.buyk_android.util;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.igaworks.IgawReceiver;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private Activity activity;

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        IgawReceiver igawReceiver = new IgawReceiver();
        igawReceiver.onReceive(context, intent);
        CustomToast toast = new CustomToast(context);
        if(!TextUtils.isEmpty(status)){
            toast.showToast(status, Toast.LENGTH_SHORT);
        }
    }
}



