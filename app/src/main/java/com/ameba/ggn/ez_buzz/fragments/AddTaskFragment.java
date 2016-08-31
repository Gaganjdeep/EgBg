package com.ameba.ggn.ez_buzz.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ameba.ggn.ez_buzz.ContactsTab;
import com.ameba.ggn.ez_buzz.MainActivity;
import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.Task;
import com.ameba.ggn.ez_buzz.adapter.ContactsModel;
import com.ameba.ggn.ez_buzz.utillG.BitmapDecoderG;
import com.ameba.ggn.ez_buzz.utillG.CallbackG;
import com.ameba.ggn.ez_buzz.utillG.DBTools;
import com.ameba.ggn.ez_buzz.utillG.RoundedCornersGaganImg;
import com.ameba.ggn.ez_buzz.utillG.UtillContacts;
import com.ameba.ggn.ez_buzz.utillG.Utills;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class AddTaskFragment extends Fragment implements View.OnClickListener
{
    private EditText edNotes/*, edTitle*/;
    TextView /*txtName*/txtPhoneNumber, txtName;


    FloatingActionButton btnAddContact;
    boolean CONTACT_PICKED = false;

    RoundedCornersGaganImg imgReminderPic;
//    private DatePickerDialog deadlineDatePicker;
//    private SimpleDateFormat dateFormatter;


    String PhoneNumber = "";


    public AddTaskFragment()
    {
    }


    public void setNumber(String phoneNumber)
    {
        CONTACT_PICKED = true;
        PhoneNumber = phoneNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.activity_add_task, container, false);


        isCreated = false;
//        edTitle = (EditText) v.findViewById(R.id.edTitle);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtPhoneNumber = (TextView) v.findViewById(R.id.txtPhoneNumber);
        imgReminderPic = (RoundedCornersGaganImg) v.findViewById(R.id.imgReminderPic);
        edNotes = (EditText) v.findViewById(R.id.edNotes);
        btnSelectDate = (Button) v.findViewById(R.id.btnSelectDate);
        btnAddContact = (FloatingActionButton) v.findViewById(R.id.btnAddContact);

        txtPhoneNumber.setText(PhoneNumber.equals("") ? "Select Contact" : PhoneNumber);


        imgReminderPic.setOnClickListener(this);

        txtPhoneNumber.setOnClickListener(this);


        if (!PhoneNumber.isEmpty())
        {
            if (UtillContacts.getContactName(getActivity(), PhoneNumber).equals("unknown"))
            {
                btnAddContact.setVisibility(View.VISIBLE);
                btnAddContact.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Utills.show_dialog_msg(getActivity(), "Add Contact " + PhoneNumber + " to your contacts ?", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                UtillContacts.addContact(getActivity(), PhoneNumber);
                                Utills.global_dialog.dismiss();
                            }
                        });
                    }
                });
            }
        }


        txtName.setText(PhoneNumber.equals("") ? "" : UtillContacts.getContactName(getActivity(), PhoneNumber));


//        showCallerData();
        return v;
    }


    //    private void showCallerData()
//    {
//        txtPhoneNumber.setText(getIntent().getStringExtra(GlobalConstantsG.PHONE));
//
//    }
    Button btnSelectDate;


    public void setDate()
    {
        final Calendar newCalendar = Calendar.getInstance();

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
        final Calendar         newDate       = Calendar.getInstance();


        DatePickerDialog deadlineDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth)
            {
//                Calendar gDate = Calendar.getInstance();
//                gDate.set(year,monthOfYear,dayOfMonth);
//
//                if (newCalendar.getTime().before(newDate.getTime()))
//                {
                TimePickerDialog deadlineTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1)
                    {

                        newDate.set(year, monthOfYear, dayOfMonth, i, i1);
                        if (newCalendar.getTime().before(newDate.getTime()))
                        {

                            Utills.show_dialog_msg(getActivity(), "set reminder for " + dateFormatter.format(newDate.getTime()), new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    Utills.global_dialog.dismiss();
                                    addTaskToDB(newDate.getTimeInMillis());
                                }
                            });
                        }
                        else
                        {
                            Utills.showToast("Invalid date/time selected", getActivity(), true);
                        }


                    }
                }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);
                deadlineTimePicker.show();
