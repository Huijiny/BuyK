package com.buyk.crocompany.buyk_android.model.LocalData;

import java.io.Serializable;

/**
 * Created by ahdguialee on 2018. 5. 18..
 */

public class FilterData implements Serializable,Cloneable{
    private String filterLocation;
    private int filterLocationPosition;
    private boolean filterNewcar;
    private boolean filterOldcar;
    private boolean fitterOfferingOfType;
    private String fitterRangePriceMin;
    private String fitterRangePriceMax;
    private String fitterRangeDistanceMin;
    private String fitterRangeDistanceMax;
    private int filterRangeDistanceLeft;
    private int filterRangeDistanceRight;
    private int filterRangePriceLeft;
    private int filterRangePriceRight;
    private String filterRangeTotalPrice;
    private String filterRangeTotalDistance;
    private int fitterMinYear;
    private int fitterMinYearPosition;
    private int fitterMaxYear;
    private int fitterMaxYearPosition;
    private String fillterTotalYear;
    private String filterLeftYear;
    private String filterRightYear;
    private boolean noAccident;
    private boolean tuning;
    private boolean changeIsOk;
    private boolean installmentIsOk;

    public boolean isFitterOfferingOfType() {
        return fitterOfferingOfType;
    }

    public String getFitterRangePriceMin() {
        return fitterRangePriceMin;
    }

    public String getFitterRangeDistanceMin() {
        return fitterRangeDistanceMin;
    }


    public boolean isNoAccident() {
        return noAccident;
    }

    public boolean isTuning() {
        return tuning;
    }

    public boolean isChangeIsOk() {
        return changeIsOk;
    }

    public boolean isInstallmentIsOk() {
        return installmentIsOk;
    }

    public void setFitterOfferingOfType(boolean fitterOfferingOfType) {
        this.fitterOfferingOfType = fitterOfferingOfType;
    }


    public int getFitterMinYear() {
        return fitterMinYear;
    }

    public int getFitterMaxYear() {
        return fitterMaxYear;
    }

    public void setFitterMinYear(int fitterMinYear) {
        this.fitterMinYear = fitterMinYear;
    }

    public void setFitterMaxYear(int fitterMaxYear) {
        this.fitterMaxYear = fitterMaxYear;
    }

    public void setNoAccident(boolean noAccident) {
        this.noAccident = noAccident;
    }

    public void setTuning(boolean tuning) {
        this.tuning = tuning;
    }

    public void setChangeIsOk(boolean changeIsOk) {
        this.changeIsOk = changeIsOk;
    }

    public void setInstallmentIsOk(boolean installmentIsOk) {
        this.installmentIsOk = installmentIsOk;
    }

    public boolean isFilterNewcar() {
        return filterNewcar;
    }

    public boolean isFilterOldcar() {
        return filterOldcar;
    }

    public void setFilterNewcar(boolean filterNewcar) {
        this.filterNewcar = filterNewcar;
    }

    public void setFilterOldcar(boolean filterOldcar) {
        this.filterOldcar = filterOldcar;
    }

    public String getFitterRangePriceMax() {
        return fitterRangePriceMax;
    }

    public String getFitterRangeDistanceMax() {
        return fitterRangeDistanceMax;
    }

    public void setFitterRangePriceMin(String fitterRangePriceMin) {
        this.fitterRangePriceMin = fitterRangePriceMin;
    }

    public void setFitterRangePriceMax(String fitterRangePriceMax) {
        this.fitterRangePriceMax = fitterRangePriceMax;
    }

    public void setFitterRangeDistanceMin(String fitterRangeDistanceMin) {
        this.fitterRangeDistanceMin = fitterRangeDistanceMin;
    }

    public void setFitterRangeDistanceMax(String fitterRangeDistanceMax) {
        this.fitterRangeDistanceMax = fitterRangeDistanceMax;
    }

    public void setFilterLocation(String filterLocation) {
        this.filterLocation = filterLocation;
    }

    public String getFilterLocation() {
        return filterLocation;
    }

    public int getFitterMinYearPosition() {
        return fitterMinYearPosition;
    }

    public int getFitterMaxYearPosition() {
        return fitterMaxYearPosition;
    }

    public void setFitterMinYearPosition(int fitterMinYearPosition) {
        this.fitterMinYearPosition = fitterMinYearPosition;
    }

    public void setFitterMaxYearPosition(int fitterMaxYearPosition) {
        this.fitterMaxYearPosition = fitterMaxYearPosition;
    }

    public int getFilterRangeDistanceLeft() {
        return filterRangeDistanceLeft;
    }

    public int getFilterRangeDistanceRight() {
        return filterRangeDistanceRight;
    }

    public int getFilterRangePriceLeft() {
        return filterRangePriceLeft;
    }

    public int getFilterRangePriceRight() {
        return filterRangePriceRight;
    }

    public void setFilterRangeDistanceLeft(int filterRangeDistanceLeft) {
        this.filterRangeDistanceLeft = filterRangeDistanceLeft;
    }

    public void setFilterRangeDistanceRight(int filterRangeDistanceRight) {
        this.filterRangeDistanceRight = filterRangeDistanceRight;
    }

    public void setFilterRangePriceLeft(int filterRangePriceLeft) {
        this.filterRangePriceLeft = filterRangePriceLeft;
    }

    public void setFilterRangePriceRight(int filterRangePriceRight) {
        this.filterRangePriceRight = filterRangePriceRight;
    }

    public String getFilterRangeTotalPrice() {
        return filterRangeTotalPrice;
    }

    public String getFilterRangeTotalDistance() {
        return filterRangeTotalDistance;
    }

    public void setFilterRangeTotalPrice(String filterRangeTotalPrice) {
        this.filterRangeTotalPrice = filterRangeTotalPrice;
    }

    public void setFilterRangeTotalDistance(String filterRangeTotalDistance) {
        this.filterRangeTotalDistance = filterRangeTotalDistance;
    }

    public int getFilterLocationPosition() {
        return filterLocationPosition;
    }

    public void setFilterLocationPosition(int filterLocationPosition) {
        this.filterLocationPosition = filterLocationPosition;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getFillterTotalYear() {
        return fillterTotalYear;
    }

    public void setFillterTotalYear(String fillterTotalYear) {
        this.fillterTotalYear = fillterTotalYear;
    }

    public String getFilterLeftYear() {
        return filterLeftYear;
    }

    public String getFilterRightYear() {
        return filterRightYear;
    }

    public void setFilterLeftYear(String filterLeftYear) {
        this.filterLeftYear = filterLeftYear;
    }

    public void setFilterRightYear(String filterRightYear) {
        this.filterRightYear = filterRightYear;
    }
}
