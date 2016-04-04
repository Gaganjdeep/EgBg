package com.ameba.ggn.ez_buzz.app_intro;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.ameba.ggn.ez_buzz.MainActivity;
import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.utillG.PrefHelper;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.ArrayList;


public class ColorAnimation extends AppIntro2
{

    @Override
    public void init(@Nullable Bundle savedInstanceState)
    {

        addSlide(AppIntroFragment.newInstance("EZ-Buzz", "EZ-Buzz is easy to use app..just a single tap and ts done..!",
                R.drawable.app_logo_big, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("Set Reminder Image", Html.fromHtml("You can also set reminder for a contact Select contact from call logs or from all contacts.."),
                R.drawable.add_reminder_one, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("Select Reminder time", Html.fromHtml("<b>Select Reminder time ..In a single click..!</b>"),
                R.drawable.add_reminder, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("Reminders List", Html.fromHtml("<b>Edit or delete reminders from list.</b>"),
                R.drawable.img_reminder_list, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("Reminder Alert", "Just Tap to perform any task....!",
                R.drawable.img_after_reminder, Color.TRANSPARENT));


        ArrayList<Integer> colors = new ArrayList<>();


        colors.add(getResources().getColor(R.color.orange));

        colors.add(Color.parseColor("#FF9E9D24"));


        colors.add(Color.parseColor("#FF268B5D"));

        colors.add(Color.parseColor("#FF4377A4"));


        colors.add(getResources().getColor(R.color.back_grey));

        setAnimationColors(colors);
        showStatusBar(false);


    }

    private void loadMainActivity()
    {
        PrefHelper.setFirstTime(ColorAnimation.this, false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed()
    {
        loadMainActivity();
    }

    @Override
    public void onNextPressed()
    {

    }

    @Override
    public void onSlideChanged()
    {

    }

    public void getStarted(View v)
    {
        loadMainActivity();
    }
}
