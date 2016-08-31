package com.ameba.ggn.ez_buzz.utillG;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.ameba.ggn.ez_buzz.adapter.ContactsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gagandeep on 26 Feb 2016.
 */

public class GetRecentContacts extends AsyncTask<Void, Void, List<ContactsModel>>
{
    Context  con;
    CallBack callBack;

    public GetRecentContacts(Context con, CallBack callBack)
    {
        this.con = con;
        this.callBack = callBack;
    }

    @Override
    protected List<ContactsModel> doInBackground(Void... voids)
    {


        List<ContactsModel> list = new ArrayList<>();


        Uri queryUri = android.provider.CallLog.Calls.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                CallLog.Calls._ID,
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE};

        String sortOrder = String.format("%s limit 150 ", CallLog.Calls.DATE + " DESC");


        Cursor cursor = con.getContentResolver().query(queryUri, projection, null, null, sortOrder);


        while (cursor.moveToNext())
        {
            String phoneNumber = cursor.getString(cursor
                    .getColumnIndex(CallLog.Calls.NUMBER));

            String title = (cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));

            String TYPE = (cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));

            CallType callType = TYPE.equals("1") ? CallType.INCOMING : TYPE.equals("2") ? CallType.OUTGOING : CallType.MISSCALL;

            ContactsModel contactsModel = new ContactsModel(title == null ? "Unknown" : title, phoneNumber, callType);

            if (list.contains(contactsModel))
            {
                continue;
            }

            boolean alreadyadded = false;
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).getPhoneNumber().equals(contactsModel.getPhoneNumber()))
                {
                    alreadyadded = true;
                    break;

                }
            }


            if (!alreadyadded)
            {
                list.add(contactsModel);
            }

        }
        cursor.close();


        //Sort Entries
//        Collections.sort(list, new Comparator<ContactsModel>()
//        {
//            @Override
//            public int compare(ContactsModel t1, ContactsModel t2)
//            {
//                return (t1.getName()).compareToIgnoreCase(t2.getName());
//            }
//        });
//


        return list;

    }

    @Override
    protected void onPostExecute(List<ContactsModel> contactsModels)
    {
//        LinkedHashSet<ContactsModel> setModel = new LinkedHashSet<>(contactsModels);

        callBack.getContactList(new ArrayList<>(contactsModels));


        super.onPostExecute(contactsModels);
    }
}