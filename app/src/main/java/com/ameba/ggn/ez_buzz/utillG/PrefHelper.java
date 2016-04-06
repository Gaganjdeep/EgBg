package com.ameba.ggn.ez_buzz.utillG;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by gagandeep on 19 Jan 2016.
 */
public class PrefHelper
{

    static final public String PREF_NAME          = "EasyBeezeePref";
    static final public String popup_dismiss_time = "popup_dismiss_time";
    static final public String all_call           = "all_call";
    static final public String incoming_call_end  = "incoming_call_end";
    static final public String outgoing_call_end  = "outgoing_call_end";
    static final public String missed_call        = "missed_call";
    static final public String call_rejected      = "call_rejected";

    static final public String SOUND_ALERT = "sound_alert";


    public static long getPopupDismissTime(Context con)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            return shrdPref.getLong(popup_dismiss_time, 5);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;

        }
    }


    public static void setPopupDismissTime(Context con, String dismissTime)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor ed = shrdPref.edit();

            ed.putLong(popup_dismiss_time, Long.parseLong(dismissTime));
            ed.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static boolean getSoundAlert(Context con)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            return shrdPref.getBoolean(SOUND_ALERT, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return true;

        }

    }


    public static void setSoundAlert(Context con, boolean soundAlert)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor ed = shrdPref.edit();

            ed.putBoolean(SOUND_ALERT, soundAlert);
            ed.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static void setPopupFor(Context con, boolean all_call_, boolean incoming_call_end_, boolean outgoing_call_end_, boolean missed_call_, boolean call_rejected_)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor ed = shrdPref.edit();

            ed.putBoolean(all_call, all_call_);
            ed.putBoolean(incoming_call_end, incoming_call_end_);
            ed.putBoolean(outgoing_call_end, outgoing_call_end_);
            ed.putBoolean(missed_call, missed_call_);
            ed.putBoolean(call_rejected, call_rejected_);

            ed.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static HashMap<String, Boolean> getPopupFor(Context con)
    {
        HashMap<String, Boolean> dataChkBox = new HashMap<>();
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            dataChkBox.put(all_call, shrdPref.getBoolean(all_call, false));
            dataChkBox.put(incoming_call_end, shrdPref.getBoolean(incoming_call_end, true));
            dataChkBox.put(outgoing_call_end, shrdPref.getBoolean(outgoing_call_end, true));
            dataChkBox.put(missed_call, shrdPref.getBoolean(missed_call, true));
            dataChkBox.put(call_rejected, shrdPref.getBoolean(call_rejected, true));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return dataChkBox;
    }


//    ===============is first time open


    public static boolean isFirstTime(Context con)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            return shrdPref.getBoolean(isFirstTime, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return true;

        }

    }

    static final String isFirstTime = "isFirstOpen";

    public static void setFirstTime(Context con, boolean isfirst)
    {
        try
        {
            SharedPreferences shrdPref = con.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor ed = shrdPref.edit();

            ed.putBoolean(isFirstTime, isfirst);
            ed.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


//    ===============is first time open end

}
