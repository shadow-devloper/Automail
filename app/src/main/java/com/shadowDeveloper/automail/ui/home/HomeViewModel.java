package com.shadowDeveloper.automail.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

   int image;
   String sender,header,desc,date,time;

    public HomeViewModel(int image, String sender,String date,String time,String header,String desc) {
        this.image = image;
        this.sender=sender;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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