package com.ameba.ggn.ez_buzz.utillG;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ameba.ggn.ez_buzz.MainActivity;
import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gagandeep on 22 Dec 2015.
 */
public class Utills
{


    public static Toast toast;


    public static void showToast(String msg, Context context, boolean center)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (center)
        {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }

        toast.show();

    }


    //dialog onw button
    public static Dialog global_dialog;

    public static void show_dialog_msg(final Context con, String text, View.OnClickListener onClickListener)
    {
        global_dialog = new Dialog(con, R.style.Theme_Dialog);
        global_dialog.setContentView(R.layout.dialog_global);
        global_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tex    = (TextView) global_dialog.findViewById(R.id.text);
        Button   ok     = (Button) global_dialog.findViewById(R.id.ok);
        Button   cancel = (Button) global_dialog.findViewById(R.id.cancel);


        tex.setText(text);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(global_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        global_dialog.show();
        global_dialog.getWindow().setAttributes(lp);


        if (onClickListener != null)
        {
            cancel.setVisibility(View.VISIBLE);
            // ok.setOnClickListener(oc);
            cancel.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    global_dialog.dismiss();

                }
            });


            ok.setOnClickListener(onClickListener);

        }
        else
        {
            ok.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    global_dialog.dismiss();

                }
            });
        }


    }

    public static void transitionToActivity(Activity activity, Class target, View logo, String transitionName)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent i = new Intent(activity, target);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                    android.util.Pair.create(logo, transitionName));
            activity.startActivity(i, options.toBundle());

        }
        else
        {
            activity.startActivity(new Intent(activity, target));
        }
    }

    public static void copyStream(InputStream input, OutputStream output)
    {

        try
        {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1)
            {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void setALarm(Context context, Task taskAlarm)
    {
        AlarmManager mgrAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("data", taskAlarm);

//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskAlarm.getId(), intent, 0);
//
//            mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, Long.valueOf(taskAlarm.getDeadline()), pendingIntent);


        // set the alarm for particular time
        mgrAlarm.set(AlarmManager.RTC_WAKEUP, Long.valueOf(taskAlarm.getDeadline()), PendingIntent.getBroadcast(context, taskAlarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    static Intent intentG;



    public static void tellUserDialog(final Context con, final String phoneNumber, final CallbackG<Intent> callBack)
    {
        final Dialog dialog = new Dialog(con, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_sendmsg);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        FloatingActionButton btnWhatsAppMsg = (FloatingActionButton) dialog.findViewById(R.id.btnWhatsAppMsg);
        FloatingActionButton btnMsg         = (FloatingActionButton) dialog.findViewById(R.id.btnMsg);

        Button       btnCancel     = (Button) dialog.findViewById(R.id.btnCancel);
        LinearLayout layoutMsgBtns = (LinearLayout) dialog.findViewById(R.id.layoutMsgBtns);

        intentG = null;

        for (int i = 0; i < layoutMsgBtns.getChildCount(); i++)
        {
            View rowView = layoutMsgBtns.getChildAt(i);
            rowView.animate().setStartDelay(i * 200)
                    .scaleX(1).scaleY(1);
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);


        btnWhatsAppMsg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();

                intentG = sendWhatsAppMsg(con, phoneNumber);

            }
        });


        btnMsg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();

                intentG = sendMSG(con, phoneNumber, "I have set reminder to call you.Use Ez-buzz to explore more.");
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                callBack.onFinishG(intentG);
            }
        });


    }


    public static Intent sendWhatsAppMsg(Context context, String phoneNumber)
    {

        Intent i = null;
        try
        {
            if (phoneNumber.equals("Reminder"))
            {
                return null;
            }

            if (isPackageExisted(context, "com.whatsapp"))
            {
                String number = phoneNumber;

                if (!number.startsWith("+"))
                {
                    if (number.startsWith("0"))
                    {
                        number = UtillContacts.GetCountryZipCode(context) + phoneNumber.substring(1);
                    }
                    else
                    {
                        number = UtillContacts.GetCountryZipCode(context) + phoneNumber;
                    }

                }


                Uri uri = Uri.parse("smsto:" + number);
                i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
//                i.putExtra(Intent.EXTRA_TEXT, "I'm the body.");

                if (!(context instanceof MainActivity))
                {
                    context.startActivity(Intent.createChooser(i, ""));
                }

            }
            else
            {
                Utills.showToast("Whats app is not installed..!", context, true);
            }
        }
        catch (Exception e)
        {
            Utills.showToast("Whats app not installed..!", context, true);
            e.printStackTrace();
        }

        return i;
    }


    public static Intent sendMSG(Context context, String number, String msg)
    {

        Intent intent = null;
        if (number.equals("Reminder"))
        {
            return intent;
        }

        Uri uri = Uri.parse("smsto:" + number);
        intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", msg);

        if (!(context instanceof MainActivity))
        {
            context.startActivity(Intent.createChooser(intent, "Choose app to send message"));
        }


        return intent;


    }


    public static boolean isPackageExisted(Context con, String targetPackage)
    {
        PackageManager pm = con.getPackageManager();
        try
        {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
        return true;
    }


}
