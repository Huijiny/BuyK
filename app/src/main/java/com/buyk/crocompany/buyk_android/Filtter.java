package com.buyk.crocompany.buyk_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appyvet.materialrangebar.RangeBar;
import com.buyk.crocompany.buyk_android.Fragment.SelectLocationBottomSheet;
import com.buyk.crocompany.buyk_android.Fragment.SelectMaxYearBottomSheet;
import com.buyk.crocompany.buyk_android.Fragment.SelectMinYearBottomSheet;
import com.buyk.crocompany.buyk_android.model.LocalData.FilterData;
import com.buyk.crocompany.buyk_android.util.ConvertKmOrPrice;
import com.buyk.crocompany.buyk_android.util.ObjectUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.Realm;


public class Filtter extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    //지역
    String locationData[] = {"전국","서울", "부산", "인천", "광주", "울산", "대전", "세종", "대구", "경기", "강원", "경남", "경북", "충북", "충남", "전북", "전남", "제주"};
    String spinnerLocationData = "";
    int spinnerLocationPosition;
    ArrayAdapter locationDataAdapter;
    Button locationBtn;
    Spinner spinnerLocation;


    //rangebar
    RangeBar rangePrice;
    RangeBar rangeDistance;
    TextView textRangeLeftPrice;
    TextView textRangeRightPrice;
    TextView textRangeLeftDistance;
    TextView textRangeRightDistance;
    TextView textDistanceDriven;
    TextView textFromDistance;
    TextView textToDistance;
    String rangePriceList[] = {"0", "50", "100", "150", "200", "250", "300", "350", "400", "500", "600", "700", "800", "1000", "1200", "1400", "1600", "1800", "2000", "2200", "2400", "2600", "2800", "3000", "3500", "4000", "4500", "5000", "제한없음"};
    String rangeDistanceList[] = {"0", "500", "1000", "2000", "3000", "5000", "7000", "10000", "20000", "30000", "40000", "50000", "100000", "제한없음" };
    String rangePriceMin;
    String rangePriceMax;
    String rangeDistanceMin;
    String rangeDistanceMax;
    String rangeTotalDistance;
    String rangeTotalPrice;
    int rangePriceLeft;
    int rangePriceRight;
    int rangeDistanceLeft;
    int rangeDistanceRight;
    ConstraintLayout priceTextField;


    //연식관련
    Button minYearBtn;

    Button maxYearBtn;
    ArrayList<String> deviceYearMin = new ArrayList<String>();
    ArrayList<String> deviceYearMax = new ArrayList<>();
    int spinnerCountMin=0;
    int spinnerCountMax=0;
    int spinnerMinYearValue=0;
    int spinnerMaxYearValue=0;
    boolean spinnerClickCheck = false;
    public boolean spinnerLeftClickCheck = false;
    public boolean spinnerRightClickCheck = false;
    boolean firstspinnerInit =false;
    ArrayAdapter minYearSpinnerAdapter;
    ArrayAdapter maxYearSpinnerAdapter;
    int spinnerMinYearPosition;
    int spinnerMaxYearPosition;
    String yearTotal;

    //신차 중고차
    boolean oldCaralble = true;
    boolean newCarable = true;
    ToggleButton oldCarBtn;
    ToggleButton newCarBtn;

    //기타옵션
    boolean noAcident = false;
    boolean tunning = false;
    boolean changeIsOk = false;
    boolean installmentIsOk = false;
    ToggleButton noAcidentBtn;
    ToggleButton tunningBtn;
    ToggleButton changeIsOkBtn;
    ToggleButton installmentIsOkBtn;

    //Local data
    public FilterData filterData = new FilterData();
    Realm realmFilterData;

    //filterApply
    boolean filterApplyCheck;

    SelectLocationBottomSheet selectLocationBottomSheet;
    SelectMinYearBottomSheet selectMinYearBottomSheet;
    SelectMaxYearBottomSheet selectMaxYearBottomSheet;
    View bottomSheet;

    boolean filterDataIsNull =false;
    String modelName;
    int model;
    String model__types__id;

    ArrayList<String>model__in = new ArrayList<>();

    ConvertKmOrPrice convertKmOrPrice;

    @BindView(R.id.rangeLeftPrice) TextView rangeLeftPriceTextView;
    @BindView(R.id.textView31) TextView textView31;

    @BindView(R.id.rangeRightPrice) TextView rangeRightPriceTextView;
    @BindView(R.id.textView32) TextView textView32;

    @BindView(R.id.rangeLeftDistance) TextView rangeLeftDistanceTextView;
    @BindView(R.id.textView36) TextView textView36;

    @BindView(R.id.rangeRightDistance) TextView rangeRightDistanceTextView;
    @BindView(R.id.textView38) TextView textView38;

    @BindView(R.id.deviceYearText) TextView deviceYearText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_fliter);
        ButterKnife.bind(this);
        convertKmOrPrice = new ConvertKmOrPrice();

        initView();

        initLocaldata();


        checkRange();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "필터", null /* class override */);
    }

    public void initView(){
      rangePrice  = findViewById(R.id.rangeBar);
      rangeDistance = findViewById(R.id.rangeBar1);
      textDistanceDriven = findViewById(R.id.distanceDriven);
      minYearBtn = findViewById(R.id.minYearBtn);
      maxYearBtn = findViewById(R.id.maxYearBtn);
      locationBtn = findViewById(R.id.locationBtn);
      noAcidentBtn = findViewById(R.id.noAcident);
      tunningBtn = findViewById(R.id.tunning);
      changeIsOkBtn = findViewById(R.id.changeIsOk);
      installmentIsOkBtn = findViewById(R.id.installmentisOk);
        textRangeLeftPrice = findViewById(R.id.rangeLeftPrice);
        textRangeRightPrice=findViewById(R.id.rangeRightPrice);
        textRangeLeftDistance =findViewById(R.id.rangeLeftDistance);
       textRangeRightDistance = findViewById(R.id.rangeRightDistance);
        textFromDistance = findViewById(R.id.textView36);
        textToDistance = findViewById(R.id.textView38);
        oldCarBtn = findViewById(R.id.button4);
        newCarBtn = findViewById(R.id.button3);
        priceTextField = findViewById(R.id.priceTextField);
        bottomSheet = findViewById(R.id.bottom_sheet);

        selectLocationBottomSheet = new SelectLocationBottomSheet();
    }

    public void initLocaldata(){
        Intent intent = getIntent();
        modelName = intent.getStringExtra("modelName");
        if(!ObjectUtils.isEmpty(intent.getStringArrayListExtra("model__in"))){
            model__in.addAll(intent.getStringArrayListExtra("model__in"));
            Log.e("model__in_in_filter",model__in.get(0));
        }
        model__types__id = intent.getStringExtra("model__types__id");
        if((FilterData)intent.getSerializableExtra("filterData")!=null) {
            filterData = (FilterData) intent.getSerializableExtra("filterData");
            Log.e("filterData", String.valueOf(filterData));
            Log.e("filterData", String.valueOf(filterData.getFilterLocationPosition()));

            if(!TextUtils.isEmpty( filterData.getFilterLocation())){
                locationBtn.setText(filterData.getFilterLocation());
            }

            if (filterData.getFilterRangePriceRight()==0){
                rangePrice.setRangePinsByIndices(filterData.getFilterRangePriceLeft(), 28);
            }else {
                rangePrice.setRangePinsByIndices(filterData.getFilterRangePriceLeft(), filterData.getFilterRangePriceRight());

            }
            setRangePrice(filterData.getFilterRangePriceLeft(), filterData.getFilterRangePriceRight());

            if(filterData.getFilterRangeDistanceRight()==0) {
                rangeDistance.setRangePinsByIndices(filterData.getFilterRangeDistanceLeft(), 13);

            }
            else {
                rangeDistance.setRangePinsByIndices(filterData.getFilterRangeDistanceLeft(), filterData.getFilterRangeDistanceRight());

            }
            setRangeDistance(filterData.getFilterRangeDistanceLeft(), filterData.getFilterRangeDistanceRight());
            oldCaralble = filterData.isFilterOldcar();
            newCarable = filterData.isFilterNewcar();
            if(oldCaralble == false && newCarable == false) {
                oldCarBtn.setChecked(true);
                newCarBtn.setChecked(true);
            }
            else {
                oldCarBtn.setChecked(oldCaralble);
                newCarBtn.setChecked(newCarable);
            }
            Log.e("중고차", String.valueOf(oldCaralble));
            Log.e("신차", String.valueOf(newCarable));

            Log.e("누가","initLocalData");


            noAcidentBtn.setChecked(filterData.isNoAccident());
            tunningBtn.setChecked(filterData.isTuning());
            changeIsOkBtn.setChecked(filterData.isChangeIsOk());
            installmentIsOkBtn.setChecked(filterData.isInstallmentIsOk());
            if(filterData.getFitterMinYear()!=0){
                minYearBtn.setText(String.valueOf(filterData.getFitterMinYear()));
            }
            if(filterData.getFitterMaxYear()!=0){
                maxYearBtn.setText(String.valueOf(filterData.getFitterMaxYear()));
            }
        }

    }

    public void checkRange(){
        rangePrice.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
                Log.e("leftPin&&rightPin", String.valueOf(leftPinIndex+" !"+rightPinIndex));


                if(leftPinIndex >= 0 && rightPinIndex < 29) {
                    setRangePrice(leftPinIndex,rightPinIndex);
                    rangePriceLeft = leftPinIndex;
                    rangePriceRight = rightPinIndex;
                    rangePriceMin = rangePriceList[leftPinIndex];
                    rangePriceMax = rangePriceList[rightPinIndex];
                }
            }

        });
        rangeDistance.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

                Log.e("leftPin&&rightPin", String.valueOf(leftPinIndex+" !"+rightPinIndex));

                if(leftPinIndex >= 0 && rightPinIndex <14 ) {
                    setRangeDistance(leftPinIndex, rightPinIndex);
                    rangeDistanceRight=rightPinIndex;
                    rangeDistanceLeft=leftPinIndex;
                    rangeDistanceMin= rangeDistanceList[leftPinIndex];
                    rangeDistanceMax = rangeDistanceList[rightPinIndex];
                }
            }
        });
    }


    @OnClick(R.id.minYearBtn)
    public void onClickMinYearBtn(){
        selectMinYearBottomSheet = SelectMinYearBottomSheet.getInstance();
        selectMinYearBottomSheet.show(getSupportFragmentManager(), selectMinYearBottomSheet.getTag());
    }
    public void setMinYearBtnText(String s){
        minYearBtn.setText(s);
        Log.e("필터에 최소연식있나", String.valueOf(filterData.getFitterMinYear()));
        selectMinYearBottomSheet.dismiss();
    }
    public void setMaxYearBtnText(String s){
        maxYearBtn.setText(s);
        selectMaxYearBottomSheet.dismiss();
    }
    @OnClick(R.id.maxYearBtn)
    public void onClickMaxYearBtn(){
        selectMaxYearBottomSheet = SelectMaxYearBottomSheet.getInstance();
        selectMaxYearBottomSheet.show(getSupportFragmentManager(), selectMaxYearBottomSheet.getTag());
    }

    @OnCheckedChanged(R.id.button4)
    public void onChangeOldcar(ToggleButton button, boolean check){

        oldCaralble = button.isChecked();
        Log.e("왜신차만 나오냐고",String.valueOf(newCarable)+String.valueOf(oldCaralble));
        if(newCarable==true && oldCaralble ==true) {
            rangeDistance.setVisibility(View.VISIBLE);
            textDistanceDriven.setVisibility(View.VISIBLE);
            filterData.setFilterOldcar(oldCaralble);
            filterData.setFilterNewcar(newCarable);
            newCarBtn.setEnabled(true);
            oldCarBtn.setEnabled(true);
            filterDataIsNull=true;
        }
        else if(newCarable==true && oldCaralble == false){
            rangeDistance.setVisibility(View.GONE);
            textToDistance.setVisibility(View.GONE);
            textFromDistance.setVisibility(View.GONE);
            textRangeLeftDistance.setVisibility(View.GONE);
            textRangeRightDistance.setVisibility(View.GONE);
            textDistanceDriven.setVisibility(View.GONE);
            filterData.setFilterOldcar(oldCaralble);
            filterData.setFilterNewcar(newCarable);
            newCarBtn.setEnabled(false);
            oldCarBtn.setEnabled(true);

            filterDataIsNull=true;
        }
        else if(newCarable == false && oldCaralble == true){
            filterData.setFilterNewcar(false);
            filterData.setFilterOldcar(true);
            oldCarBtn.setEnabled(false);
            newCarBtn.setEnabled(true);

        }
        else if(newCarable==false && oldCaralble == false) {
            filterData.setFilterNewcar(true);
            filterData.setFilterOldcar(true);

        }

    }
    @OnCheckedChanged(R.id.button3)
    public void onClickNewCar( ToggleButton button){
        newCarable = button.isChecked();

        if(newCarable==true && oldCaralble == false){
            rangeDistance.setVisibility(View.GONE);
            textToDistance.setVisibility(View.GONE);
            textFromDistance.setVisibility(View.GONE);
            textRangeLeftDistance.setVisibility(View.GONE);
            textRangeRightDistance.setVisibility(View.GONE);
            textDistanceDriven.setVisibility(View.GONE);
            filterData.setFilterNewcar(newCarable);
            filterData.setFilterOldcar(oldCaralble);
            newCarBtn.setEnabled(false);
            oldCarBtn.setEnabled(true);
            filterDataIsNull=true;
        }
        else if(newCarable==true && oldCaralble ==true) {
            rangeDistance.setVisibility(View.VISIBLE);
            textDistanceDriven.setVisibility(View.VISIBLE);
            filterData.setFilterNewcar(newCarable);
            filterData.setFilterOldcar(oldCaralble);

            newCarBtn.setEnabled(true);
            oldCarBtn.setEnabled(true);
            filterDataIsNull=true;
        }else if(newCarable ==false && oldCaralble == true) {
            filterData.setFilterNewcar(false);
            filterData.setFilterOldcar(oldCaralble);
            newCarBtn.setEnabled(true);
            oldCarBtn.setEnabled(false);
        }
        else if(newCarable==false && oldCaralble == false) {
            filterData.setFilterNewcar(true);
            filterData.setFilterOldcar(true);


        }

    }
    @OnCheckedChanged(R.id.noAcident)
    public void onChangeNoAcident(ToggleButton button, boolean check){
        boolean checked = button.isChecked();
        if(checked==true){
            noAcident = true;
            filterData.setNoAccident(noAcident);
            filterDataIsNull=true;
        }else{
            noAcident= false;
            filterData.setNoAccident(noAcident);

        }
    }
    @OnCheckedChanged(R.id.tunning)
    public void onChangeTunning(ToggleButton button, boolean check){
        boolean checked = button.isChecked();
        if(checked==true){
            tunning = true;
            filterData.setTuning(tunning);
            filterDataIsNull=true;
        }else{
            tunning = false;
            filterData.setTuning(tunning);

        }
    }
    @OnCheckedChanged(R.id.changeIsOk)
    public void onChangeChangeInOk(ToggleButton button, boolean check){
        boolean checked = button.isChecked();
        if(checked == true){
            changeIsOk = true;
            filterData.setChangeIsOk(changeIsOk);
            filterDataIsNull=true;
        }else{
            changeIsOk= false;
            filterData.setChangeIsOk(changeIsOk);
        }
    }
    @OnCheckedChanged(R.id.installmentisOk)
    public void onChangeInstallmentIsOk(ToggleButton button, boolean check){
        boolean checked = button.isChecked();
        if(checked==true){
            installmentIsOk = true;
            filterData.setInstallmentIsOk(installmentIsOk);
            filterDataIsNull=true;
        }else{
            installmentIsOk= false;
            filterData.setInstallmentIsOk(installmentIsOk);
        }
    }
    @OnClick(R.id.locationBtn)
    public void onClickLocationBtn(View v){
        selectLocationBottomSheet = SelectLocationBottomSheet.getInstance();
        selectLocationBottomSheet.show(getSupportFragmentManager(), selectLocationBottomSheet.getTag());
    }

    public void setlocationData(String location) {
        locationBtn.setText(location);
        selectLocationBottomSheet.dismiss();
    }

    @OnClick(R.id.resetBtn)
    public void onClickResetBtn(View v){
        Log.e("resetbtn","클릭오키");
        rangePrice.setRangePinsByIndices(0,28);
        textRangeRightPrice.setText("");
        textRangeLeftPrice.setText("");
        textRangeLeftDistance.setText("");
        textRangeRightDistance.setText("");
        rangeDistance.setRangePinsByIndices(0,13);
        noAcidentBtn.setChecked(false);
        tunningBtn.setChecked(false);
        changeIsOkBtn.setChecked(false);
        installmentIsOkBtn.setChecked(false);
        filterApplyCheck=false;
        noAcident=false;
        filterData = new FilterData();
        filterData.setFilterNewcar(true);
        filterData.setFilterOldcar(true);
        filterDataIsNull = true;
        oldCarBtn.setChecked(true);
        newCarBtn.setChecked(true);
        minYearBtn.setText("최소연식");
        maxYearBtn.setText("최대연식");
        locationBtn.setText("전국");
        filterData.setFilterLocation(null);
        filterData.setFitterMinYear(0);
        filterData.setFitterMaxYear(0);

    }


    @OnClick(R.id.applyBtn)
    public void onClickApplyBtn(View v){
        Log.e("applyBtn","클릭오키");

        filterApplyCheck = true;
        Intent backToOffetingList = new Intent(Filtter.this,OfferingList.class);
        if(filterDataIsNull==true) {
            Log.e("filterDataIsNull", String.valueOf(filterDataIsNull));
            backToOffetingList.putExtra("filterData", filterData);
            Log.e("필터에 최소연식있나", String.valueOf(filterData.getFitterMinYear()));

            backToOffetingList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            backToOffetingList.putExtra("modelName",modelName);
            backToOffetingList.putStringArrayListExtra("model__in",model__in);
            backToOffetingList.putExtra("model__types__id",model__types__id);
            startActivity(backToOffetingList);
            overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
        }else {
            super.finish();
            backToOffetingList.putExtra("modelName",modelName);
            overridePendingTransition(R.anim.no_change, R.anim.slide_down_info);

        }


    }

    public void setRangePrice(int leftPinIndex, int rightPinIndex){
        if(leftPinIndex>=0&&rightPinIndex<=28) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) deviceYearText.getLayoutParams();

            filterDataIsNull=true;
            if(leftPinIndex >0 && rightPinIndex < 28){
                rangeTotalPrice = rangePriceList[leftPinIndex] + "만원" + "~" + rangePriceList[rightPinIndex] + "만원";
                filterData.setFilterRangeTotalPrice(rangeTotalPrice);
                filterData.setFitterRangePriceMin(rangePriceList[leftPinIndex]);
                filterData.setFitterRangePriceMax(rangePriceList[rightPinIndex]);
                rangeLeftPriceTextView.setVisibility(View.VISIBLE);
                textView31.setVisibility(View.VISIBLE);
                rangeRightPriceTextView.setVisibility(View.VISIBLE);
                textView32.setVisibility(View.VISIBLE);
            }
            Log.e("leftPin&&rightPin", String.valueOf(leftPinIndex + rightPinIndex));
            try{
                if (rangePriceList[leftPinIndex].equals("0") && rangePriceList[rightPinIndex].equals("제한없음")) {
                    filterData.setFilterRangePriceLeft(0);
                    filterData.setFilterRangePriceRight(28);
                    filterData.setFitterRangePriceMax("");
                    filterData.setFitterRangePriceMin("");
                    filterData.setFilterRangeTotalPrice("");
                    rangeLeftPriceTextView.setVisibility(View.GONE);
                    textView31.setVisibility(View.GONE);
                    rangeRightPriceTextView.setVisibility(View.GONE);
                    textView32.setVisibility(View.GONE);


                } else if (rangePriceList[leftPinIndex].equals("0") && !rangePriceList[rightPinIndex].equals("0") && !rangePriceList[rightPinIndex].equals("제한없음")) {
                    String price = convertKmOrPrice.convertPrice(Integer.parseInt(rangePriceList[rightPinIndex]));

                    textRangeRightPrice.setText(price);
                    filterData.setFilterRangeTotalPrice(rangePriceList[rightPinIndex] + "만원까지");
                    filterData.setFilterRangePriceLeft(0);
                    filterData.setFilterRangePriceRight(rightPinIndex);
                    filterData.setFitterRangePriceMax(rangePriceList[rightPinIndex]);
                    rangeLeftPriceTextView.setVisibility(View.GONE);
                    textView31.setVisibility(View.GONE);
                    rangeRightPriceTextView.setVisibility(View.VISIBLE);
                    textView32.setVisibility(View.VISIBLE);

                } else if (!rangePriceList[leftPinIndex].equals("0") && rangePriceList[rightPinIndex].equals("제한없음")) {
                    String price = convertKmOrPrice.convertPrice(Integer.parseInt(rangePriceList[leftPinIndex]));

                    textRangeLeftPrice.setText(price);
                    try {
                        filterData.setFilterRangeTotalPrice(rangePriceList[leftPinIndex] + "만원부터");
                        filterData.setFilterRangePriceLeft(leftPinIndex);
                        filterData.setFilterRangePriceRight(28);
                        filterData.setFitterRangePriceMin(rangePriceList[leftPinIndex]);
                        rangeLeftPriceTextView.setVisibility(View.VISIBLE);
                        textView31.setVisibility(View.VISIBLE);
                        rangeRightPriceTextView.setVisibility(View.GONE);
                        textView32.setVisibility(View.GONE);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if (!rangePriceList[leftPinIndex].equals("0") && !rangePriceList[rightPinIndex].equals("제한없음")) {
                    String priceLeft = convertKmOrPrice.convertPrice(Integer.parseInt(rangePriceList[leftPinIndex]));
                    String priceRight = convertKmOrPrice.convertPrice(Integer.parseInt(rangePriceList[rightPinIndex]));

                    textRangeRightPrice.setText(priceRight);
                    textRangeLeftPrice.setText(priceLeft);
                    filterData.setFilterRangePriceLeft(leftPinIndex);
                    filterData.setFilterRangePriceRight(rightPinIndex);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void setRangeDistance(int leftPinIndex, int rightPinIndex){
        if(leftPinIndex>=0&&rightPinIndex<=14) {
            filterDataIsNull=true;
            if(leftPinIndex>0&&rightPinIndex<14){
                rangeTotalDistance = rangeDistanceList[leftPinIndex] + "km" + "~" + rangeDistanceList[rightPinIndex] + "km";
                filterData.setFilterRangeTotalDistance(rangeTotalDistance);
                filterData.setFilterRangeDistanceRight(rightPinIndex);
                filterData.setFitterRangeDistanceMin(rangeDistanceList[leftPinIndex]);
                filterData.setFitterRangeDistanceMax(rangeDistanceList[rightPinIndex]);
                rangeLeftDistanceTextView.setVisibility(View.VISIBLE);
                textView36.setVisibility(View.VISIBLE);
                rangeRightDistanceTextView.setVisibility(View.VISIBLE);
                textView38.setVisibility(View.VISIBLE);

            }
            try{
                if (rangeDistanceList[leftPinIndex].equals("0") && rangeDistanceList[rightPinIndex].equals("제한없음")) {
                    filterData.setFilterRangeDistanceLeft(0);
                    filterData.setFitterRangeDistanceMin("");
                    filterData.setFitterRangeDistanceMax("");
                    filterData.setFilterRangeDistanceRight(13);
                    filterData.setFilterRangeTotalDistance("");
                    rangeLeftDistanceTextView.setVisibility(View.GONE);
                    textView36.setVisibility(View.GONE);
                    rangeRightDistanceTextView.setVisibility(View.GONE);
                    textView38.setVisibility(View.GONE);

                } else if (rangeDistanceList[leftPinIndex].equals("0") && !rangeDistanceList[rightPinIndex].equals("0") && !rangeDistanceList[rightPinIndex].equals("제한없음")) {
                    String distance = convertKmOrPrice.convertDistance(Integer.parseInt(rangeDistanceList[rightPinIndex]));
                    textRangeRightDistance.setText(distance);
                    filterData.setFilterRangeTotalDistance(rangeDistanceList[rightPinIndex] + "km 까지");
                    filterData.setFilterRangeDistanceLeft(leftPinIndex);
                    filterData.setFilterRangeDistanceRight(rightPinIndex);
                    filterData.setFitterRangeDistanceMax(rangeDistanceList[rightPinIndex]);
                    rangeLeftDistanceTextView.setVisibility(View.GONE);
                    textView36.setVisibility(View.GONE);
                    rangeRightDistanceTextView.setVisibility(View.VISIBLE);
                    textView38.setVisibility(View.VISIBLE);
                } else if (!rangeDistanceList[leftPinIndex].equals("0") && rangeDistanceList[rightPinIndex].equals("제한없음")) {
                    String distance = convertKmOrPrice.convertDistance(Integer.parseInt(rangeDistanceList[leftPinIndex]));

                    textRangeLeftDistance.setText(distance);
                    try {
                        filterData.setFilterRangeTotalDistance(rangeDistanceList[leftPinIndex] + "km 부터");
                        filterData.setFilterRangeDistanceLeft(leftPinIndex);
                        filterData.setFilterRangeDistanceRight(rightPinIndex);
                        filterData.setFitterRangeDistanceMin(rangeDistanceList[leftPinIndex]);
                        rangeLeftDistanceTextView.setVisibility(View.VISIBLE);
                        textView36.setVisibility(View.VISIBLE);
                        rangeRightDistanceTextView.setVisibility(View.GONE);
                        textView38.setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if (!rangeDistanceList[leftPinIndex].equals("0") && !rangeDistanceList[rightPinIndex].equals("제한없음")) {
//                textDistanceDriven.setText(rangeTotalDistance);
                    String distanceRight = convertKmOrPrice.convertDistance(Integer.parseInt(rangeDistanceList[rightPinIndex]));

                    String distanceLeft = convertKmOrPrice.convertDistance(Integer.parseInt(rangeDistanceList[leftPinIndex]));

                    textRangeRightDistance.setText(distanceRight);
                    textRangeLeftDistance.setText(distanceLeft);
                    filterData.setFilterRangeDistanceLeft(leftPinIndex);
                    filterData.setFilterRangeDistanceRight(rightPinIndex);

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @OnClick(R.id.closeFilterBtn)
    public void onClickCloseFilterBtn(){
        this.finish();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);

    }


}
