
package com.shadowDeveloper.automail.Utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GmailMessages {

    @SerializedName("internalDate")
    @Expose
    private String internalDate;
    @SerializedName("historyId")
    @Expose
    private String historyId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("snippet")
    @Expose
    private String snippet;
    @SerializedName("sizeEstimate")
    @Expose
    private Integer sizeEstimate;
    @SerializedName("threadId")
    @Expose
    private String threadId;
    @SerializedName("labelIds")
    @Expose
    private List<String> labelIds = null;
    @SerializedName("payload")
    @Expose
    private Payload payload;

    public String getInternalDate() {
        return internalDate;
    }

    public void setInternalDate(String internalDate) {
        this.internalDate = internalDate;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Integer getSizeEstimate() {
        return sizeEstimate;
    }

    public void setSizeEstimate(Integer sizeEstimate) {
        this.sizeEstimate = sizeEstimate;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public List<String> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<String> labelIds) {
        this.labelIds = labelIds;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

}
