package com.buyk.crocompany.buyk_android;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.buyk.crocompany.buyk_android.model.LocalData.LocalUserData;
import com.buyk.crocompany.buyk_android.model.RemoteData.LikeModel;
import com.buyk.crocompany.buyk_android.model.RemoteData.ResponseModelList;
import com.buyk.crocompany.buyk_android.model.RequestMethod;
import com.buyk.crocompany.buyk_android.util.ConvertKmOrPrice;
import com.buyk.crocompany.buyk_android.util.DateCaculater;
import com.buyk.crocompany.buyk_android.util.NoticeDialog;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.buyk.crocompany.buyk_android.util.QuickReturnFooterBehavior;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.Realm;

import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.API_URL;
import static com.buyk.crocompany.buyk_android.model.RemoteData.RetrofitInterface.buyk_api_key;

public class OfferingDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private FirebaseAnalytics mFirebaseAnalytics;

    private SliderLayout mDemoSlider;
    List<BarEntry> entries;
    BarChart chart;
    BarDataSet barDataSet;
    BarData barData;
    Button callDirectBtn;
    GridView tradeInfoGridView;
    ConstraintLayout headerView;
    NestedScrollView scrollView;
    AppBarLayout mAppBarLayout;
    ImageButton backBtn;
    ImageButton backBtnWhite;
    ConstraintLayout contactLayout;
    ToggleButton zeemBtn;
    ToggleButton zeemBtnBlack;
    ResponseModelList detailData;
    TextView priceText;
    ArrayList<Boolean>conditionList;
    TextView tradeLocationDataText;
    TextView rideRangeDataText;
    TextView rideYearDataText;
    TextView accidentCheck;
    TextView accidentCheckDataText;
    TextView paperCheck;
    TextView paperCheckData;
    TextView tuningTextTitle;
    TextView tuningTextView;
    TextView repairTextTitle;
    TextView repairDisctextView;
    TextView detailDiscTextView;
    TextView sellerNameTextView;
    TextView sellerPhoneNumTextView;
    ImageView sellerProfileImageView;
    View divideTuning;
    View divideRepair;
    TextView whenUploadTime;
    Realm localUserDataRealm;
    LocalUserData localUserData;
    HashMap<String,String>headerMap;
    ArrayList<String>imageList;

    int tradeInfoImgList[] = {R.drawable.ic_money, R.drawable.ic_card, R.drawable.ic_exchange, R.drawable.ic_loan, R.drawable.ic_month};
    int tradeInfoImgListActive[] = {R.drawable.ic_money_active, R.drawable.ic_card_active, R.drawable.ic_exchange_active, R.drawable.ic_loan_active, R.drawable.ic_month_active};

    String tradeIngoTextList[] = {"현금","카드","대차","대출","리스"};

    ConvertKmOrPrice convertKmOrPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offering_detail);
        initView();
        initData();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        imageSliderSet();
