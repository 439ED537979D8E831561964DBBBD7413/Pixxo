package com.example.breezil.pixxo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ImagesResult implements Parcelable {

    @SerializedName("hits")
    @Expose
    private List<ImagesModel> hits = new ArrayList() ;

    protected ImagesResult(Parcel in) {
        hits = in.createTypedArrayList(ImagesModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(hits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImagesResult> CREATOR = new Creator<ImagesResult>() {
        @Override
        public ImagesResult createFromParcel(Parcel in) {
            return new ImagesResult(in);
        }

        @Override
        public ImagesResult[] newArray(int size) {
            return new ImagesResult[size];
        }
    };

    public List<ImagesModel> getHits() {
        return hits;
    }

    public void setHits(List<ImagesModel> hits) {
        this.hits = hits;
    }
}