//                } else
//                {
//                    Utills.showToast("invalid date selected",getActivity(),true);
//                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        deadlineDatePicker.show();

    }

    String imgURI = "";

    public void addTaskToDB(long remindTime)
    {
        //Get Data from Input Fields
        String taskName    = imgURI;
        String isCompleted = "0";
        String taskNotes   = edNotes.getText().toString();


        String phone = null;


        phone = CONTACT_PICKED ? txtPhoneNumber.getText().toString() : "Reminder";


        String taskDeadline = null;
        try
        {
            taskDeadline = Long.toString(remindTime);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        //Put Data to HashMaps
        HashMap<String, String> taskData = new HashMap<String, String>();
        taskData.put("name", taskName);
        taskData.put("notes", taskNotes);
        taskData.put("deadline", taskDeadline + "");
        taskData.put("isCompleted", isCompleted);
        taskData.put("phone", phone);

        DBTools db     = new DBTools(getActivity());
        int     taskID = db.addTask(taskData);
        Log.d("KS", "Task Inserted: " + taskID + " " + taskData.toString());


        Utills.setALarm(getActivity(), new Task(taskID, taskName, taskNotes, false, taskDeadline, phone));

        if (phone.equals("Reminder"))
        {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        else
        {
            Utills.tellUserDialog(getActivity(), phone, new CallbackG<Intent>()
            {
                @Override
                public void onFinishG(Intent output)
                {
//                    Intent i = new Intent(getActivity(), MainActivity.class);
//                    startActivity(i);
//                    getActivity().finish();

                    if (output != null)
                    {

                        isCreated = true;

                        getActivity().startActivity(Intent.createChooser(output, "Choose app to send message"));

//                        context.startActivity(Intent.createChooser(intent, "Choose Messaging App"));
                    }
                }
            });
        }

    }

    boolean isCreated = false;

    @Override
    public void onResume()
    {
        if (isCreated)
        {
            isCreated = false;
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();

        }


        super.onResume();
    }

    public static final int PICK_CONTACT_REQUEST = 77;

    private void pickContact()
    {
//        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
//        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
//        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);


        Intent pickContactIntent = new Intent(getActivity(), ContactsTab.class);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST)
        {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK)
            {


                ContactsModel contactsModel = (ContactsModel) data.getParcelableExtra("data");


//                String display = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                CONTACT_PICKED = true;
                txtPhoneNumber.setText(/*display+"\n"+*/contactsModel.getPhoneNumber());
                txtName.setText(contactsModel.getName());
                // Do something with the phone number...
            }
        }
        else
        {
            try
            {
                imgURI = BitmapDecoderG.getFromData(requestCode, resultCode, data, getActivity().getContentResolver()).toString();
                imgReminderPic.setRadius(200);
                imgReminderPic.setImageUrl(getActivity(), imgURI);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }


    public void tenMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10);

        addTaskToDB(timeForAlarm);

    }

    public void thirtyMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);

        addTaskToDB(timeForAlarm);
    }

    public void sixtyMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60);

        addTaskToDB(timeForAlarm);
    }

    public void oneTwentyMin(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(120);

        addTaskToDB(timeForAlarm);
    }


    public void threeHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3);

        addTaskToDB(timeForAlarm);
    }

    public void sixHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(6);

        addTaskToDB(timeForAlarm);
    }

    public void twelveHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12);

        addTaskToDB(timeForAlarm);
    }

    public void adayHours(View view)
    {
        long timeForAlarm = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24);

        addTaskToDB(timeForAlarm);
    }

    public void selectDate(View view)
    {
        setDate();
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

                BitmapDecoderG.selectImage(getActivity(), this);

                break;


        }


    }


}
