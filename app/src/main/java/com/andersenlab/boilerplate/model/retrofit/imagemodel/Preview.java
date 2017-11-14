package com.andersenlab.boilerplate.model.retrofit.imagemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class Preview {

    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
