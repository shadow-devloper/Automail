
package com.shadowDeveloper.automail.Utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Part {

    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("headers")
    @Expose
    private List<Header__1> headers = null;
    @SerializedName("body")
    @Expose
    private Body__1 body;
    @SerializedName("partId")
    @Expose
    private String partId;
    @SerializedName("filename")
    @Expose
    private String filename;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<Header__1> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header__1> headers) {
        this.headers = headers;
    }

    public Body__1 getBody() {
        return body;
    }

    public void setBody(Body__1 body) {
        this.body = body;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
