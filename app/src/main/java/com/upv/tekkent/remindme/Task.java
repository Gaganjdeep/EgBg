package com.upv.tekkent.remindme;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{
    private int id;
    private String name, notes;
    private boolean isCompleted;
    private String deadline;

    public Task(int id, String name, String notes, boolean isCompleted, String deadline){
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.isCompleted = isCompleted;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return this.name;
    }
}
