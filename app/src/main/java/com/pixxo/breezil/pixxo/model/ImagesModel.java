package com.pixxo.breezil.pixxo.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "images_model_table")
public class ImagesModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int roomId;

    private int imageId;

    @SerializedName("largeImageURL")
    @Expose
    private String largeImageURL;

    @SerializedName("likes")
    @Expose
    private int likes;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("views")
    @Expose
    private int views;

    @SerializedName("webformatURL")
    @Expose
    private String webformatURL;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("tags")
    @Expose
    private String tags;

    @SerializedName("downloads")
    @Expose
    private int downloads;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("favorites")
    @Expose
    private int favorites;

    @SerializedName("userImageURL")
    @Expose
    private String userImageURL;

    @SerializedName("previewURL")
    @Expose
    private String previewURL;

    public ImagesModel() {
    }

    protected ImagesModel(Parcel in) {
        largeImageURL = in.readString();
        likes = in.readInt();
        id = in.readInt();
        views = in.readInt();
        webformatURL = in.readString();
        type = in.readString();
        tags = in.readString();
        downloads = in.readInt();
        user = in.readString();
        favorites = in.readInt();
        userImageURL = in.readString();
        previewURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(largeImageURL);
        dest.writeInt(likes);
        dest.writeInt(id);
        dest.writeInt(views);
        dest.writeString(webformatURL);
        dest.writeString(type);
        dest.writeString(tags);
        dest.writeInt(downloads);
        dest.writeString(user);
        dest.writeInt(favorites);
        dest.writeString(userImageURL);
        dest.writeString(previewURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImagesModel> CREATOR = new Creator<ImagesModel>() {
        @Override
        public ImagesModel createFromParcel(Parcel in) {
            return new ImagesModel(in);
        }

        @Override
        public ImagesModel[] newArray(int size) {
            return new ImagesModel[size];
        }
    };

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    @Override
    public String toString() {
        return "ImagesModel{" +
                "largeImageURL='" + largeImageURL + '\'' +
                ", likes=" + likes +
                ", id=" + id +
                ", views=" + views +
                ", webformatURL='" + webformatURL + '\'' +
                ", type='" + type + '\'' +
                ", tags='" + tags + '\'' +
                ", downloads=" + downloads +
                ", user='" + user + '\'' +
                ", favorites=" + favorites +
                ", userImageURL='" + userImageURL + '\'' +
                ", previewURL='" + previewURL + '\'' +
                '}';
    }
}
