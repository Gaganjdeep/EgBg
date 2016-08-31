package com.ameba.ggn.ez_buzz.utillG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ameba.ggn.ez_buzz.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class DBTools extends SQLiteOpenHelper
{

    public DBTools(Context applicationContext)
    {
        super(applicationContext, "remind_me.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTasks = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "notes TEXT, " +
                "deadline TEXT, " +
                "isCompleted INTEGER DEFAULT 0," +
                "phone TEXT)";

        db.execSQL(createTasks);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop table
        db.execSQL("DROP TABLE tasks IF EXISTS");

        // Create table again
        onCreate(db);

    }

    public int addTask(HashMap<String, String> taskData)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", taskData.get("name"));
        values.put("notes", taskData.get("notes"));
        values.put("deadline", taskData.get("deadline"));
        values.put("isCompleted", taskData.get("isCompleted"));

        values.put("phone", taskData.get("phone"));

        int taskID = (int) database.insert("tasks", null, values);


        database.close();


        return taskID;
    }

    public Task getTask(int id)
    {
        Task result;

        String selectQuery = "SELECT * FROM tasks WHERE id=" + id;

        SQLiteDatabase db     = this.getReadableDatabase();
        Cursor         cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            String taskDeadline = "";
            int taskId = Integer.parseInt(cursor.getString(0));
            String taskName = cursor.getString(1);
            String taskNotes = cursor.getString(2);
            String taskPhone = cursor.getString(5);


//            DateFormat dateformat = new SimpleDateFormat(GlobalConstantsG.dateformat, Locale.ENGLISH);

            Boolean taskIsCompleted = Integer.parseInt(cursor.getString(4)) == 0 ? false : true;

            try
            {

                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(Long.valueOf(cursor.getString(3)));

//                taskDeadline = dateformat.format(dateformat.parse(cal.getTime()));
                taskDeadline = GlobalConstantsG.dateformat.format(cal.getTime());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }


            result = new Task(taskId, taskName, taskNotes, taskIsCompleted, taskDeadline, taskPhone);

            cursor.close();
            db.close();

            return result;
        }

        return null;
    }

    public int updateTask(int taskID, HashMap<String, String> taskData)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", taskData.get("name"));
        values.put("notes", taskData.get("notes"));
        values.put("deadline", taskData.get("deadline"));
        values.put("isCompleted", taskData.get("isCompleted"));

        values.put("phone", taskData.get("phone"));


        int g = database.update("tasks", values, "id" + " = ?", new String[]{taskID + ""});

        database.close();

        return g;
    }

    public int deleteTask(int taskID)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        int rowsAffected = database.delete("tasks", "id" + " = ?", new String[]{taskID + ""});
        database.close();

        return rowsAffected;
    }

    public List<Task> getAllTasks()
    {
        List<Task> tasks = new ArrayList<Task>();
        Task       result;

        //Select All Query
        String selectQuery = "SELECT * FROM tasks";

        SQLiteDatabase db     = this.getReadableDatabase();
        Cursor         cursor = db.rawQuery(selectQuery, null);

        //Loop to all rows
        if (cursor.moveToFirst())
        {
            do
            {
                String taskDeadline = "";
                int taskId = Integer.parseInt(cursor.getString(0));
                String taskName = cursor.getString(1);
                String taskNotes = cursor.getString(2);
                String taskPhone = cursor.getString(5);

//                DateFormat dateformat = new SimpleDateFormat("MMMM d, hh:mm a", Locale.ENGLISH);

                Boolean taskIsCompleted = Integer.parseInt(cursor.getString(4)) == 0 ? false : true;

                try
                {

                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(Long.valueOf(cursor.getString(3)));

//                taskDeadline = dateformat.format(dateformat.parse(cal.getTime()));
                    taskDeadline = GlobalConstantsG.dateformat.format(cal.getTime());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                result = new Task(taskId, taskName, taskNotes, taskIsCompleted, taskDeadline, taskPhone);

                tasks.add(result);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tasks;
    }


    public int getReminderCountByNumber(String Number)
    {
        //Select All Query
        try
        {
            String selectQuery = "SELECT * FROM tasks where phone='" + Number+"'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            int count = cursor.getCount();

            cursor.close();
            db.close();

            return count;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            return 0;
        }
    }


}
