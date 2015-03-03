package com.upv.tekkent.remindme;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class EditTask extends ActionBarActivity {
    private EditText taskTitleEditText;
    private CheckBox isCompletedCheckBox;
    private EditText taskNotesEditText;
    private EditText taskDeadlineEditText;
    private DatePickerDialog deadlineDatePicker;
    private SimpleDateFormat dateFormatter;
    private int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //Get Data from Intent
        Bundle extras = getIntent().getExtras();
        taskID = Integer.parseInt(extras.getString("taskID"));

        Log.d("KS","Received ID: "+taskID);
        DBTools db = new DBTools(getApplicationContext());
        Task currentTask = db.getTask(taskID);

        taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
        isCompletedCheckBox = (CheckBox) findViewById(R.id.isCompletedCheckBox);
        taskNotesEditText = (EditText) findViewById(R.id.taskNotesEditText);
        taskDeadlineEditText = (EditText) findViewById(R.id.taskDeadlineEditText);

        taskTitleEditText.setText(currentTask.getName(),TextView.BufferType.EDITABLE);
        isCompletedCheckBox.setChecked(currentTask.isCompleted());
        taskNotesEditText.setText(currentTask.getNotes(),TextView.BufferType.EDITABLE);
        taskDeadlineEditText.setText(currentTask.getDeadline(),TextView.BufferType.EDITABLE);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_discard:
                deleteCurrentTask();
                return true;
            case R.id.action_accept:
                updateCurrentTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDate(){
        Calendar newCalendar = Calendar.getInstance();
        deadlineDatePicker = new DatePickerDialog(EditTask.this, new DatePickerDialog.OnDateSetListener() {

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

    public void deleteCurrentTask(){
        AlertDialog.Builder confirmEnd = new AlertDialog.Builder(this);
        confirmEnd.setTitle("Delete Task");
        confirmEnd.setMessage("The deleted task cannot be recovered. Continue?");
        confirmEnd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBTools db = new DBTools(getApplicationContext());
                int rowsAffected = db.deleteTask(taskID);
                Log.d("KS","Deleted Rows: " + rowsAffected);
                Toast.makeText(getApplicationContext(),"Task Deleted", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(EditTask.this, MainActivity.class);
                startActivity(i);
            }
        });

        confirmEnd.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmEnd.show();
    }

    public void updateCurrentTask(){
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
        int rowsAffected = db.updateTask(taskID, taskData);

        Log.d("KS","Updated Rows: " + rowsAffected);
        Toast.makeText(getApplicationContext(),"Task Updated", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(EditTask.this, MainActivity.class);
        startActivity(i);
    }
}
