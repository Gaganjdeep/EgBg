package com.ameba.ggn.ez_buzz;

import java.io.Serializable;

public class Task implements Serializable{
    private int id;
    private String image, notes,phonenumber;
    private boolean isCompleted;
    private String deadline;

    public Task(int id, String image, String notes, boolean isCompleted, String deadline,String phonenumber){
        this.id = id;
        this.image = image;
        this.notes = notes;
        this.isCompleted = isCompleted;
        this.deadline = deadline;
        this.phonenumber=phonenumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString(){
        return this.image;
    }
}
