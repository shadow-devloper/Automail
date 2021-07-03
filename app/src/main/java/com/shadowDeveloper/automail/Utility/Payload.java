
package com.shadowDeveloper.automail.Utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Payload {

    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("body")
    @Expose
    private Body body;
    @SerializedName("partId")
    @Expose
    private String partId;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("headers")
    @Expose
    private List<Header> headers = null;
    @SerializedName("parts")
    @Expose
    private List<Part> parts = null;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
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

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

}
