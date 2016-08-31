package com.ameba.ggn.ez_buzz.realmUtills;

import io.realm.RealmObject;

/**
 * Created by GaganDroid on 31 Aug 2016.
 */
public class EventInfo extends RealmObject
{
    private String phonenumber;


    private long time;
    private String name;
    private String event_type;


    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEvent_type()
    {
        return event_type;
    }

    public void setEvent_type(String event_type)
    {
        this.event_type = event_type;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }
}
