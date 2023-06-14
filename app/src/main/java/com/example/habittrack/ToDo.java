package com.example.habittrack;

import java.util.Date;

public class ToDo implements Comparable<ToDo> {
    private String UID;
    private String name;
    private String description;
    private Date end_date;
    private Date reminder;
    private int difficulty;

    public ToDo(){

    }
    public ToDo(String name,String description,Date end_date,Date reminder,int difficulty){
        this.name=name;
        this.description=description;
        this.end_date=end_date;
        this.reminder=reminder;
        this.difficulty=difficulty;
    }
    public ToDo(String name,String description,Date end_date,int difficulty){
        this.name=name;
        this.description=description;
        this.end_date=end_date;
        this.difficulty=difficulty;
    }
    //Setters
    public void setUID(String uid){this.UID=uid;}
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    //Getters
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public Date getReminder() {
        return reminder;
    }

    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public int compareTo(ToDo toDo) {
        return getEnd_date().compareTo(toDo.getEnd_date());
    }

    public String getUID() {return UID; }
}
