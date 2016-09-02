package com.ameba.ggn.ez_buzz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.fragments.AddTaskFragment;
import com.ameba.ggn.ez_buzz.fragments.AllReminderFrag;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setUptoolBar();


        ViewPager viewPagerG = (ViewPager) findViewById(R.id.viewpager);
        if (viewPagerG != null)
        {
            setupViewPager(viewPagerG);
        }


        TabLayout tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.orange));
        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);


        viewPagerG.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                setToolbar_title(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.settings)
        {
//            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            startActivity(new Intent(MainActivity.this, ContainerActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    String[] titles = {"All Reminders", "Add Reminder"};


    private void setUptoolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        try
        {
            setSupportActionBar(toolbar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        imgSettings = (ImageView) toolbar.findViewById(R.id.imgSettings);
    }


    private void setToolbar_title(String title)
    {
        toolbar_title.setText(title);
    }

    TextView toolbar_title;
//    ImageView imgSettings;

    AddTaskFragment addTaskFragment;

    private void setupViewPager(ViewPager viewPager)
    {

        addTaskFragment = new AddTaskFragment();
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AllReminderFrag(), "");
        adapter.addFragment(addTaskFragment, "");
        viewPager.setAdapter(adapter);
    }

    int[] tabIcons = {R.mipmap.reminder_icon, R.mipmap.add_reminder_icon};


    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setupWithViewPager(mViewpager);


        for (int i = 0; i < 2; i++)
        {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }


        //..
    }



    static class Adapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm)
        {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitles.get(position);
        }
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
    {addTaskFragment.sixHours(view);
    }

    public void twelveHours(View view)
    {addTaskFragment.twelveHours(view);
    }

    public void adayHours(View view)
    {addTaskFragment.adayHours(view);
    }

    public void selectDate(View view)
    {addTaskFragment.selectDate(view);
    }



}
