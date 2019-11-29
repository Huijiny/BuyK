package com.buyk.crocompany.buyk_android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.Event.BottomSheetEvent;
import com.buyk.crocompany.buyk_android.Fragment.SelectLocationFromSoldBikeBottomSheet;
import com.buyk.crocompany.buyk_android.Fragment.SelectYearFromSoldBikeBottomSheet;
import com.buyk.crocompany.buyk_android.util.DivideNewAndUsedCar;
import com.buyk.crocompany.buyk_android.util.NoticeDialog;
import com.buyk.crocompany.buyk_android.util.NoticeDialogTwoButtons;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.TextView.OnEditorActionListener;

public class bikeBasicInfo extends Fragment implements   OnEditorActionListener,DivideNewAndUsedCar{


    String[] yearTypes={"2018","2017","2016","2015","2014","2013","2012","2011","2010"};
    @BindView(R.id.distanceDriven)
    EditText distanceDriven;
    @BindView(R.id.bikePrice)
    EditText bikePrice;
    boolean isAdd = false;
    Activity activity;
    @BindView(R.id.basicInfo)
    LinearLayout lay2;
    @BindView(R.id.oldBike)
    RadioButton oldBike;
    @BindView(R.id.newBike)
    RadioButton newBike;
    @BindView(R.id.ifBikeOld)
    LinearLayout ifBikeOld;
    @BindView(R.id.basicInfoScrollView)
    ScrollView scrollView;
    @BindView(R.id.before)
    RadioButton before;
    @BindView(R.id.no)
    RadioButton no;
    @BindView(R.id.ready)
    RadioButton ready;
    @BindView(R.id.locationBtn)
    Button locationBtn;
    @BindView(R.id.yearBtn)
    Button yearBtn;

    InputMethodManager imm;
    String dResult="";
    String pResult="";
    String distanceHint = "거리입력";
    String priceHint = "가격";
    Context mContext;
    NoticeDialogTwoButtons nd;
    int rootViewHeight, visibleDisplayFrameHeight, keyboard;
    int editTextLocation;
    ViewGroup rootView;
    NoticeDialog noticeDialog;
    android.support.v4.view.ViewPager pager;
    SelectLocationFromSoldBikeBottomSheet selectLocationBottomSheet;
    SelectYearFromSoldBikeBottomSheet selectYearFromSoldBikeBottomSheet;
    String location;
    String year;

