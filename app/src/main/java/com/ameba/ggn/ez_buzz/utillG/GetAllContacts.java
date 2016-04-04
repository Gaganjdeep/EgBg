package com.ameba.ggn.ez_buzz.utillG;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
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


        List<ContactsModel> list = new ArrayList<>();

        ContentResolver cr = con.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext())
                    {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Toast.makeText(NativeContentProvider.this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                        ContactsModel contactsModel = new ContactsModel(name, phoneNo, CallType.ALLCALLS);
                        list.add(contactsModel);
                    }
                    pCur.close();
                }
            }
        }


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
