
package com.shadowDeveloper.automail.Utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Mails {

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;
    @SerializedName("resultSizeEstimate")
    @Expose
    private Integer resultSizeEstimate;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public Integer getResultSizeEstimate() {
        return resultSizeEstimate;
    }

    public void setResultSizeEstimate(Integer resultSizeEstimate) {
        this.resultSizeEstimate = resultSizeEstimate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
