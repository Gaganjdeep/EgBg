package com.ameba.ggn.ez_buzz.utillG;

/**
 * Created by gagandeep on 21 Dec 2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

import com.ameba.ggn.ez_buzz.AfterCallActivity;
import com.ameba.ggn.ez_buzz.realmUtills.SaveEvent;

import java.util.Date;
import java.util.HashMap;

public class CallEndBroadCastReceiver extends BroadcastReceiver
{

    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date    callStartTime;
    private static boolean isIncoming;
    private static String savedNumber = "";


    @Override
    public void onReceive(Context context, Intent intent)
    {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
        {
            if (intent.getExtras().getString("android.intent.extra.PHONE_NUMBER") != null)
            {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            }

        }
        else
        {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
//            String number = "";
//            savedNumber=intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);


            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                state = TelephonyManager.CALL_STATE_IDLE;

                if (intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER) != null)
                {
                    savedNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                }


            }
            else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                state = TelephonyManager.CALL_STATE_OFFHOOK;

                if (intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER) != null)
                {
                    savedNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                }
            }
            else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                state = TelephonyManager.CALL_STATE_RINGING;

                if (intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER) != null)
                {
                    savedNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                }
            }


            if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()))
            {

                if (intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER) != null)
                {
                    savedNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                }
            }

            onCallStateChanged(context, state, savedNumber);
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start)
    {
//        Toast.makeText(ctx,"incoming",Toast.LENGTH_LONG).show();
        openAddAlarm(ctx, number);
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
//        Toast.makeText(ctx,"incomingS",Toast.LENGTH_LONG).show();
        openAddAlarm(ctx, number);
    }

    protected void onIncomingCallEnded(Context ctx, String number, boolean show)
    {
//        Toast.makeText(ctx,"incomingEnd",Toast.LENGTH_LONG).show();
        if (show)
        {
            openAddAlarm(ctx, number);
        }

    }


    private void openAddAlarm(Context ctx, String number)
    {
        Intent addTaskIntent = new Intent(ctx, AfterCallActivity.class);
        addTaskIntent.putExtra(GlobalConstantsG.PHONE, number);
        addTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(addTaskIntent);
    }


    protected void onOutgoingCallEnded(Context ctx, String number, boolean show)
    {
        if (show)
        {
            openAddAlarm(ctx, number);
        }
    }

    protected void onMissedCall(Context ctx, String number, boolean show)
    {

        if (show)
        {
            openAddAlarm(ctx, number);
        }
    }


    //Deals with actual events
    public static boolean isCallActive(Context context)
    {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    private boolean alreadySaved = false;


    public void onCallStateChanged(Context context, int state, String number)
    {
        if (lastState == state)
        {
            //No change, debounce extras
            return;
        }
        switch (state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;

                alreadySaved = false;

                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING)
                {
                    isIncoming = false;
                    callStartTime = new Date();

                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:


                HashMap<String, Boolean> dataChkBox = PrefHelper.getPopupFor(context);
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING)
                {
                    if (!alreadySaved)
                    {
                        alreadySaved = true;
                        SaveEvent.MISSEDCALL.setData(context, System.currentTimeMillis(), savedNumber);
                    }
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, dataChkBox.get(PrefHelper.missed_call));
                }
                else if (isIncoming)
                {
                    onIncomingCallEnded(context, savedNumber, dataChkBox.get(PrefHelper.incoming_call_end));


                    SaveEvent.INCOMINGCALL.setData(context, System.currentTimeMillis(), savedNumber);

                }
                else
                {

                    onOutgoingCallEnded(context, savedNumber, dataChkBox.get(PrefHelper.outgoing_call_end));

                    if (!alreadySaved)
                    {
                        alreadySaved = true;
                        SaveEvent.OUTGOINGCALL.setData(context, System.currentTimeMillis(), savedNumber);
                    }

                }
                break;
        }
        lastState = state;
    }
}