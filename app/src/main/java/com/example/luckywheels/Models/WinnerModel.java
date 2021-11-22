package com.example.luckywheels.Models;

public class WinnerModel {

    int id;
    String userName, prize, date;

    public WinnerModel(String userName, String prize, String date) {
        this.userName = userName;
        this.prize = prize;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getPrize() {
        return prize;
    }

    public String getDate() {
        return date;
    }
}
