package com.buyk.crocompany.buyk_android.myPage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.Event.LoginEvent;
import com.buyk.crocompany.buyk_android.MyRegisteredBike;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.util.LoginDialog;
import com.buyk.crocompany.buyk_android.util.LoginInMyPageDialog;
import com.kakao.plusfriend.PlusFriendService;
import com.kakao.util.exception.KakaoException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class recycleViewAdapter extends RecyclerView.Adapter<viewHolder>{
    public Context context;
    public View view;


    LoginDialog loginDialog;
    ArrayList<listName>listNames = new ArrayList<>(0);
    public recycleViewAdapter(Context context, ArrayList<listName> listNames){
        this.context = context;
        this.listNames=listNames;

    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.list_name_ui,parent,false);
        context = parent.getContext();
        return new viewHolder(v,this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        listName name = listNames.get(position);
        holder.mlistName.setText(name.getListName());
        if(holder.mlistName.getText().equals("문의하기")){
            holder.request.setVisibility(View.VISIBLE);
            holder.mail.setText("메일");
            holder.kakaoTalk.setText("카카오톡");
        }
        holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return listNames.size();
    }

}
 class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mlistName;
       Context cnxt;
     public TextView kakaoTalk;
     public TextView mail;
     public LinearLayout request;

    public viewHolder(View itemView,Context context1){
        super(itemView);
        cnxt=context1;
        itemView.setOnClickListener(this);
        mlistName=(TextView)itemView.findViewById(R.id.recycleViewListName1);
        kakaoTalk=(TextView)itemView.findViewById(R.id.kakaoTalk);
        mail=(TextView)itemView.findViewById(R.id.mail);
        request = (LinearLayout)itemView.findViewById(R.id.requestToCop);

    }


     @Override
     public void onClick(View view) {
         int itemPosition= getAdapterPosition();

         String name = mlistName.getText().toString();

         if(itemPosition!=RecyclerView.NO_POSITION){
         switch(name){
             case "로그인":
                 //로그인
                 Intent showLoginPopup = new Intent(cnxt,LoginInMyPageDialog.class);
                 EventBus.getDefault().post(new LoginEvent("myPage"));
                 this.cnxt.startActivity(showLoginPopup);

                 break;
             case "내가 찜한 바이크":
                 Intent goToMyLikedBikePate = new Intent(cnxt,LikedList.class);
                 Log.e("go","내가 올린 페이지");
                 this.cnxt.startActivity(goToMyLikedBikePate);
                 //내가 찜한 바이크
                 break;
             case "내가 올린 바이크" :
                 //내가 올린 바이크
                 Intent goToMyRegisteredBikePate = new Intent(cnxt,MyRegisteredBike.class);
                 Log.e("go","내가 올린 페이지");
                 this.cnxt.startActivity(goToMyRegisteredBikePate);
                 break;
             case "문의하기":
                 //문의하기
                setClickListener(mail,kakaoTalk);
                 break;
//             case "알림": 바이크에 알림기능 x
//                 //알림
//                 Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + cnxt.getPackageName()));
//                 cnxt.startActivity(intent);
//
//                 break;
            }
         }
     }

     private void setClickListener(TextView mail, TextView kakao)
     {
         mail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 askByGmail(v);
             }
         });
         kakao.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 askByKakao(v,cnxt);
             }
         });
     }

     private void askByGmail(View v)
     {
         String appVersion = null;
         String osVersion = Build.VERSION.RELEASE;
         try{
             PackageInfo i = cnxt.getPackageManager().getPackageInfo(cnxt.getPackageName(), 0);
             appVersion = i.versionName;
         }catch(PackageManager.NameNotFoundException e){}

         Intent email = new Intent(Intent.ACTION_SEND);
         email.setType("plain/text");
         String[]address={"wearecro@gmail.com"};
         email.setPackage("com.google.android.gm");
         email.putExtra(Intent.EXTRA_EMAIL,address);
         email.putExtra(Intent.EXTRA_SUBJECT,"바이크 고객센터에 문의합니다.");
         email.putExtra(Intent.EXTRA_TEXT,"문의 할 내용을 적어주세요.\n\n내용 : \n\n\n\n\n\n\n\n\n\n\n\n"+"APP version : "+appVersion+"\n"+"OS version : "+osVersion);
        cnxt.startActivity(email);
     }
     private void askByKakao(View v,Context cnxt)
     {
         try {
             PlusFriendService.getInstance().chat(cnxt, "_WSiYC");
         } catch (KakaoException e) {
//              에러 처리 (앱키 미설정 등등)
             e.getErrorType();

         }
     }
 }


