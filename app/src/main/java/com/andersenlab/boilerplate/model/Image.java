package com.andersenlab.boilerplate.model;

import android.os.Parcel;

public class Image extends BaseModel {

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int i) {
            return new Image[0];
        }
    };

    private long id;
    private String contentDescription;
    private String imageUrl;

    public Image() {
    }

    public Image(String contentDescription, String imageUrl) {
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
    }

    private Image(Parcel source) {
        this.id = source.readLong();
        this.contentDescription = source.readString();
        this.imageUrl = source.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(this.id);
        dest.writeString(this.contentDescription);
        dest.writeString(this.imageUrl);
    }
}
