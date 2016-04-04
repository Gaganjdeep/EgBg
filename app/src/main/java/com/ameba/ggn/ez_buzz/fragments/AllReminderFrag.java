package com.ameba.ggn.ez_buzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.Task;
import com.ameba.ggn.ez_buzz.adapter.TaskRowAdapter;
import com.ameba.ggn.ez_buzz.utillG.DBTools;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllReminderFrag extends Fragment
{
    private RecyclerView allTasksListView;
    private List<Task> tasks;


    public AllReminderFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.all_reminders, container, false);

        allTasksListView = (RecyclerView) v.findViewById(R.id.allTasksListView);


        return v;
    }


    private void refreshdata()
    {
        // Array of tasks
        DBTools db = new DBTools(getActivity());
        tasks = db.getAllTasks();

        allTasksListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TaskRowAdapter adapter = new TaskRowAdapter(getActivity(), tasks);

        allTasksListView.setAdapter(adapter);

        Collections.reverse(tasks);

        //Sort Entries
//        Collections.sort(tasks, new Comparator<Task>()
//        {
//            @Override
//            public int compare(Task t1, Task t2)
//            {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd,HH:mm aa");
//                Date d1 = new Date();
//                Date d2 = new Date();
//
//                try
//                {
//                    d1 = dateFormat.parse(t1.getDeadline());
//                    Log.d("KS", "Date 1: " + d1.toString());
//                    d2 = dateFormat.parse(t2.getDeadline());
//                    Log.d("KS", "Date 2: " + d2.toString());
//                }
//                catch (ParseException e)
//                {
//                    e.printStackTrace();
//                    Log.e("KS", "exception", e);
//                }
//
//                Log.d("KS", "Compare: " + d1.compareTo(d2));
//                return d1.compareTo(d2);
//            }
//        });
//
        ((TaskRowAdapter) adapter).notifyDataSetChanged();


//        allTasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                final Task currentTask = (Task) allTasksListView.getItemAtPosition(position);
//                Log.d("KS", "Selected: " + currentTask.getId() + " " + currentTask.toString());
//                Intent i = new Intent(getActivity(), EditTask.class);
//                i.putExtra("taskID", currentTask.getId() + "");
//                startActivity(i);
//            }
//        });
    }


    @Override
    public void onResume()
    {
        refreshdata();
        super.onResume();
    }
}
