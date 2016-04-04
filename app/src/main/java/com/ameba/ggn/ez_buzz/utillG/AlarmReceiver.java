package com.ameba.ggn.ez_buzz.utillG;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.ameba.ggn.ez_buzz.AlarmActivity;
import com.ameba.ggn.ez_buzz.MainActivity;
import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.Task;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by gagandeep on 22 Dec 2015.
 */
public class AlarmReceiver extends BroadcastReceiver
{


    private void showNotification(Context context, String message, Task taskData)
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_top_notification);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);

//        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_img));
        if (!taskData.getImage().isEmpty())
        {

            try
            {
                Uri imageUri = Uri.parse(taskData.getImage());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
//        }
                builder.setLargeIcon(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_img));
        }

        builder.setContentTitle("Reminder");
        builder.setContentText(message);
//        builder.setSubText("Tap to open EasyBeezee and set reminders..!");


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (PrefHelper.getSoundAlert(context))
        {
            if (defaultSound == null)
            {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                if (defaultSound == null)
                {
                    defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                }
            }
        }
        else
        {
            defaultSound = null;
        }
        builder.setSound(defaultSound);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(taskData.getId(), builder.build());
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Task taskData = (Task) intent.getSerializableExtra("data");

        dbHelper = new DBTools(context);
        if (dbHelper.getTask(taskData.getId()) == null)
        {
            return;
        }


        showNotification(context, "Reminder for " + taskData.getPhonenumber(), taskData);

        openAlarmActivity(context, taskData);
    }

    DBTools dbHelper;

    private void openAlarmActivity(Context ctx, Task taskData)
    {

        if (dbHelper == null)
        {
            dbHelper = new DBTools(ctx);
        }
        try
        {
//            Calendar cal=new GregorianCalendar();
//            cal.setTime(GlobalConstantsG.dateformat.parse(taskData.getDeadline()));
            //Put Data to HashMaps


            HashMap<String, String> taskDataHashMap = new HashMap<String, String>();
            taskDataHashMap.put("name", taskData.getImage());
            taskDataHashMap.put("notes", taskData.getNotes());
            taskDataHashMap.put("deadline", taskData.getDeadline() + "");
            taskDataHashMap.put("isCompleted", "1");
            taskDataHashMap.put("phone", taskData.getPhonenumber());


            dbHelper.updateTask(taskData.getId(), taskDataHashMap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Intent addTaskIntent = new Intent(ctx, AlarmActivity.class);
        addTaskIntent.putExtra("data", taskData);
        addTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(addTaskIntent);
    }


}
