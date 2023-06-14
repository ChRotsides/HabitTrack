package com.example.habittrack.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String userName;
    private String userEmail;
    private long exp;
    public LoggedInUser(){

    }
    public LoggedInUser(String userId, String email,String userName) {
        this.userId = userId;
        this.userEmail=email;
        this.userName = userName;
        this.exp=0;

    }

    public String getUserId() {
        return userId;
    }
    public long getExp(){return exp;}
    public String getUserName() {
        return userName;
    }
    public String getUserEmail(){return userEmail;}

    public void setUserId(String userId){
        this.userId=userId;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setUserEmail(String email){this.userEmail=email;}
    public void setExp(long exp){this.exp=exp;}

}