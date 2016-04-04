package com.ameba.ggn.ez_buzz.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.ameba.ggn.ez_buzz.utillG.CallType;

/**
 * Created by gagandeep on 27 Jan 2016.
 */
public class ContactsModel implements Parcelable
{
    private String Name, PhoneNumber;


    private CallType callType;


    public ContactsModel(String name, String phoneNumber, CallType callType)
    {
        Name = name;
        PhoneNumber = phoneNumber;
        this.callType = callType;
    }


    public CallType getCallType()
    {
        return callType;
    }

    public String getName()
    {
        return Name;
    }

    public String getPhoneNumber()
    {
        return PhoneNumber;
    }


    protected ContactsModel(Parcel in)
    {
        Name = in.readString();
        PhoneNumber = in.readString();

    }

    public static final Creator<ContactsModel> CREATOR = new Creator<ContactsModel>()
    {
        @Override
        public ContactsModel createFromParcel(Parcel in)
        {
            return new ContactsModel(in);
        }

        @Override
        public ContactsModel[] newArray(int size)
        {
            return new ContactsModel[size];
        }
    };


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(Name);
        parcel.writeString(PhoneNumber);
    }
}
