package com.ameba.ggn.ez_buzz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ameba.ggn.ez_buzz.fragments.MemoListFragment;

public class ContainerActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        setUptoolBar();


        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, new MemoListFragment()).commit();


    }

    private void setUptoolBar()
    {
        Toolbar  toolbar       = (Toolbar) findViewById(R.id.toolbar);
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        try
        {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setTitle("Memo List");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
