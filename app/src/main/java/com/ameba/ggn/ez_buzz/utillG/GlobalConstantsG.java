package com.ameba.ggn.ez_buzz.utillG;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by gagandeep on 22 Dec 2015.
 */
public class GlobalConstantsG
{

    public static String NAME = "name", NOTES = "notes", DEADLINE = "deadline", ISCOMPLETED = "isCompleted", PHONE = "phone";

    public static DateFormat dateformat = new SimpleDateFormat("MMM d, hh:mm a", Locale.ENGLISH);


    final public static int REQUESTCODE_CAMERA = 11;
    final public static int REQUESTCODE_GALLERY = 22;
}
