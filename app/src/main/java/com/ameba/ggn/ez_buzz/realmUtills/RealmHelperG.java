package com.ameba.ggn.ez_buzz.realmUtills;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by GaganDroid on 31 Aug 2016.
 */
public class RealmHelperG
{
    private static RealmHelperG ourInstance = new RealmHelperG();

    public static RealmHelperG getInstance(Context context)
    {
        if (realmConfig == null)
        {
            realmConfig = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build();
        }

        return ourInstance;
    }

    private RealmHelperG()
    {
    }

    public static RealmConfiguration realmConfig;


    public void SAVE_MEMO(final ContactInfo contactInfo)
    {
        try
        {
            final Realm realm = Realm.getInstance(realmConfig);

            realm.executeTransaction(new Realm.Transaction()
            {
                @Override
                public void execute(Realm realmG)
                {
                    ContactInfo contactInfo1;
                    try
                    {
                        contactInfo1 = realmG.where(ContactInfo.class).equalTo("Number", contactInfo.getNumber()).findFirst();
                    }
                    catch (Exception e)
                    {
                        contactInfo1 = null;
                        e.printStackTrace();
                    }
                    if (contactInfo1 == null)
                    {
                        contactInfo1 = realmG.createObject(ContactInfo.class);//insert new to database
                    }
                    contactInfo1.setNumber(contactInfo.getNumber());
                    contactInfo1.setLastCallTime(contactInfo.getLastCallTime());
                    contactInfo1.setMemo(contactInfo.getMemo());
                    contactInfo1.setName(contactInfo.getName());
                }
            });
        }
        catch (Exception | Error e)
        {
            e.printStackTrace();
        }
    }


    public ContactInfo GET_MEMO_FOR_NUMBER(String number)
    {
        final Realm realm = Realm.getInstance(realmConfig);
        return realm.where(ContactInfo.class).equalTo("Number", number).findFirst();
    }


    public List<ContactInfo> GET_ALL_MEMOS()
    {
        final Realm               realm            = Realm.getInstance(realmConfig);
        RealmResults<ContactInfo> infoRealmResults = realm.where(ContactInfo.class).findAll();
        List<ContactInfo>         contactInfoList  = realm.copyFromRealm(infoRealmResults);
        return contactInfoList;
    }


    public void DELETE_MEMO(final String phoneNumber)
    {
        final Realm realm = Realm.getInstance(realmConfig);
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realmG)
            {
                (realmG.where(ContactInfo.class).equalTo("Number", phoneNumber).findFirst()).removeFromRealm();
            }
        });
    }


    public List<EventInfo> GET_EVENTS()
    {
        final Realm realm = Realm.getInstance(realmConfig);
        return (realm.where(EventInfo.class).findAllSorted("time", Sort.DESCENDING));
    }

    public void DELETE_ALL_EVENTS()
    {
        final Realm realm = Realm.getInstance(realmConfig);
        realm.beginTransaction();
        realm.where(EventInfo.class).findAll().clear();
        realm.commitTransaction();
    }


    public void SAVE_EVENT(final EventInfo eventInfo)
    {
        try
        {
            final Realm realm = Realm.getInstance(realmConfig);

            realm.executeTransaction(new Realm.Transaction()
            {
                @Override
                public void execute(Realm realmG)
                {
                    RealmResults<EventInfo> eventInfoRealmResults = realmG.where(EventInfo.class).findAllSorted("time", Sort.DESCENDING);


                    if (eventInfoRealmResults != null && eventInfoRealmResults.size() > 0)
                    {
                        if (eventInfoRealmResults.size() > 4)
                        {
                            eventInfoRealmResults.removeLast();
                        }
                    }

                    EventInfo contactInfo1;

                    contactInfo1 = realmG.createObject(EventInfo.class);//insert new to database

                    contactInfo1.setPhonenumber(eventInfo.getPhonenumber());
                    contactInfo1.setTime(eventInfo.getTime());
                    contactInfo1.setEvent_type(eventInfo.getEvent_type());
                    contactInfo1.setName(eventInfo.getName());
                }
            });
        }
        catch (Exception | Error e)
        {
            e.printStackTrace();
        }
    }


}
