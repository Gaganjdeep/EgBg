package com.ameba.ggn.ez_buzz.utillG;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.ameba.ggn.ez_buzz.adapter.ContactsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gagandeep on 26 Feb 2016.
 */


public class GetAllContacts extends AsyncTask<Void, Void, List<ContactsModel>>
{
    Context  con;
    CallBack callBack;

    public GetAllContacts(Context con, CallBack callBack)
    {
        this.con = con;
        this.callBack = callBack;
    }

    @Override
    protected List<ContactsModel> doInBackground(Void... voids)
    {


        List<ContactsModel> list = null;
        try
        {
            list = new ArrayList<>();


            String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            String[] projection = new String[]{
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
                    ContactsContract.Contacts._ID,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.Contacts.DISPLAY_NAME};


            ContentResolver cr = con.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, selection, null, null);


            if (cur.getCount() > 0)
            {
                while (cur.moveToNext())
                {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //                if (Integer.parseInt(cur.getString(
                    //                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    //                {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext())
                    {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        ContactsModel contactsModel = new ContactsModel(name, phoneNo, CallType.ALLCALLS);
                        list.add(contactsModel);
                    }
                    pCur.close();
                    //                }
                }
            }
            cur.close();


            //Sort Entries
            Collections.sort(list, new Comparator<ContactsModel>()
            {
                @Override
                public int compare(ContactsModel t1, ContactsModel t2)
                {
                    return (t1.getName()).compareToIgnoreCase(t2.getName());
                }
            });
//
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return list;
    }

    @Override
    protected void onPostExecute(List<ContactsModel> contactsModels)
    {
        LinkedHashSet<ContactsModel> setModel = new LinkedHashSet<>(contactsModels);

        callBack.getContactList(new ArrayList<>(setModel));


        super.onPostExecute(contactsModels);
    }
}