//        barChartSet(); 시세뷰 api 제공안되어 일단 제거
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "바이크 매물 상세", null /* class override */);
        appbarListener();
        scrollViewListener();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent gotoOfferingImageDetail = new Intent(this, OfferingImageDetail.class);
        gotoOfferingImageDetail.putStringArrayListExtra("imageList",imageList);
        startActivity(gotoOfferingImageDetail);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void initView(){
        ButterKnife.bind(this);
        contactLayout=(ConstraintLayout)findViewById(R.id.contactLayout);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
//        chart = (BarChart) findViewById(R.id.chart); 시세뷰 api 제공안되어 일단 제거
        callDirectBtn = (Button)findViewById(R.id.callDirect);
        tradeInfoGridView = (GridView)findViewById(R.id.tradeInfoGridView);
        scrollView = (NestedScrollView)findViewById(R.id.offeringDetailScrollView);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appBar);
        backBtn = (ImageButton)findViewById(R.id.backBtn);
        backBtnWhite = (ImageButton)findViewById(R.id.backBtnWhite);
        zeemBtn = (ToggleButton)findViewById(R.id.zeemBtn);
        zeemBtnBlack = (ToggleButton)findViewById(R.id.zeemBtnBlack);
        priceText =(TextView)findViewById(R.id.priceText);
        whenUploadTime = (TextView)findViewById(R.id.whenUploadTime);
         tradeLocationDataText = (TextView)findViewById(R.id.tradeLocationData);
         rideRangeDataText = (TextView)findViewById(R.id.rideRangeData);
         rideYearDataText = (TextView)findViewById(R.id.rideYearData);
         accidentCheck = (TextView)findViewById(R.id.accidentCheck);
         accidentCheckDataText = (TextView)findViewById(R.id.accidentCheckData);
         paperCheck = (TextView)findViewById(R.id.paperCheck);
         paperCheckData = (TextView)findViewById(R.id.paperCheckData);
         tuningTextTitle = (TextView)findViewById(R.id.tuningTitleText);
         tuningTextView = (TextView)findViewById(R.id.tuningTextView);
         repairTextTitle = (TextView)findViewById(R.id.repairDisc);
         repairDisctextView = (TextView)findViewById(R.id.repairDisctextView);
         detailDiscTextView =(TextView)findViewById(R.id.detailDiscTextView);
         divideTuning = (View)findViewById(R.id.divideLine4);
         divideRepair = (View)findViewById(R.id.divideLine5);
         whenUploadTime = (TextView)findViewById(R.id.whenUploadTime);
         sellerNameTextView = (TextView)findViewById(R.id.sellerName);
         sellerPhoneNumTextView = (TextView)findViewById(R.id.sellerPhoneNum);
         sellerProfileImageView = (ImageView)findViewById(R.id.sellerProfileImg);

    }
    public void initData() {
        Intent intent = getIntent();
        convertKmOrPrice = new ConvertKmOrPrice();
        detailData = new ResponseModelList();
        detailData = (ResponseModelList) intent.getSerializableExtra("detailData");
        imageList = new ArrayList<>();
        detailData.getImages();
        detailData.getImages().stream().forEach(d -> imageList.add(d.getLarge_image()));
        checkUserData();
        setDetailInfo();
        checkCondition();
        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(), R.layout.trade_info_grid_view_item, tradeInfoImgList, tradeInfoImgListActive);
        tradeInfoGridView.setAdapter(adapter);
        String price = convertKmOrPrice.convertPrice(detailData.getPrice());
        DateCaculater dateCaculater = new DateCaculater();
        String createdTime = detailData.getCreated_at();
        long convertTime = dateCaculater.timeConverterNow(createdTime);
        Log.e("time", String.valueOf(convertTime));
        String recentTime =dateCaculater.convertRecentTime(createdTime,convertTime);
        whenUploadTime.setText(recentTime);
        priceText.setText(price);
        if(detailData.isLiked()){
            zeemBtn.setChecked(true);
            Log.e("찜되어있니", String.valueOf(detailData.isLiked()));
            zeemBtnBlack.setChecked(true);
        }
        else {
            zeemBtn.setChecked(false);
            Log.e("찜안되어있니", String.valueOf(detailData.isLiked()));
            zeemBtnBlack.setChecked(false);
        }

    }
    public void imageSliderSet(){


            // initialize a SliderLayout
        Picasso picasso;
            detailData.getImages().forEach(e -> {
                DefaultSliderView textSliderView = new DefaultSliderView(this);
                textSliderView
                        .image(API_URL + e.getLarge_image())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                mDemoSlider.addSlider(textSliderView);
            });

    }


