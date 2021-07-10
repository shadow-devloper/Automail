package com.shadowDeveloper.automail.ui.history;

import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {

    int image;
    String rec_header,rec_email,cc,bcc,header,desc,date,time;

    public HistoryViewModel(int image, String rec_header,String rec_email,String cc,String bcc,String date,String time,String header,String desc) {
        this.image = image;
        this.rec_header=rec_header;
        this.rec_email=rec_email;
        this.cc=cc;
        this.bcc=bcc;
        this.date=date;
        this.time=time;
        this.header = header;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRecHeader() {
        return rec_header;
    }

    public void setRecderHeader(String rec_header) {
        this.rec_header = rec_header;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getRecEmail() {
        return rec_email;
    }

    public void setRecEmail(String rec_email) {
        this.rec_email = rec_email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
