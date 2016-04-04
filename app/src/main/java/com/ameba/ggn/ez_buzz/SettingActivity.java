package com.ameba.ggn.ez_buzz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ameba.ggn.ez_buzz.utillG.PrefHelper;
import com.ameba.ggn.ez_buzz.utillG.Utills;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity
{


    private AppCompatCheckBox /*chkBoxAllCall*/ chkBoxIncomingCall, chkBoxoutgoingCall, chkBoxMissCall, chkBoxRejetCall;


    AppCompatSpinner spinnerSound;

    private EditText edPopupDismissTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewbyId();

    }

    private void findViewbyId()
    {
//        chkBoxAllCall = (AppCompatCheckBox) findViewById(R.id.chkBoxAllCall);
        chkBoxIncomingCall = (AppCompatCheckBox) findViewById(R.id.chkBoxIncomingCall);
        chkBoxoutgoingCall = (AppCompatCheckBox) findViewById(R.id.chkBoxoutgoingCall);
        chkBoxMissCall = (AppCompatCheckBox) findViewById(R.id.chkBoxMissCall);
        chkBoxRejetCall = (AppCompatCheckBox) findViewById(R.id.chkBoxRejetCall);
        edPopupDismissTime = (EditText) findViewById(R.id.edPopupDismissTime);
        spinnerSound = (AppCompatSpinner) findViewById(R.id.spinnerSound);

        showSavedValues();
    }


    private void saveCheckbox()
    {
//

        PrefHelper.setPopupFor(SettingActivity.this, true, chkBoxIncomingCall.isChecked(), chkBoxoutgoingCall.isChecked(), chkBoxMissCall.isChecked(), chkBoxRejetCall.isChecked());

    }


    private void showSavedValues()
    {
        HashMap<String, Boolean> dataChkBox = PrefHelper.getPopupFor(SettingActivity.this);

//        chkBoxAllCall.setChecked(dataChkBox.get(PrefHelper.all_call));
        chkBoxIncomingCall.setChecked(dataChkBox.get(PrefHelper.incoming_call_end));
        chkBoxoutgoingCall.setChecked(dataChkBox.get(PrefHelper.outgoing_call_end));
        chkBoxMissCall.setChecked(dataChkBox.get(PrefHelper.missed_call));
        chkBoxRejetCall.setChecked(dataChkBox.get(PrefHelper.call_rejected));

        edPopupDismissTime.setText(PrefHelper.getPopupDismissTime(SettingActivity.this) + "");

        spinnerSound.setSelection(PrefHelper.getSoundAlert(SettingActivity.this) ? 0 : 1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:


                saveCheckbox();
                Utills.showToast("Settings saved.", SettingActivity.this, true);


                if (!edPopupDismissTime.getText().toString().trim().isEmpty())
                {
                    PrefHelper.setPopupDismissTime(SettingActivity.this, edPopupDismissTime.getText().toString());
                }
                PrefHelper.setSoundAlert(SettingActivity.this, spinnerSound.getSelectedItemPosition() == 0);

                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
