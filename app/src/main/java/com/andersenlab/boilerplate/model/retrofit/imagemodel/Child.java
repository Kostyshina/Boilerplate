package com.andersenlab.boilerplate.model.retrofit.imagemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Child {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}