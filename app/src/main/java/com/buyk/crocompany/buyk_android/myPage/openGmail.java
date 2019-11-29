package com.buyk.crocompany.buyk_android.myPage;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class openGmail extends Activity {

    public void goToGmail(){
        Intent email = new Intent(Intent.ACTION_SEND);
        String appVersion = null;
        String osVersion = Build.VERSION.RELEASE;
        try{
            PackageInfo i = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            appVersion = i.versionName;
        }catch(PackageManager.NameNotFoundException e){}
        email.setType("plain/text");
        String[]address={"wearecro@gmail.com"};
        email.setPackage("com.google.android.gm");
        email.putExtra(Intent.EXTRA_EMAIL,address);
        email.putExtra(Intent.EXTRA_SUBJECT,"바이크 고객센터에 문의합니다.");
        email.putExtra(Intent.EXTRA_TEXT,"문의 할 내용을 적어주세요.\n\n내용 :\n\n\n\n\n\n\n");
        email.putExtra(Intent.EXTRA_TEXT,"APP version : "+appVersion+"\n");
        email.putExtra(Intent.EXTRA_TEXT,"OS version : "+osVersion);
        startActivity(Intent.createChooser(email,"send Email"));
    }


}
