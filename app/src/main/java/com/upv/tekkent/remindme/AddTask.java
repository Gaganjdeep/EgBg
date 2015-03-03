package com.upv.tekkent.remindme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddTask extends ActionBarActivity {
    private EditText taskTitleEditText;
    private CheckBox isCompletedCheckBox;
    private EditText taskNotesEditText;
    private EditText taskDeadlineEditText;
    private DatePickerDialog deadlineDatePicker;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
        isCompletedCheckBox = (CheckBox) findViewById(R.id.isCompletedCheckBox);
        taskNotesEditText = (EditText) findViewById(R.id.taskNotesEditText);
        taskDeadlineEditText = (EditText) findViewById(R.id.taskDeadlineEditText);

        taskDeadlineEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    setDate();
                }

                return false;
            }
        });
    }

    public void setDate(){
        Calendar newCalendar = Calendar.getInstance();
        deadlineDatePicker = new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,monthOfYear,dayOfMonth);
                taskDeadlineEditText.setText(dateFormatter.format(newDate.getTime()), TextView.BufferType.EDITABLE);

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        deadlineDatePicker.show();

    }

    public void addTaskToDB(){
        //Get Data from Input Fields
        String taskName = taskTitleEditText.getText().toString();
        String isCompleted = isCompletedCheckBox.isChecked() == true ? "1":"0";
        String taskNotes = taskNotesEditText.getText().toString();
        String taskDeadline = taskDeadlineEditText.getText().toString();

        //Put Data to HashMaps
        HashMap<String, String> taskData = new HashMap<String, String>();
        taskData.put("name",taskName);
        taskData.put("notes",taskNotes);
        taskData.put("deadline",taskDeadline);
        taskData.put("isCompleted",isCompleted);

        DBTools db = new DBTools(getApplicationContext());
        int taskID = db.addTask(taskData);
        Log.d("KS","Task Inserted: "+taskID+" "+taskData.toString());

        Intent i = new Intent(AddTask.this, MainActivity.class);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_cancel:
                Intent i = new Intent(AddTask.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.action_accept:
                addTaskToDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
