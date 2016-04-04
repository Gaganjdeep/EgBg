package com.ameba.ggn.ez_buzz;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ameba.ggn.ez_buzz.fragments.AddTaskFragment;
import com.ameba.ggn.ez_buzz.utillG.GlobalConstantsG;

public class AfterCallAddReminder extends AppCompatActivity
{
    AddTaskFragment addTaskFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_call_add_reminder);
        /*this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.6f;*/

        addTaskFragment = new AddTaskFragment();
        addTaskFragment.setNumber(getIntent().getStringExtra(GlobalConstantsG.PHONE));


        displayView(addTaskFragment);



    }



    public void tenMin(View view)
    {
        addTaskFragment.tenMin(view);
    }

    public void thirtyMin(View view)
    {
        addTaskFragment.thirtyMin(view);
    }

    public void sixtyMin(View view)
    {
        addTaskFragment.sixtyMin(view);
    }

    public void oneTwentyMin(View view)
    {
        addTaskFragment.oneTwentyMin(view);
    }


    public void threeHours(View view)
    {
        addTaskFragment.threeHours(view);
    }

    public void sixHours(View view)
    {
        addTaskFragment.sixHours(view);
    }

    public void twelveHours(View view)
    {
        addTaskFragment.twelveHours(view);
    }

    public void adayHours(View view)
    {
        addTaskFragment.adayHours(view);
    }

    public void selectDate(View view)
    {
        addTaskFragment.selectDate(view);
    }


    private void displayView(android.support.v4.app.Fragment fragment)
    {


        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.parentlayout, fragment).commit();

        }

    }
}
