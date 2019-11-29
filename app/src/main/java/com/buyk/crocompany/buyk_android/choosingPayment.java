package com.buyk.crocompany.buyk_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.buyk.crocompany.buyk_android.model.RemoteData.PaymentResponseMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class choosingPayment extends Fragment implements View.OnClickListener{


    @BindView(R.id.chooseAll)
    RadioButton chooseAll;
    @BindView(R.id.cash)
    ToggleButton cashBtn;
    @BindView(R.id.lease)
    ToggleButton leaseBtn;
    @BindView(R.id.Daecha)
    ToggleButton daecahBtn;
    @BindView(R.id.loan)
    ToggleButton loanBtn;
    @BindView(R.id.card)
    ToggleButton cardBtn;
    PaymentResponseMethod paymentResponseMethod;
    boolean isCashChecked;
    public static choosingPayment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        choosingPayment fragment = new choosingPayment();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onResume() {

        super.onResume();
        initCheck();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.activity_choosing_payment, viewGroup, false);

        ButterKnife.bind(this,rootView);

        isCashChecked=true;
        paymentResponseMethod = new PaymentResponseMethod();
        initCheck();

        cashBtn.setOnClickListener(this);
        cardBtn.setOnClickListener(this);
        daecahBtn.setOnClickListener(this);
        leaseBtn.setOnClickListener(this);
        loanBtn.setOnClickListener(this);
        chooseAll.setOnClickListener(this);
        return rootView;
    }
    public void initCheck()
    {
        if(cashBtn.isChecked()){
            paymentResponseMethod.setCash(true);
        }else{
            paymentResponseMethod.setCash(false);
        }
        if(cardBtn.isChecked()){
            paymentResponseMethod.setCard(true);
        }else{
            paymentResponseMethod.setCard(false);
        }
        if(leaseBtn.isChecked()){
            paymentResponseMethod.setLease(true);
        }else{
            paymentResponseMethod.setLease(false);
        }
        if(loanBtn.isChecked()){
            paymentResponseMethod.setLoan(true);
        }else{
            paymentResponseMethod.setLoan(false);
        }
        if(daecahBtn.isChecked()){
            paymentResponseMethod.setTrade(true);
        }else{
            paymentResponseMethod.setTrade(false);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(checkPayment()){
                ((soldBikeView)getActivity()).setEnabled();
            }else{
                ((soldBikeView)getActivity()).setDisabled();
            }
        }
    }

    private Boolean checkPayment()
    {
        if((!cardBtn.isChecked())&&(!cashBtn.isChecked())&&!(leaseBtn.isChecked())&&!(daecahBtn.isChecked())&&!(loanBtn.isChecked())){
            return false;
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((soldBikeView)getActivity()).itemPost.setPayment_method(paymentResponseMethod);
    }

    boolean isChosedAll=false;
    boolean isDacha = false;
    boolean isLoan = false;
    boolean isCard = false;
    boolean isLease = false;

    @OnClick
    public void onClick(View v) {
        boolean item;
        switch (v.getId())
        {
            case R.id.chooseAll:
                isChosedAll=!isChosedAll;
                if(isChosedAll) {
                    cashBtn.setChecked(true);
                    daecahBtn.setChecked(true);
                    cardBtn.setChecked(true);
                    leaseBtn.setChecked(true);
                    loanBtn.setChecked(true);
                    chooseAll.setChecked(true);
                    isDacha=true;
                    isLoan=true;
                    isCard=true;
                    isLease =true;
                    isCashChecked=true;
                }else{
                    cashBtn.setChecked(false);
                    daecahBtn.setChecked(false);
                    cardBtn.setChecked(false);
                    leaseBtn.setChecked(false);
                    loanBtn.setChecked(false);
                    chooseAll.setChecked(false);
                    isDacha=false;
                    isLoan=false;
                    isCard=false;
                    isLease =false;
                    isCashChecked=false;
                }
                break;
            case R.id.cash:
                isCashChecked=cashBtn.isChecked();
                break;

            case R.id.Daecha:
                isDacha= daecahBtn.isChecked();
                break;
            case R.id.loan:
                isLoan= loanBtn.isChecked();
                break;
            case R.id.card:
                isCard = cardBtn.isChecked();
                break;
            case R.id.lease:
                isLease= leaseBtn.isChecked();
                break;

        }
        initCheck();

        if(checkPayment()){
            ((soldBikeView)getActivity()).setEnabled();
        }else{
            ((soldBikeView)getActivity()).setDisabled();
        }

        if(isLease&&isCard&&isLoan&&isDacha&&isCashChecked){
            chooseAll.setChecked(true);
        }else {
            chooseAll.setChecked(false);
        }


    }


}
