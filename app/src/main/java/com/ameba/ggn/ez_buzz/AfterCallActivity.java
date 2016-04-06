package com.ameba.ggn.ez_buzz;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.utillG.DBTools;
import com.ameba.ggn.ez_buzz.utillG.GlobalConstantsG;
import com.ameba.ggn.ez_buzz.utillG.PrefHelper;
import com.ameba.ggn.ez_buzz.utillG.UtillContacts;
import com.ameba.ggn.ez_buzz.utillG.Utills;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AfterCallActivity extends AppCompatActivity
{
    String numberCalled = "";

    TextView txtName, txtPhoneNumber, tvAlreadyHaveReminder;


    Button btnCancel;

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
            String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(MD5(androidId).toUpperCase()).build();

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


    public String MD5(String md5)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
            {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    CountDownTimer handler;

    @Override
    public void onUserInteraction()
    {

        handler.cancel();

        startTimer();
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
