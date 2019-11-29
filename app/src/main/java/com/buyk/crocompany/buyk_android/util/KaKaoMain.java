package com.buyk.crocompany.buyk_android.util;

import android.app.Activity;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.igaworks.IgawCommon;
import com.kakao.auth.KakaoSDK;

import io.fabric.sdk.android.Fabric;

/**
 * Created by leeyun on 2018. 5. 12..
 */

public class KaKaoMain extends Application{
        private static volatile KaKaoMain instance = null;
        private static volatile Activity currentActivity = null;

        @Override
        public void onCreate() {
            super.onCreate();
            Fabric.with(this, new Crashlytics());
            instance = this;
            IgawCommon.autoSessionTracking(KaKaoMain.this);
            KakaoSDK.init(new KakaoSDKAdapter());
        }

        public static Activity getCurrentActivity() {
            return currentActivity;
        }

        public static void setCurrentActivity(Activity currentActivity) {
            KaKaoMain.currentActivity = currentActivity;
        }

        /**
         * singleton 애플리케이션 객체를 얻는다.
         * @return singleton 애플리케이션 객체
         */
        public static KaKaoMain getGlobalApplicationContext() {
            if(instance == null)
                throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
            return instance;
        }

        /**
         * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
         */
        @Override
        public void onTerminate() {
            super.onTerminate();
            instance = null;
        }
}