    public bikeBasicInfo() {
        // Required empty public constructor
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
           checkEnable();
        }
    }

    public static bikeBasicInfo newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        bikeBasicInfo fragment = new bikeBasicInfo();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            activity=(Activity) context;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bike_basic_info, container, false);
        ButterKnife.bind(this,rootView);
        noticeDialog = new NoticeDialog(getContext());
        oldBike.setOnClickListener(myListener);
        newBike.setOnClickListener(myListener);
        ready.setOnClickListener(myListener);
        no.setOnClickListener(myListener);
        before.setOnClickListener(myListener);
        imm =(InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);


        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("스크롤뷰 터치","ㅐㅐ");
                imm.hideSoftInputFromWindow(scrollView.getWindowToken(),0);
            }
        });

        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("레이아웃 터치","ㅐㅐ");
                imm.hideSoftInputFromWindow(scrollView.getWindowToken(),0);
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                rootViewHeight = rootView.getRootView().getHeight();
                visibleDisplayFrameHeight=rect.height();

                keyboard = rootViewHeight-visibleDisplayFrameHeight;
            }
        });

        return rootView;
    }


    public void init_dialog(String content,EditText editText)
    {
        noticeDialog.setContent(content);
        noticeDialog.show();
        noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               String text = editText.getText().toString();
                editText.setText(text.substring(0,text.length()-3)+text.substring(text.length()-2,text.length()));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init_spinner();
        init_editTextWidget();
    }

    private void checkEnable()
    {
        int year = ((soldBikeView)getActivity()).itemPost.getModel_year();
        String deal_area = ((soldBikeView)getActivity()).itemPost.getDeal_area();
        int price = ((soldBikeView)getActivity()).itemPost.getPrice();
        int distance=((soldBikeView)getActivity()).itemPost.getDriven_distance();
        String document_status=((soldBikeView)getActivity()).itemPost.getDocument_status();
        Boolean newOrOld = oldBike.isChecked()||newBike.isChecked();

        if((year!=0)&&deal_area!=null&&price!=0&&newOrOld){
            if(oldBike.isChecked()){//중고차일땐 아래 두 개까지 만족해야 ok
                if(distance!=0&&document_status!=null){
                    ((soldBikeView)getActivity()).setEnabled();
                }else{
                    ((soldBikeView)getActivity()).setDisabled();
                }
            }else{//신차일땐 위에 3개만 만족해도 ok
                ((soldBikeView)getActivity()).setEnabled();
            }
        }else{//3개 중 하나라도 안되면 no
            ((soldBikeView)getActivity()).setDisabled();
        }


    }
    void init_spinner()
    {
        ArrayAdapter<String> yearTypeAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,yearTypes);
        yearTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }
    @OnClick(R.id.locationBtn)
    public void onClicklocationBtn() {
         selectLocationBottomSheet = SelectLocationFromSoldBikeBottomSheet.getInstance();
        selectLocationBottomSheet.show(getFragmentManager(), selectLocationBottomSheet.getTag());
    }
    @OnClick(R.id.yearBtn)
    public void onClickYearBtn() {
        selectYearFromSoldBikeBottomSheet = SelectYearFromSoldBikeBottomSheet.getInstance();
        selectYearFromSoldBikeBottomSheet.show(getFragmentManager(), selectYearFromSoldBikeBottomSheet.getTag());
    }
    public void UsedCarView(DivideNewAndUsedCar callback)
    {
        callback.UsedCar();
    }
    public void NewCarView(DivideNewAndUsedCar callback)
    {
        callback.NewCar();
    }


    private View.OnClickListener singleListener = new View.OnClickListener() {
        public void onClick(View v) {
            nd.dismiss();
        }
    };
    public void init_editTextWidget()
    {
        bikePrice.setHint(priceHint);
        distanceDriven.setHint(distanceHint);

        DecimalFormat df = new DecimalFormat("###,###.####");
        distanceDriven.addTextChangedListener(new TextWatcher() {
            String before;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() ==1) {
                        dResult =s.toString()+"km";
                        distanceDriven.setText(dResult);
                        distanceDriven.setSelection(dResult.length() - 2);
                    }else if(s.length()==0){
                       distanceDriven.setHint(distanceHint);
                    }
                    else if(s.length()==2){
                        dResult="";
                        distanceDriven.setText(dResult);
                    }
                    else {
                        if (!s.toString().equals(dResult)) {
                            dResult = df.format(Long.parseLong(s.toString().substring(0, s.toString().length() - 2).replaceAll(",", "")));
                            dResult = dResult + "km";
                            distanceDriven.setText(dResult);
                            distanceDriven.setSelection(dResult.length() - 2);
                        }

                    }
                }catch (Exception e)
                {
                    distanceDriven.setText(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
               String a=s.toString().replace("k","");
               a=a.replace("m","");
               a=a.replace(",","");
               Log.e("after",String.valueOf(a));
               if(a.equals("")){
                   ((soldBikeView) getActivity()).itemPost.setDriven_distance(0);
               }else if(Integer.parseInt(a)>200000){
                  init_dialog("주행거리는 200만 km를 초과 할 수 없습니다.",distanceDriven);
               }
               if(a.length()!=0) {
                   ((soldBikeView) getActivity()).itemPost.setDriven_distance(Integer.valueOf(a));
               }
                checkEnable();
            }
        });

        bikePrice.addTextChangedListener(new TextWatcher(){

            String before;
            @Override
            public void afterTextChanged(Editable s) {
                String a=s.toString().replace("만","");
                a=a.replace("원","");
                a=a.replace(",","");
                Log.e("after",String.valueOf(a));
                if(a.equals("")){
                    ((soldBikeView) getActivity()).itemPost.setPrice(0);
                }else if(Integer.parseInt(a)>=200000) {
                    init_dialog("입력 판매가격은 20억을 초과 할 수 없습니다.",bikePrice);
                }
                if(a.length()!=0) {
                    ((soldBikeView) getActivity()).itemPost.setPrice(Integer.valueOf(a));
                }
                checkEnable();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    if (s.length() ==1) {
                        pResult =s.toString()+"만원";
                        bikePrice.setText(pResult);
                        bikePrice.setSelection(pResult.length() - 2);
                    }else if(s.length()==0){
                        bikePrice.setHint(priceHint);
                    }
                    else if(s.length()==2){
                        pResult="";
                        bikePrice.setText(pResult);
                    }
                    else {
                        if (!s.toString().equals(pResult)) {
                            pResult = df.format(Long.parseLong(s.toString().substring(0, s.toString().length() - 2).replaceAll(",", "")));
                            pResult = pResult + "만원";
                            bikePrice.setText(pResult);
                            bikePrice.setSelection(pResult.length() - 2);
                        }

                    }
                }catch (Exception e)
                {
                    bikePrice.setText(null);
                }
            }
        });


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(v.getId()==R.id.distanceDriven&&actionId== EditorInfo.IME_ACTION_DONE) {
            String tmp = distanceDriven.getText().toString();
            if(isAdd==false) {
                tmp += " km";
            }
            isAdd=true;
            distanceDriven.setText(tmp);
            distanceDriven.setSelection(tmp.length());

            editTextLocation=distanceDriven.getSelectionEnd();

            int x = distanceDriven.getLeft();
            int y=distanceDriven.getTop();

            if(keyboard<editTextLocation){
                scrollView.smoothScrollTo(x,y);
            }
            return true;
        }
        return false;
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        soldBikeView soldBikeView = new soldBikeView();

        @Override
        public void onClick(View v) {
            pager = ((soldBikeView)getActivity()).pager;
            switch (v.getId()) {
                case R.id.oldBike:
                    oldBike.setChecked(true);
                    newBike.setChecked(false);
                    UsedCar();
//                  soldBikeView.usedCarView(divideNewAndUsedCar);
//                    EventBus.getDefault().post(new CarClassificationEvent("old"));

                    break;
                case R.id.newBike:
                    Log.e("newBike","new");
                    oldBike.setChecked(false);
                    newBike.setChecked(true);
//                    EventBus.getDefault().post(new CarClassificationEvent("new"));
                    NewCar();
//                  soldBikeView.newCarView(divideNewAndUsedCar);
                    pager.invalidate();

                    break;
                case R.id.before:
                    ((soldBikeView)getActivity()).itemPost.setDocument_status("U");
                    break;
                case R.id.ready:
                    ((soldBikeView)getActivity()).itemPost.setDocument_status("R");
                    break;
                case R.id.no:
                    ((soldBikeView)getActivity()).itemPost.setDocument_status("N");
                    break;

            }

            checkEnable();
        }
    };

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onBottomSheetEvent(BottomSheetEvent event) {
        Log.e("바텀시트", String.valueOf(event.text));
        if(event.where.equals("location")){
            locationBtn.setText(event.text);
            location = event.text;
            selectLocationBottomSheet.dismiss();

        }else {
            yearBtn.setText(event.text);
            year = event.text;
            selectYearFromSoldBikeBottomSheet.dismiss();
        }
    }



    public void NewCar() {

        pager = ((soldBikeView)getActivity()).pager;
        ((soldBikeView)getActivity()).pagerAdapter.removeCarView(pager.getCurrentItem());
        ((soldBikeView)getActivity()).pagerAdapter.addNewCarView(pager.getCurrentItem());
//        ((soldBikeView)getActivity()).pagerAdapter.notifyDataSetChanged();
        Log.e("신차눌림","newcar");
        pager.invalidate();
        if(TextUtils.isEmpty(location)) {
            locationBtn.setText("전국");
        }
        else {
            locationBtn.setText(location);
        }
        if(TextUtils.isEmpty(year)) {
            yearBtn.setText("2018");
        }
        else {
            yearBtn.setText(year);
        }
        ifBikeOld.setVisibility(View.INVISIBLE);

    }

    public void UsedCar() {
        pager = ((soldBikeView)getActivity()).pager;
        ((soldBikeView)getActivity()).pagerAdapter.removeCarView(pager.getCurrentItem());
        ((soldBikeView)getActivity()).pagerAdapter.addUsedCarView(pager.getCurrentItem());
//        ((soldBikeView)getActivity()).pagerAdapter.notifyDataSetChanged();
        Log.e("중고차눌림","oldcar");
        pager.invalidate();
        if(TextUtils.isEmpty(location)) {
            locationBtn.setText("전국");
        }
        else {
            locationBtn.setText(location);
        }
        if(TextUtils.isEmpty(year)) {
            yearBtn.setText("2018");
        }
        else {
            yearBtn.setText(year);
        }
        ifBikeOld.setVisibility(View.VISIBLE);

    }
}
