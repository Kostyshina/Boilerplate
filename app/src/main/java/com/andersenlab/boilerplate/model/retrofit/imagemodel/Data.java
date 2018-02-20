package com.andersenlab.boilerplate.model.retrofit.imagemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("after")
    @Expose
    private String after;

    @SerializedName("children")
    @Expose
    private List<Child> children = null;

    @SerializedName("preview")
    @Expose
    private Preview preview;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }
}
