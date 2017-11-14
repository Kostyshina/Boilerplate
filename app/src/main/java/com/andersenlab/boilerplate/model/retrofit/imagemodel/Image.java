package com.andersenlab.boilerplate.model.retrofit.imagemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Image {

    @SerializedName("source")
    @Expose
    private Source source;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
