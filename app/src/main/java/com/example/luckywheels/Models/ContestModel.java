package com.example.luckywheels.Models;

public class ContestModel {
    int id;
    int prize;
    String draw_date, draw_time;

    public String getDraw_time() {
        return draw_time;
    }

    public ContestModel(int id, int prize, String draw_date, String draw_time) {
        this.id = id;
        this.prize = prize;
        this.draw_date = draw_date;
        this.draw_time = draw_time;
    }
//    boolean alreadyParticipant;

    public ContestModel(int id, int prize, String draw_date) {
        this.id = id;
        this.prize = prize;
        this.draw_date = draw_date;
//        this.alreadyParticipant = alreadyParticipant;
    }

//    public boolean isAlreadyParticipant() {
//        return alreadyParticipant;
//    }

//    public void setAlreadyParticipant(boolean alreadyParticipant) {
//        this.alreadyParticipant = alreadyParticipant;
//    }



    public int getId() {
        return id;
    }

    public int getPrize() {
        return prize;
    }

    public String getDraw_date() {
        return draw_date;
    }
}
