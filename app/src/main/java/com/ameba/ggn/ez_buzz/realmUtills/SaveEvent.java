package com.ameba.ggn.ez_buzz.realmUtills;

import android.content.Context;

import com.ameba.ggn.ez_buzz.utillG.UtillContacts;

/**
 * Created by GaganDroid on 31 Aug 2016.
 */
public enum SaveEvent
{
    MISSEDCALL("Missed Call")
            {
                @Override
                protected void saveTORealM()
                {
                    super.saveTORealM();
                }
            },

    OUTGOINGCALL("Outgoing Call")
            {
                @Override
                protected void saveTORealM()
                {
                    super.saveTORealM();
                }
            },

    INCOMINGCALL("Incoming Call")
            {
                @Override
                protected void saveTORealM()
                {
                    super.saveTORealM();
                }
            };


    private long  timeG;
    private String  eventType;
    private String  nameG;
    private String  phoneNumberG;
    private Context context;

    SaveEvent(String eventType)
    {
        this.eventType = eventType;
    }


    protected void saveTORealM()
    {
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEvent_type(eventType);
        eventInfo.setTime(timeG);
        eventInfo.setName(nameG);
        eventInfo.setPhonenumber(phoneNumberG);

        RealmHelperG.getInstance(context).SAVE_EVENT(eventInfo);
    }


    public void setData(Context context, long time, String phoneNumber)
    {
        this.context = context;
        timeG = time;
        phoneNumberG = phoneNumber;

        nameG = UtillContacts.getContactName(context, phoneNumber);
        nameG = nameG.equals("unknown") ? phoneNumber : nameG;

        saveTORealM();
    }


}
