package com.ameba.ggn.ez_buzz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.realmUtills.ContactInfo;
import com.ameba.ggn.ez_buzz.realmUtills.EventInfo;
import com.ameba.ggn.ez_buzz.realmUtills.RealmHelperG;
import com.ameba.ggn.ez_buzz.utillG.DBTools;
import com.ameba.ggn.ez_buzz.utillG.GlobalConstantsG;
import com.ameba.ggn.ez_buzz.utillG.PrefHelper;
import com.ameba.ggn.ez_buzz.utillG.UtillContacts;
import com.ameba.ggn.ez_buzz.utillG.Utills;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class AfterCallActivity extends AppCompatActivity
{
    String numberCalled = "";

    private TextView txtName, txtPhoneNumber, tvAlreadyHaveReminder, tvMemo;
    private LinearLayout layoutEvents;

    private Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_call);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
//        wmlp.gravity = Gravity.CENTER;
//        wmlp.dimAmount = 0.6f;
        try
        {


            AdView    mAdView   = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("E33B04E738A4BC55C88E6E171ECDD0AD").build();
//            AdRequest.Builder.addTestDevice("E33B04E738A4BC55C88E6E171ECDD0AD");

            mAdView.loadAd(adRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        numberCalled = getIntent().getStringExtra(GlobalConstantsG.PHONE);

        btnCancel = (Button) findViewById(R.id.btnCancel);

        tvAlreadyHaveReminder = (TextView) findViewById(R.id.tvAlreadyHaveReminder);


        txtName = (TextView) findViewById(R.id.txtName);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtPhoneNumber.setText(numberCalled);
        txtName.setText(UtillContacts.getContactName(AfterCallActivity.this, numberCalled));


        LinearLayout lButton = (LinearLayout) findViewById(R.id.layoutButton);
        animateButtons(lButton);


        DBTools dbHelper = new DBTools(AfterCallActivity.this);

        int reminderCount = dbHelper.getReminderCountByNumber(numberCalled);

        if (reminderCount > 0)
        {
            tvAlreadyHaveReminder.setVisibility(View.VISIBLE);
            tvAlreadyHaveReminder.setText(String.format("NOTE:You have already set %d %s for this contact !", reminderCount, reminderCount == 1 ? "reminder" : "reminders"));
        }


        startTimer();


//        Events added
        layoutEvents = (LinearLayout) findViewById(R.id.layoutEvents);
        List<EventInfo> listEvents = RealmHelperG.getInstance(AfterCallActivity.this).GET_EVENTS();
        if (listEvents != null && listEvents.size() > 0)
        {
            for (EventInfo event : listEvents)
            {
                TextView tvEvent = new TextView(AfterCallActivity.this);
                tvEvent.setTextColor(Color.BLACK);
                tvEvent.setText(String.format("%s " + (event.getEvent_type().equals("Outgoing Call") ? "to" : "from") + " %s", event.getEvent_type(), event.getName()));
                layoutEvents.addView(tvEvent);
            }
        }
//        Events added end


//memo added
        tvMemo = (TextView) findViewById(R.id.tvMemo);
        ContactInfo contactInfo = RealmHelperG.getInstance(AfterCallActivity.this).GET_MEMO_FOR_NUMBER(numberCalled);

        if (contactInfo == null)
        {
            tvMemo.setText(String.format("Tap to set Memo for %s", numberCalled));
        }
        else
        {
            tvMemo.setText(String.format("Memo : %s", contactInfo.getMemo()));
        }


        ((CardView) findViewById(R.id.cardview)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AfterCallActivity.this, R.style.dialog_memo);
                builder.setTitle("Set memo");
                builder.setMessage("Set memo for " + numberCalled);
                final EditText ed = new EditText(AfterCallActivity.this);
                ed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ed.setHint("enter memo here..");
                ed.setTextColor(getResources().getColor(R.color.orange));
                ed.setHintTextColor(getResources().getColor(R.color.extremelt_grey));
                builder.setView(ed);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        tvMemo.setText(ed.getText().toString());

                        ContactInfo contactInfo1 = new ContactInfo();
                        contactInfo1.setName(txtName.getText().toString());
                        contactInfo1.setMemo(ed.getText().toString());
                        contactInfo1.setLastCallTime(System.currentTimeMillis() + "");
                        contactInfo1.setNumber(numberCalled);

                        RealmHelperG.getInstance(AfterCallActivity.this).SAVE_MEMO(contactInfo1);
                    }
                });
                builder.setNegativeButton("Cancel", null);

                builder.show();
            }
        });
//memo added end
    }

    long finishTime = 0;

    public void startTimer()
    {
        finishTime = PrefHelper.getPopupDismissTime(AfterCallActivity.this);

        if (finishTime > 0)
        {
            handler = new CountDownTimer(finishTime * 1000, 1000)
            {
                @Override
                public void onTick(long l)
                {
                    showTimer(finishTime + "");
                    finishTime--;
                }

                @Override
                public void onFinish()
                {
                    finish();
                }
            }.start();

        }
    }


    private void showTimer(final String time)
    {
        new Handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                String styledText = "<small>CANCEL IN -</small> <b>" + time + "</b><small> sec</small>";
                btnCancel.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

            }
        });

    }


    CountDownTimer handler;

    @Override
    public void onUserInteraction()
    {

        if (handler != null)
        {
            handler.cancel();
            btnCancel.setText("CANCEL");

        }

//        startTimer();
        super.onUserInteraction();
    }

    public void cAll(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numberCalled, null));
        startActivity(intent);
    }

    public void mSg(View view)
    {

        Utills.sendMSG(AfterCallActivity.this, numberCalled, "");
    }

    public void msgWhatsapp(View view)
    {
        Utills.sendWhatsAppMsg(AfterCallActivity.this, numberCalled);

    }


    public void canCel(View view)
    {
        finish();
    }

    public void setReminder(View view)
    {

        Intent addTaskIntent = new Intent(AfterCallActivity.this, AfterCallAddReminder.class);
        addTaskIntent.putExtra(GlobalConstantsG.PHONE, numberCalled);
        addTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(addTaskIntent);
        finish();
    }


    private void animateButtons(LinearLayout layout)
    {
        for (int i = 0; i < layout.getChildCount(); i++)
        {
            View rowView = layout.getChildAt(i);
            rowView.animate().setStartDelay(i * 200)
                    .scaleX(1).scaleY(1);
        }

    }


}
