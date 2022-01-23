package com.example.luckywheels.Models;

public class ParticipantModel {
    int id;
    int user_id;
    int contest_id;
    boolean is_winner;
    String user_name;
    String email;
    int prize;
    String draw_date;

    public ParticipantModel(int id, int user_id, int contest_id, String user_name, String email, boolean is_winner, int prize, String draw_date) {
        this.id = id;
        this.user_id = user_id;
        this.contest_id = contest_id;
        this.user_name = user_name;
        this.email = email;
        this.is_winner = is_winner;
        this.prize = prize;
        this.draw_date = draw_date;
    }

    //for winners
    public ParticipantModel( String user_name, String email, int prize, String draw_date) {
        this.user_name = user_name;
        this.email = email;
        this.prize = prize;
        this.draw_date = draw_date;
    }
    public ParticipantModel( String user_name,int user_id, String email, int prize, String draw_date) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.email = email;
        this.prize = prize;
        this.draw_date = draw_date;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getContest_id() {
        return contest_id;
    }

    public boolean isIs_winner() {
        return is_winner;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getPrize() {
        return prize;
    }
}
