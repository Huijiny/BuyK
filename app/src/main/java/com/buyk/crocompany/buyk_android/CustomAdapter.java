package com.buyk.crocompany.buyk_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import com.buyk.crocompany.buyk_android.util.repairHistory;
import com.buyk.crocompany.buyk_android.util.tuningHistory;

import java.util.ArrayList;

public class CustomAdapter extends FragmentStatePagerAdapter {

   // private final int SearchBike = 0;

    ArrayList<android.support.v4.app.Fragment> fragment = new ArrayList<android.support.v4.app.Fragment>(0);
    Context context;
    String phoneNumber=null;
    public CustomAdapter(FragmentManager fm,Context context) {

        super(fm);
        this.context = context;

        android.support.v4.app.Fragment noPhonefragments[] = {InputPhoneNumber.newInstance(0), SearchBike.newInstance(1),BikeImages.newInstance(2),
                choosingPayment.newInstance(3),bikeBasicInfo.newInstance(4)/*, repairHistory.newInstance(5),tuningHistory.newInstance(6), bikeDetailInfo.newInstance(7)*/};

        android.support.v4.app.Fragment existPhonefragments[]={SearchBike.newInstance(0),BikeImages.newInstance(1),
                choosingPayment.newInstance(2),bikeBasicInfo.newInstance(3)/*, repairHistory.newInstance(4),tuningHistory.newInstance(5), bikeDetailInfo.newInstance(6)*/};

        SharedPreferences sharedPreferences = context.getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);

        try {
            phoneNumber = sharedPreferences.getString("휴대폰번호", "");
            Log.i("폰번호", phoneNumber);

            if (phoneNumber.length() == 11) {
                for (int i = 0; i < existPhonefragments.length; i++) {
                    fragment.add(existPhonefragments[i]);
                }
            } else {
                for (int i = 0; i < noPhonefragments.length; i++) {
                    fragment.add(noPhonefragments[i]);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Log.e("fragment get",String.valueOf(fragment.get(position)));
        return fragment.get(position);

    }
    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    public void addUsedCarView(int position)
    {
        Log.e("현재 뷰페이저 position/addUsedCarView",String.valueOf(position));
        fragment.add(position+1, repairHistory.newInstance(position+1));
        fragment.add(position+2,tuningHistory.newInstance(position+2));
        fragment.add(position+3,bikeDetailInfo.newInstance(position+3));
        fragment.size();
        notifyDataSetChanged();
        for(int i=0;i<fragment.size();i++){
            Log.e("fragment",String.valueOf(fragment.get(i)));
        }
    }

    public void addNewCarView(int position)
    {
        Log.e("현재 뷰페이저 position/addNewCarView",String.valueOf(position));
        fragment.add(position+1,PromotionEventHistory.newInstance(position+1));
        fragment.add(position+2,bikeDetailInfo.newInstance(position+2));
        notifyDataSetChanged();

        for(int i=0;i<fragment.size();i++){
            Log.e("fragment",String.valueOf(fragment.get(i)));
        }
    }

    public void removeCarView(int position)
    {
        Log.e("프레그먼트 size",String.valueOf(fragment.size()));
        Log.e("현재 포지션",String.valueOf(position));
        int i;
        int turn = fragment.size()-position;
        if(turn>1) {
            Log.e("turn",String.valueOf(turn));
            for (i = 1; i < turn; i++) {
                Log.e("현재 뷰페이저 position/removeCarView", String.valueOf(i));
                fragment.remove(position + 1);
                notifyDataSetChanged();
            }
        }
    }

    public void removePhoneView(int position)
    {
        fragment.remove(position);
    }
    public void addPhoneView(){
        fragment.add(0,InputPhoneNumber.newInstance(0));
    }


    @Override
    public int getCount() {
        return fragment.size();
    }

    @Override

    public void destroyItem(View container, int position, Object object) {

        // TODO Auto-generated method stub

        // super.destroyItem(container, position, object);

    }


}


