package com.ameba.ggn.ez_buzz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ameba.ggn.ez_buzz.app_intro.ColorAnimation;
import com.ameba.ggn.ez_buzz.realmUtills.RealmHelperG;
import com.ameba.ggn.ez_buzz.utillG.PrefHelper;
import com.ameba.ggn.ez_buzz.utillG.Utills;

public class SplashScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        startUp();
//        RealmHelperG.getInstance(SplashScreen.this).DELETE_ALL_EVENTS();
    }


    private void startUp()
    {
        if (ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                    Manifest.permission.READ_CONTACTS))
            {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Utills.show_dialog_msg(SplashScreen.this, "Allow EzBz to access your contact information .", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ActivityCompat.requestPermissions(SplashScreen.this,
                                new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_ALARM, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.CAMERA},
                                11);

                    }
                });
            }
            else
            {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_ALARM, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.CAMERA},
                        11);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
        else
        {
            if (PrefHelper.isFirstTime(SplashScreen.this))
            {
                Intent i = new Intent(getApplicationContext(), ColorAnimation.class);
                 startActivity(i);
            }
            else
            {
                gotoMainActivity();
            }

        }
    }


    private void gotoMainActivity()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                Utills.transitionToActivity(SplashScreen.this, MainActivity.class, findViewById(R.id.splashlogo), "applogo");

            }
        }, 2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 11:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    gotoMainActivity();
                }
                else
                {
                    startUp();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
