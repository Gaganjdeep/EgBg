package com.ameba.ggn.ez_buzz.utillG;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.ameba.ggn.ez_buzz.R;

/**
 * Created by gagandeep on 27 Jan 2016.
 */
public class UtillContacts
{
    //    ================================
//    public static List<ContactsModel> getRecentContacts(Context context)
//    {
//
//        List<ContactsModel> list = new ArrayList<>();
//
//
//        Uri queryUri = android.provider.CallLog.Calls.CONTENT_URI;
//
//        String[] projection = new String[]{
//                ContactsContract.Contacts._ID,
//                CallLog.Calls._ID,
//                CallLog.Calls.NUMBER,
//                CallLog.Calls.CACHED_NAME,
//                CallLog.Calls.DATE};
//
//        String sortOrder = String.format("%s limit 150 ", CallLog.Calls.DATE + " DESC");
//
//
//        Cursor cursor = context.getContentResolver().query(queryUri, projection, null, null, sortOrder);
//
//
//        while (cursor.moveToNext())
//        {
//            String phoneNumber = cursor.getString(cursor
//                    .getColumnIndex(CallLog.Calls.NUMBER));
//
//            String title = (cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
//
//            ContactsModel contactsModel = new ContactsModel(title == null ? "Unknown" : title, phoneNumber);
//
//            if (list.contains(contactsModel))
//            {
//                continue;
//            }
//
//            boolean alreadyadded = false;
//            for (int i = 0; i < list.size(); i++)
//            {
//                if (list.get(i).getPhoneNumber().equals(contactsModel.getPhoneNumber()))
//                {
//                    alreadyadded = true;
//                    break;
//
//                }
//            }
//
//
//            if (!alreadyadded)
//            {
//                list.add(contactsModel);
//            }
//
//        }
//        cursor.close();
//
//        return list;
//    }
//    ================================


//    public static List<ContactsModel> getAllContacts(final Context context)
//    {


//    }


    public static String GetCountryZipCode(Context context)
    {
        String CountryZipCode = "";
        try
        {
            String CountryID = "";
            CountryZipCode = "";

            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //getNetworkCountryIso
            CountryID = manager.getSimCountryIso().toUpperCase();
            String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
            for (int i = 0; i < rl.length; i++)
            {
                String[] g = rl[i].split(",");
                if (g[1].trim().equals(CountryID.trim()))
                {
                    CountryZipCode = g[0];
                    break;
                }
            }

            if (CountryZipCode.equals(""))
            {
                CountryZipCode = "91";
            }
        }
        catch (Exception e)
        {
            CountryZipCode = "91";
            e.printStackTrace();
        }

        return "+" + CountryZipCode;
    }


    public static String getContactName(Context context, String number)
    {
        String name = null;
        try
        {
            name = null;
            // define the columns I want the query to return
            String[] projection = new String[]{
                    ContactsContract.PhoneLookup.DISPLAY_NAME,
                    ContactsContract.PhoneLookup._ID};


            // encode the phone number and build the filter URI
            Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

            // query time
            Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }
                else
                {
                    return "unknown";
                }
                cursor.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "unknown";
        }
        return name;
    }

    public static void addContact(Context context, String phoneNumber)
    {
        Intent intent = new Intent(
                ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                Uri.parse("tel:" + phoneNumber));
        intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
        context.startActivity(intent);
    }

}
