package com.ameba.ggn.ez_buzz.realmUtills;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by GaganDroid on 31 Aug 2016.
 */
public class ContactInfo extends RealmObject
{
    @PrimaryKey
    private String Number;


    private String Memo;
    private String Name;
    private String LastCallTime;


    public String getNumber()
    {
        return Number;
    }

    public void setNumber(String number)
    {
        Number = number;
    }

    public String getMemo()
    {
        return Memo;
    }

    public void setMemo(String memo)
    {
        Memo = memo;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getLastCallTime()
    {
        return LastCallTime;
    }

    public void setLastCallTime(String lastCallTime)
    {
        LastCallTime = lastCallTime;
    }
}
