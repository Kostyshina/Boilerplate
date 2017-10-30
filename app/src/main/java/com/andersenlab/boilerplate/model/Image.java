package com.andersenlab.boilerplate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable{
    private int serialNumber;
    private String contentDescription;
    private String imageUrl;

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

    public Image() {
    }

    public Image(int serialNumber, String contentDescription, String imageUrl) {
        this.serialNumber = serialNumber;
        this.contentDescription = contentDescription;
        this.imageUrl = imageUrl;
    }

    private Image(Parcel source) {
        this.serialNumber = source.readInt();
        this.contentDescription = source.readString();
        this.imageUrl = source.readString();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
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
        dest.writeInt(this.serialNumber);
        dest.writeString(this.contentDescription);
        dest.writeString(this.imageUrl);
    }
}
