package com.upv.tekkent.remindme;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskRowAdapter extends ArrayAdapter<Task> {
    Activity activity;

    public TaskRowAdapter(Activity activity, List<Task> tasks){
        super(activity, R.layout.task_row,tasks);
        this.activity = activity;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater theInflater = LayoutInflater.from(getContext());
            convertView = theInflater.inflate(R.layout.task_row, parent, false);
        }

        final Task currentTask = getItem(position);
        //Log.d("KS", "Name: " + currentTask.toString());

        DBTools db = new DBTools(getContext());
        final Task taskData = db.getTask(currentTask.getId());

        String taskName= taskData.getName();
        String taskNotes = taskData.getNotes();

        String taskDeadline = taskData.getDeadline();
        TextView taskNameTextView = (TextView) convertView.findViewById(R.id.taskTitleTextView);
        TextView taskNotesTextView = (TextView) convertView.findViewById(R.id.taskNotesTextView);
        TextView taskDeadlineTextView = (TextView) convertView.findViewById(R.id.taskDeadlineTextView);
        taskNameTextView.setText(taskName);
        taskNotesTextView.setText(taskNotes);
        taskDeadlineTextView.setText(taskDeadline);

        taskNameTextView.setTextColor(Color.BLACK);
        taskNotesTextView.setTextColor(Color.GRAY);
        taskDeadlineTextView.setTextColor(Color.GRAY);

        if (taskData.isCompleted()){
            taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskNotesTextView.setPaintFlags(taskNotesTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskDeadlineTextView.setPaintFlags(taskDeadlineTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            TextView taskDeadlineLabelTextView = (TextView) convertView.findViewById(R.id.taskDeadlineLabelTextView);
            taskDeadlineLabelTextView.setPaintFlags(taskDeadlineLabelTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
