package com.ameba.ggn.ez_buzz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.utillG.DBTools;
import com.ameba.ggn.ez_buzz.utillG.RoundedCornersGaganImg;
import com.ameba.ggn.ez_buzz.utillG.UtillContacts;
import com.ameba.ggn.ez_buzz.utillG.Utills;

import java.util.HashMap;

public class AlarmActivity extends AppCompatActivity
{
    Task    taskData;
    DBTools dbHelper;
    private TextView txtPhoneNumber, txtTitle, txtNotes;
    private LinearLayout layoutbtnCallContainer, layoutbtnMsgContainer, layoutbtnOtherContainer;

    private RoundedCornersGaganImg imgVReminderImg;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reminder_complete);

        dbHelper = new DBTools(AlarmActivity.this);

        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtNotes = (TextView) findViewById(R.id.txtNotes);
        imgVReminderImg = (RoundedCornersGaganImg) findViewById(R.id.imgVReminderImg);
        layoutbtnCallContainer = (LinearLayout) findViewById(R.id.layoutbtnCallContainer);
        layoutbtnMsgContainer = (LinearLayout) findViewById(R.id.layoutbtnMsgContainer);
        layoutbtnOtherContainer = (LinearLayout) findViewById(R.id.layoutbtnOtherContainer);

        getReminderata();

        animateButtons();
    }

    private void animateButtons()
    {
        for (int i = 0; i < layoutbtnCallContainer.getChildCount(); i++)
        {
            View rowView = layoutbtnCallContainer.getChildAt(i);
            rowView.animate().setStartDelay(i * 200)
                    .scaleX(1).scaleY(1);
        }
        for (int i = 0; i < layoutbtnMsgContainer.getChildCount(); i++)
        {
            View rowView = layoutbtnMsgContainer.getChildAt(i);
            rowView.animate().setStartDelay(i * 200)
                    .scaleX(1).scaleY(1);
        }
        for (int i = 0; i < layoutbtnOtherContainer.getChildCount(); i++)
        {
            View rowView = layoutbtnOtherContainer.getChildAt(i);
            rowView.animate().setStartDelay(i * 200)
                    .scaleX(1).scaleY(1);
        }
    }


    private void getReminderata()
    {
        taskData = (Task) getIntent().getSerializableExtra("data");


        if (taskData.getPhonenumber().contains("Reminder"))
        {
//            txtTitle.setText(taskData.getPhonenumber().substring(taskData.getPhonenumber().indexOf("\n")));
//            txtPhoneNumber.setText(taskData.getPhonenumber().substring(0, taskData.getPhonenumber().indexOf("\n") + 1));
            txtTitle.setText("Reminder");

            layoutbtnMsgContainer.setVisibility(View.GONE);
            layoutbtnCallContainer.setVisibility(View.GONE);
        }
        else
        {

            try
            {
                txtTitle.setText(UtillContacts.getContactName(AlarmActivity.this, taskData.getPhonenumber()));
            }
            catch (Exception | Error e)
            {
                e.printStackTrace();
            }

            txtPhoneNumber.setText(taskData.getPhonenumber());

        }


        txtNotes.setText(taskData.getNotes());


        if (!taskData.getImage().isEmpty())
        {
            imgVReminderImg.setRadius(200);
            imgVReminderImg.setImageUrl(AlarmActivity.this, taskData.getImage());
        }

//        updateAlarminDB();
    }


    public void dOne(View view)
    {
        startActivity(new Intent(AlarmActivity.this, MainActivity.class));
        finish();
    }


    private void updateAlarminDB()
    {
        try
        {
//            Calendar cal=new GregorianCalendar();
//            cal.setTime(GlobalConstantsG.dateformat.parse(taskData.getDeadline()));
            //Put Data to HashMaps
            HashMap<String, String> taskDataHashMap = new HashMap<String, String>();
            taskDataHashMap.put("name", taskData.getImage());
            taskDataHashMap.put("notes", taskData.getNotes());
            taskDataHashMap.put("deadline", taskData.getDeadline() + "");
            taskDataHashMap.put("isCompleted", "1");
            taskDataHashMap.put("phone", taskData.getPhonenumber());

            dbHelper.updateTask(taskData.getId(), taskDataHashMap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void delEte(View view)
    {
        dbHelper.deleteTask(taskData.getId());
        startActivity(new Intent(AlarmActivity.this, MainActivity.class));
        finish();
    }

    public void cAll(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", taskData.getPhonenumber(), null));
        startActivity(intent);
    }

    public void mSg(View view)
    {
//        String number = taskData.getPhonenumber();  // The number on which you want to send SMS
//
//        Uri    uri    = Uri.parse("smsto:" + number);
//        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//        intent.putExtra("sms_body", "");
//        startActivityForResult(Intent.createChooser(intent, "Choose Messaging App"), 0);

        Utills.sendMSG(AlarmActivity.this, taskData.getPhonenumber(),"");
    }

    public void msgWhatsapp(View view)
    {
        Utills.sendWhatsAppMsg(AlarmActivity.this, taskData.getPhonenumber());
    }


    public void editReminder(View view)
    {
        Intent i = new Intent(AlarmActivity.this, EditTask.class);
        i.putExtra("taskID", taskData.getId() + "");
        startActivity(i);
        finish();
    }
}
