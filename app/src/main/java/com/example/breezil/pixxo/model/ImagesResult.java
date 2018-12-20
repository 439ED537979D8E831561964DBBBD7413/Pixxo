package com.example.breezil.pixxo.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ImagesResult implements Parcelable {

    @PrimaryKey
    private int id;

    @Ignore
    @SerializedName("hits")
    @Expose
    private List<ImagesModel> hits = new ArrayList() ;

    public ImagesResult() {
    }

    public ImagesResult(int id, List<ImagesModel> hits) {
        this.id = id;
        this.hits = hits;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ImagesModel> getHits() {
        return hits;
    }

    public void setHits(List<ImagesModel> hits) {
        this.hits = hits;
    }
}