//    public void barChartSet(){ 시세뷰 api 제공안되어 일단 제거
//
//        entries = new ArrayList<BarEntry>();
//
//        for (int i = 1;i <= 62; i+=2) {
//            //realm 에 쿼리 (7월달로 고정되어 있다. )
//            entries.add(new BarEntry((float) i, i+10));
//        }
//
//        barDataSet = new BarDataSet(entries,"");
//        barDataSet.setDrawValues(false);
//        barDataSet.setColor(Color.rgb(0, 80, 86));
//        barData = new BarData(barDataSet);
//        barData.setBarWidth(1.5f);
//        YAxis leftAxis = chart.getAxisLeft(); leftAxis.setAxisMinValue(0);
//        chart.setPadding(0,0,0,0);
//        chart.setExtraOffsets(0, 0, 0, 0);
//        chart.setData(barData);
//        chart.setTouchEnabled(false);
//        chart.setDoubleTapToZoomEnabled(false);
//        chart.setPinchZoom(false);
//        chart.getLegend().setEnabled(false);
//        chart.setPinchZoom(false);
//        chart.setFitBars(true);
//        chart.setDrawBorders(false);
//        chart.setDrawGridBackground(false);
//        chart.getAxisLeft().setDrawGridLines(false);
//        chart.getXAxis().setDrawGridLines(false);
//        chart.getAxisRight().setDrawGridLines(false);
//        chart.getAxisLeft().setDrawLabels(false);
//        chart.getAxisRight().setDrawLabels(false);
//        chart.getXAxis().setDrawLabels(false);
//        chart.getXAxis().setDrawAxisLine(false);
//        chart.getAxisLeft().setDrawAxisLine(false);
//        chart.getAxisRight().setDrawAxisLine(false);
//        chart.getDescription().setEnabled(false);
//        chart.setDescription(null);
//        chart.animateXY(2000,3000);
//        chart.invalidate();
//    }

    @OnClick(R.id.callDirect)
    public void onClickCallDirect(View v){
        String tel = "tel:" + detailData.getUser().getPhone();
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(tel)));

    }
    @OnClick(R.id.messageDirect)
    public void onClickMessageOpen(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);

                String smsBody = "[BuyK 문의]\n" +
                        detailData.getModel() + detailData.getPrice()+
                        "문의합니다. ";

                sendIntent.putExtra("sms_body", smsBody); // 보낼 문자

                sendIntent.putExtra("address", detailData.getUser().getPhone()); // 받는사람 번호

                sendIntent.setType("vnd.android-dir/mms-sms");

                startActivity(sendIntent);
            }

    class GridViewAdapter extends BaseAdapter {
        Context context;
        int layout;
        int img[];
        int active_img[];
        LayoutInflater inf;

        public GridViewAdapter(Context context, int layout, int[] img,int[] active_img) {
            this.context = context;
            this.layout = layout;
            this.img = img;
            this.active_img = active_img;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }



        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return img[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView = inf.inflate(layout, null);
            ImageView iv = (ImageView)convertView.findViewById(R.id.tradeInfoImage);
            TextView tv = (TextView)convertView.findViewById(R.id.tradeInfoText);
            if(conditionList.get(position) == true) {
                iv.setImageResource(active_img[position]);
                tv.setTextColor(Color.argb(100,0, 80, 86));
            }
            else {
                iv.setImageResource(img[position]);
                tv.setTextColor(Color.argb(30,15, 15, 15));
            }
//
            tv.setText(tradeIngoTextList[position]);

            return convertView;
        }
    }
    public void checkCondition(){
        conditionList = new ArrayList<>();
        conditionList.add(detailData.getPayment_method().isCash());
        conditionList.add(detailData.getPayment_method().isCard());
        conditionList.add(detailData.getPayment_method().isTrade());
        conditionList.add(detailData.getPayment_method().isLoan());
        conditionList.add(detailData.getPayment_method().isLease());
    }
    public void setDetailInfo(){
        tradeLocationDataText.setText(detailData.getDeal_area());
        if(detailData.getDriven_distance() == 0){
            rideRangeDataText.setText("신차");
            accidentCheck.setVisibility(View.GONE);
            accidentCheckDataText.setVisibility(View.GONE);
        }
        else {
            rideRangeDataText.setText(convertKmOrPrice.convertDistance(detailData.getDriven_distance()));
        }
        rideYearDataText.setText(String.valueOf(detailData.getModel_year()+"년"));
        if(TextUtils.isEmpty(detailData.getRepair_history())){
            accidentCheckDataText.setText("무");

        }
        else {
            accidentCheckDataText.setText("유");
        }
        if(!TextUtils.isEmpty(detailData.getDocument_status())) {
            if(detailData.getDocument_status().equals("준비되지않음")){
                Log.e("document-status",detailData.getDocument_status());
                paperCheckData.setText("폐지전");
            }
            else {
                paperCheckData.setText("준비됨");
                paperCheck.setVisibility(View.GONE);
                paperCheckData.setVisibility(View.GONE);

            }
        }
        else {
            paperCheckData.setText("없음");
//            paperCheck.setVisibility(View.GONE);
//            paperCheckData.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(detailData.getTuning_history())){
            tuningTextTitle.setVisibility(View.GONE);
            tuningTextView.setVisibility(View.GONE);
            divideTuning.setVisibility(View.GONE);
        }else {
            if(detailData.getDriven_distance()==0){
                tuningTextTitle.setText("프로모션/이벤트");
            }
            tuningTextView.setText(detailData.getTuning_history());
        }
        if(TextUtils.isEmpty(detailData.getRepair_history())){
            repairDisctextView.setVisibility(View.GONE);
            repairTextTitle.setVisibility(View.GONE);
            divideRepair.setVisibility(View.GONE);
        }
        else {
            repairDisctextView.setText(detailData.getRepair_history());
        }
        detailDiscTextView.setText(detailData.getDetail_information());
        Glide.with(this).
                load(detailData.getUser().getProfile_image_url())
                .apply(new RequestOptions().centerCrop().circleCrop())
                .into(sellerProfileImageView);
        sellerPhoneNumTextView.setText(detailData.getUser().getPhone());
        sellerNameTextView.setText(detailData.getUser().getNickname());
    }
    @OnCheckedChanged(R.id.zeemBtn)
    public void onCheckedChangedZeembtn(CompoundButton button, boolean checked){
        if(checked)
        {
            zeemBtnBlack.setChecked(true);
            Log.e("찜","찜");
            if(!ObjectUtils.isEmpty(headerMap)) {
                Log.e("찜헤더","찜");
                LikeModel likeModel =new LikeModel();
                likeModel.setItem(detailData.getId());
                RequestMethod requestMethod;
                requestMethod = new RequestMethod();
                requestMethod.initLikeModelRequest(headerMap,likeModel);
            }
        }
        else
        {
            zeemBtnBlack.setChecked(false);
            if(!ObjectUtils.isEmpty(headerMap)) {
                LikeModel likeCancelModel = new LikeModel();
                likeCancelModel.setItem(detailData.getId());
                RequestMethod requestMethod;
                requestMethod = new RequestMethod();
                requestMethod.initLikeCancelRequest(headerMap,likeCancelModel);
            }
        }
    }
    @OnCheckedChanged(R.id.zeemBtnBlack)
    public void onCheckedChangedZeembtnBlack(CompoundButton button, boolean checked){
        if(checked)
        {
            zeemBtn.setChecked(true);
            if(ObjectUtils.isEmpty(headerMap)) {
                LikeModel likeModel =new LikeModel();
                likeModel.setItem(detailData.getId());
                RequestMethod requestMethod;
                requestMethod = new RequestMethod();
                requestMethod.initLikeModelRequest(headerMap,likeModel);
            }
        }
        else
        {
            zeemBtn.setChecked(false);

            if(ObjectUtils.isEmpty(headerMap)) {
                LikeModel likeCancelModel = new LikeModel();
                likeCancelModel.setItem(detailData.getId());
                RequestMethod requestMethod;
                requestMethod = new RequestMethod();
                requestMethod.initLikeCancelRequest(headerMap,likeCancelModel);
            }
        }
    }

    @OnClick(R.id.backBtn)
    public void onClickBackBtn(){
        this.finish();
    }
    @OnClick(R.id.backBtnWhite)
    public void onClickBackBtnWhite(){
        this.finish();
    }

    private void appbarListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    Log.e("1","1");
                    backBtn.setVisibility(View.GONE);
                    backBtnWhite.setVisibility(View.VISIBLE);
                    zeemBtn.setVisibility(View.VISIBLE);
                    zeemBtnBlack.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                        Log.e("2","2");
                        backBtn.setVisibility(View.VISIBLE);
                        backBtnWhite.setVisibility(View.GONE);
                        zeemBtn.setVisibility(View.GONE);
                        zeemBtnBlack.setVisibility(View.VISIBLE);
                } else {
                    backBtn.setVisibility(View.GONE);
                    backBtnWhite.setVisibility(View.VISIBLE);
                    zeemBtn.setVisibility(View.VISIBLE);
                    zeemBtnBlack.setVisibility(View.GONE);
                }
            }
        });
    }
    private void scrollViewListener(){
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                AttributeSet attributeSet = null;
                if (diff <= 0) { // 스크롤 bottom
                    QuickReturnFooterBehavior quickReturnFooterBehavior =new QuickReturnFooterBehavior(getApplicationContext(),attributeSet);
                    quickReturnFooterBehavior.show(contactLayout);
                }
            }
        });
    }
    public void checkUserData(){

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String access_token = userInfo.getString("access-token",null);
        if(access_token != null){
          String token = userInfo.getString("access-token",null);
          String  uuid = userInfo.getString("user-uuid",null);
          String  user_id = userInfo.getString("user-id",null);
            headerMap = new HashMap<>();
            headerMap.put("buyk-api-key",buyk_api_key);
            headerMap.put("access-token",token);
            headerMap.put("user-id",user_id);
            headerMap.put("user-uuid",uuid);
        }
    }
    public void permissionCheck(){

    }
}


