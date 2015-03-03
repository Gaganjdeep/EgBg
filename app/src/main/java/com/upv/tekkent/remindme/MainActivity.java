package com.upv.tekkent.remindme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private ListView allTasksListView;
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Array of tasks
        DBTools db = new DBTools(getApplicationContext());
        tasks = db.getAllTasks();

        allTasksListView = (ListView) findViewById(R.id.allTasksListView);
        ListAdapter adapter = new TaskRowAdapter(this,tasks);

        allTasksListView.setAdapter(adapter);

        //Sort Entries
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                Date d1 = new Date();
                Date d2 = new Date();

                Log.d("KS","Deadlines: "+t1.getDeadline()+" "+t2.getDeadline());
                try {
                    d1 = dateFormat.parse(t1.getDeadline());
                    Log.d("KS","Date 1: "+d1.toString());
                    d2 = dateFormat.parse(t2.getDeadline());
                    Log.d("KS","Date 2: "+d2.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("KS", "exception", e);
                }

                Log.d("KS", "Compare: "+d1.compareTo(d2));
                return d1.compareTo(d2);
            }
        });

        ((TaskRowAdapter)adapter).notifyDataSetChanged();

        allTasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Task currentTask = (Task) allTasksListView.getItemAtPosition(position);
                Log.d("KS","Selected: "+currentTask.getId()+" "+currentTask.toString());
                Intent i = new Intent(MainActivity.this, EditTask.class);
                i.putExtra("taskID", currentTask.getId()+"");
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            Intent i = new Intent(MainActivity.this, AddTask.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
