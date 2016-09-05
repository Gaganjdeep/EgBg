package com.ameba.ggn.ez_buzz;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import com.ameba.ggn.ez_buzz.fragments.MemoListFragment;

public class ContainerActivity extends AppCompatActivity
{
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            animate();
        }
        else
        {
            toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        }
        setUptoolBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, new MemoListFragment()).commit();
    }


    private void animate()
    {
        getWindow().getEnterTransition().addListener(new Transition.TransitionListener()
        {
            @Override
            public void onTransitionStart(Transition transition)
            {
            }

            @Override
            public void onTransitionEnd(Transition transition)
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
                animateRevealShow(toolbar);
            }
            @Override
            public void onTransitionCancel(Transition transition)
            {
            }

            @Override
            public void onTransitionPause(Transition transition)
            {
            }

            @Override
            public void onTransitionResume(Transition transition)
            {
            }
        });

    }


    private void animateRevealShow(View viewRoot)
    {
        int cx          = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy          = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    private void setUptoolBar()
    {
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
