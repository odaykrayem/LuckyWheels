package com.example.luckywheels.Models;

public class ContestModel {
    int id;
    int prize;
    String draw_date;
    boolean alreadyParticipant;

    public ContestModel(int id, int prize, String draw_date, boolean alreadyParticipant) {
        this.id = id;
        this.prize = prize;
        this.draw_date = draw_date;
        this.alreadyParticipant = alreadyParticipant;
    }

    public boolean isAlreadyParticipant() {
        return alreadyParticipant;
    }

    public void setAlreadyParticipant(boolean alreadyParticipant) {
        this.alreadyParticipant = alreadyParticipant;
    }



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
