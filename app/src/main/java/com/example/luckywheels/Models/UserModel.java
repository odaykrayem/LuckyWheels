package com.example.luckywheels.Models;

public class UserModel {
    private int userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private int points;
    private float balance;
    private String referralCode;
    private String regRefCode;
    private int refTimes;


    //register constructor
    public UserModel(String firstName, String middleName, String lastName, String email, String regRefCode, String password) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.regRefCode = regRefCode;
    }
    //login constructor
    public UserModel(int userId, String firstName, String middleName, String lastName, String email, int points, float balance, String referralCode, int refTimes) {
        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.points = points;
        this.balance = balance;
        this.referralCode = referralCode;
        this.refTimes =  refTimes;
    }

    public int getUserId() {
        return userId;
    }

    public String getRegRefCode() {
        return regRefCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public float getBalance() {
        return balance;
    }

    public int getRefTimes() {
        return refTimes;
    }

    public String getReferralCode() {
        return referralCode;
    }
}
