package com.ameba.ggn.ez_buzz;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ameba.ggn.ez_buzz.fragments.RecentContactsFragment;

public class ContactsTab extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_tab);


        ViewPager viewPagerG = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPagerG);


        TabLayout tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.orange));
        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);


    }


    private void setupViewPager(ViewPager viewPager)
    {


// set Fragmentclass Arguments

        RecentContactsFragment recentFragment = new RecentContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("all", false);
        recentFragment.setArguments(bundle);


        RecentContactsFragment allFragment = new RecentContactsFragment();
        Bundle bundleall = new Bundle();
        bundleall.putBoolean("all", true);
        allFragment.setArguments(bundleall);


        MainActivity.Adapter adapter = new MainActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(recentFragment, "Recent Contacts");
        adapter.addFragment(allFragment, "All Contacts");
        viewPager.setAdapter(adapter);
    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewpager);

    }


}
