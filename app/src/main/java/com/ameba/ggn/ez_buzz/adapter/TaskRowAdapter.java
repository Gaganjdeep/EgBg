package com.ameba.ggn.ez_buzz.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.EditTask;
import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.Task;
import com.ameba.ggn.ez_buzz.utillG.RoundedCornersGaganImg;
import com.ameba.ggn.ez_buzz.utillG.UtillContacts;

import java.util.List;

public class TaskRowAdapter extends RecyclerView.Adapter<TaskRowAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;
    List<Task> dataList;
    Context    activity;

    public TaskRowAdapter(Context activity, List<Task> tasks)
    {
//        super(activity, R.layout.contacts_inflator, tasks);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        dataList = tasks;
    }

    @Override
    public MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new MyViewHolderG(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final Task taskData = dataList.get(position);


        String taskName  = taskData.getImage();
        String taskNotes = taskData.getNotes();

        String taskDeadline = taskData.getDeadline();


        holder.imgReminderPicAdapter.setRadius(200);
        if (!taskName.isEmpty())
        {
            holder.imgReminderPicAdapter.setImageUrl(activity, taskName);
        }


        String name = UtillContacts.getContactName(activity, taskData.getPhonenumber());


        holder.txtvPhoneNo.setText(name.equals("unknown") ? taskData.getPhonenumber() : name);


        holder.txtvNotes.setText(taskNotes);
        holder.txtvTime.setText(taskDeadline);


        holder.txtvNotes.setTextColor(Color.GRAY);
        holder.txtvTime.setTextColor(Color.GRAY);

        if (taskData.isCompleted())
        {

            holder.txtvNotes.setPaintFlags(holder.txtvNotes.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtvTime.setPaintFlags(holder.txtvTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtvPhoneNo.setPaintFlags(holder.txtvPhoneNo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }


        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Task currentTask = taskData;
                Log.d("KS", "Selected: " + currentTask.getId() + " " + currentTask.toString());
                Intent i = new Intent(activity, EditTask.class);
                i.putExtra("taskID", currentTask.getId() + "");
                activity.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    private void animate(View view)
    {
        view.setAlpha(0f);
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView txtvNotes, txtvTime, txtvPhoneNo;
        View                   view;
        RoundedCornersGaganImg imgReminderPicAdapter;

        CardView cardView;

        public MyViewHolderG(View itemView)
        {
            super(itemView);
            txtvNotes = (TextView) itemView.findViewById(R.id.txtvNotes);
            txtvTime = (TextView) itemView.findViewById(R.id.txtvTime);
            txtvPhoneNo = (TextView) itemView.findViewById(R.id.txtvPhoneNo);

            cardView = (CardView) itemView.findViewById(R.id.cardview);

            imgReminderPicAdapter = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgReminderPicAdapter);
            view = itemView;
        }
    }
}
