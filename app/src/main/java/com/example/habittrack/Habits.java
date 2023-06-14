package com.example.habittrack;

import java.time.LocalDateTime;
import java.util.Date;

public class Habits {

    private String Name;
    private String question; //Have you done this?
    private int times_per_week; // 3 times a week I must do this activity
    Boolean reminder; // Time to send notification for reminder
    private String adittional_info; //Additional notes for habit
    private int difficulty;
    private int sessions=0;



    private String uid;
    //measurable
    private boolean measurable;
    private String goal;
    private String Unit;

    public Habits(){

    }

    public Habits(String name,String question,int times_per_week,Boolean reminder,String adittional_info,int difficulty){
        this.Name=name;
        this.question=question;
        this.times_per_week=times_per_week;
        this.reminder=reminder;
        this.adittional_info=adittional_info;
        this.difficulty=difficulty;
    }
    public Habits(String name,String question,int times_per_week,Boolean reminder,String adittional_info,int difficulty,boolean measurable,String goal,String Unit){
        this.Name=name;
        this.question=question;
        this.times_per_week=times_per_week;
        this.reminder=reminder;

        this.adittional_info=adittional_info;
        this.goal=goal;
        this.Unit=Unit;
        this.difficulty=difficulty;
    }

    //setters
    public void setName(String name){
        this.Name=name;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setTimes_per_week(int times_per_week) {
        this.times_per_week = times_per_week;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setReminder(Boolean reminder) {this.reminder = reminder;}
    public void setDifficulty(int difficulty) {this.difficulty = difficulty;}
    public void setGoal(String goal) {
        this.goal = goal;
    }
    public void setUnit(String unit) {
        Unit = unit;
    }
    public void setMeasurable(boolean measurable) {this.measurable = measurable;}
    public void setAdittional_info(String adittional_info){this.adittional_info=adittional_info;}
    public void setSessions(int sessions) {
        this.sessions = sessions;
    }
    //Getters
    public String getUid() {
        return uid;
    }
    public boolean isMeasurable() {return measurable;}
    public int getTimes_per_week() {
        return times_per_week;
    }
    public String getName() {
        return Name;
    }
    public String getQuestion() {
        return question;
    }
    public Boolean getReminder() {
        return reminder;
    }
    public String getAdittional_info() {
        return adittional_info;
    }
    public int getDifficulty() {return difficulty;}
    public String getGoal() {
        return goal;
    }
    public String getUnit() {
        return Unit;
    }
    public int getSessions() {
        return sessions;
    }



}
