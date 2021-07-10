package com.shadowDeveloper.automail.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {



   int image;
   String sender_header,sender_email,cc,bcc,header,desc,date,time;

    public HomeViewModel(int image, String sender_header,String sender_email,String cc,String bcc,String date,String time,String header,String desc) {
        this.image = image;
        this.sender_header=sender_header;
        this.sender_email=sender_email;
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

    public String getSenderHeader() {
        return sender_header;
    }

    public void setSenderHeader(String sender_header) {
        this.sender_header = sender_header;
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

    public String getSenderEmail() {
        return sender_email;
    }

    public void setSenderEmail(String sender_email) {
        this.sender_email = sender_email;
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