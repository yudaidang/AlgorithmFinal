package com.example.yudai.algorithmfinal.Entity;

import android.support.annotation.NonNull;

public class LocationLocal implements Comparable<LocationLocal>{
    private String mName;
    private double mLatitude;
    private double mLongitude;
    private int mStay;
    private int mOpen;
    private int mClose;
    private int mId;

    public LocationLocal(String mName, double mLatitude, double mLongitude, int mOpen, int mClose, int mStay, int mId) {
        this.mName = mName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mStay = mStay;
        this.mOpen = mOpen;
        this.mClose = mClose;
        this.mId = mId;
    }

    @Override
    public int hashCode() {
        return mId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.hashCode() == this.hashCode();
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public int getmStay() {
        return mStay;
    }

    public void setmStay(int mStay) {
        this.mStay = mStay;
    }

    public int getmOpen() {
        return mOpen;
    }

    public void setmOpen(int mOpen) {
        this.mOpen = mOpen;
    }

    public int getmClose() {
        return mClose;
    }

    public void setmClose(int mClose) {
        this.mClose = mClose;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    @Override
    public int compareTo(@NonNull LocationLocal o) {
        if(mOpen == o.mOpen){
            return 0;
        }else if(mOpen > o.mOpen){
            return 1;
        }else {
            return -1;
        }
    }
}
