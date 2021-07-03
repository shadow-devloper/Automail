
package com.shadowDeveloper.automail.Utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Body__1 {

    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("size")
    @Expose
    private Integer size;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
