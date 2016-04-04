package com.ameba.ggn.ez_buzz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ameba.ggn.ez_buzz.adapter.ContactsModel;
import com.ameba.ggn.ez_buzz.fragments.AddTaskFragment;
import com.ameba.ggn.ez_buzz.utillG.BitmapDecoderG;
import com.ameba.ggn.ez_buzz.utillG.DBTools;
import com.ameba.ggn.ez_buzz.utillG.GlobalConstantsG;
import com.ameba.ggn.ez_buzz.utillG.RoundedCornersGaganImg;
import com.ameba.ggn.ez_buzz.utillG.UtillContacts;
import com.ameba.ggn.ez_buzz.utillG.Utills;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class EditTask extends AppCompatActivity implements View.OnClickListener
{
    private EditText edNotes/*, edTitle*/;
    TextView txtPhoneNumber, txtName;

    private int taskID;
    RoundedCornersGaganImg imgReminderPic;

    Task dataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Get Data from Intent


        txtName = (TextView) findViewById(R.id.txtName);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        edNotes = (EditText) findViewById(R.id.edNotes);
        imgReminderPic = (RoundedCornersGaganImg) findViewById(R.id.imgReminderPic);

        getDataToShow();

    }


    private void getDataToShow()
    {

        Bundle extras = getIntent().getExtras();
        taskID = Integer.parseInt(extras.getString("taskID"));
        Log.d("KS", "Received ID: " + taskID);
        DBTools db = new DBTools(getApplicationContext());
        dataTask = db.getTask(taskID);

        if (!dataTask.getPhonenumber().equals("Reminder"))
        {
            txtPhoneNumber.setText(dataTask.getPhonenumber());

            txtName.setText(UtillContacts.getContactName(EditTask.this, dataTask.getPhonenumber()));
        }
        else
        {
            txtPhoneNumber.setText("Reminder");
        }


//        edTitle.setText(dataTask.getImage());
        edNotes.setText(dataTask.getNotes());
        imgReminderPic.setRadius(200);
        imgReminderPic.setImageUrl(EditTask.this, dataTask.getImage());


        imgReminderPic.setOnClickListener(this);

        txtPhoneNumber.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_discard:
                deleteCurrentTask();
                return true;
            case R.id.action_accept:

                try
                {
                    String name = dataTask.getImage();
                    String phoneNumber = txtPhoneNumber.getText().toString();
                    String notes = edNotes.getText().toString();

                    Calendar cal = new GregorianCalendar();
                    cal.setTime(GlobalConstantsG.dateformat.parse(dataTask.getDeadline()));


                    updateCurrentTask(new Task(taskID, name, notes, dataTask.isCompleted(), cal.getTimeInMillis() + "", phoneNumber));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteCurrentTask()
    {
        final AlertDialog.Builder confirmEnd = new AlertDialog.Builder(this);
        confirmEnd.setTitle("Delete Task");
        confirmEnd.setMessage("The deleted task cannot be recovered. Continue?");
        confirmEnd.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                DBTools db = new DBTools(getApplicationContext());
                int rowsAffected = db.deleteTask(taskID);
                Log.d("KS", "Deleted Rows: " + rowsAffected);
                Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_SHORT).show();


//                Intent i = new Intent(EditTask.this, MainActivity.class);
//                startActivity(i);
                finish();
            }
        });

        confirmEnd.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        confirmEnd.show();
    }

    public void updateCurrentTask(Task data)
    {
        String taskName = data.getImage();
        String isCompleted = data.isCompleted() ? "1" : "0";
        String taskNotes = data.getNotes();
        String taskDeadline = data.getDeadline();


        String phone = null;


//        phone = CONTACT_PICKED ? txtPhoneNumber.getText().toString() : "Reminder";


        //Put Data to HashMaps
        HashMap<String, String> taskData = new HashMap<String, String>();
        taskData.put("name", taskName);
        taskData.put("notes", taskNotes);
        taskData.put("deadline", taskDeadline);
        taskData.put("isCompleted", isCompleted);
        taskData.put("phone", data.getPhonenumber());


        DBTools db = new DBTools(getApplicationContext());
        int rowsAffected = db.updateTask(taskID, taskData);

        Log.d("KS", "Updated Rows: " + rowsAffected);
        Toast.makeText(getApplicationContext(), "Reminder Updated", Toast.LENGTH_SHORT).show();


//        Intent i = new Intent(EditTask.this, MainActivity.class);
//        startActivity(i);
        finish();
    }


    public void tenMin(View view)
    {

        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
        updateReminder(timeForAlarm);
    }

    public void thirtyMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
        updateReminder(timeForAlarm);
    }

    public void sixtyMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60);

        updateReminder(timeForAlarm);
    }

    public void oneTwentyMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(120);


        updateReminder(timeForAlarm);

    }


    public void threeHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3);

        updateReminder(timeForAlarm);
    }

    public void sixHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(6);

        updateReminder(timeForAlarm);
    }

    public void twelveHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12);

        updateReminder(timeForAlarm);
    }

    public void adayHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24);

        updateReminder(timeForAlarm);
    }

    public void selectDate(View view)
    {
        setDate();
    }


    public void setDate()
    {
        final Calendar newCalendar = Calendar.getInstance();

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy HH:mm aa");
        final Calendar newDate = Calendar.getInstance();


        DatePickerDialog deadlineDatePicker = new DatePickerDialog(EditTask.this, new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth)
            {

                TimePickerDialog deadlineTimePicker = new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1)
                    {

                        newDate.set(year, monthOfYear, dayOfMonth, i, i1);
                        if (newCalendar.getTime().before(newDate.getTime()))
                        {

                            Utills.show_dialog_msg(EditTask.this, "set reminder for " + dateFormatter.format(newDate.getTime()), new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    updateReminder(newDate.getTimeInMillis());
                                }
                            });
                        }
                        else
                        {
                            Utills.showToast("Invalid date/time selected", EditTask.this, true);
                        }


                    }
                }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);
                deadlineTimePicker.show();

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        deadlineDatePicker.show();

    }


    private void updateReminder(long timeForAlarm)
    {
        String name = dataTask.getImage();


        String notes = edNotes.getText().toString();

        Task task = new Task(taskID, name, notes, false, timeForAlarm + "", txtPhoneNumber.getText().toString());

        updateCurrentTask(task);
        Utills.setALarm(EditTask.this, task);

    }

    private void pickContact()
    {
        Intent pickContactIntent = new Intent(EditTask.this, ContactsTab.class);
        startActivityForResult(pickContactIntent, AddTaskFragment.PICK_CONTACT_REQUEST);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtPhoneNumber:

                pickContact();

                break;

            case R.id.imgReminderPic:

                BitmapDecoderG.selectImage(EditTask.this, null);

                break;


        }

    }


    boolean CONTACT_PICKED = false;
    String imgURI = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request it is that we're responding to
        if (requestCode == AddTaskFragment.PICK_CONTACT_REQUEST)
        {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK)
            {
                ContactsModel contactsModel = (ContactsModel) data.getParcelableExtra("data");


//                String display = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                CONTACT_PICKED = true;
                txtPhoneNumber.setText(contactsModel.getPhoneNumber());
                txtName.setText(contactsModel.getName());
            }
        }
        else
        {
            try
            {
                imgURI = BitmapDecoderG.getFromData(requestCode, resultCode, data, getContentResolver()).toString();
                imgReminderPic.setRadius(200);
                imgReminderPic.setImageUrl(EditTask.this, imgURI);

                dataTask.setImage(imgURI);


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }
}
