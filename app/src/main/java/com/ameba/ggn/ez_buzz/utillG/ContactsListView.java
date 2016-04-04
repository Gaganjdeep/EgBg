package com.ameba.ggn.ez_buzz.utillG;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ameba.ggn.ez_buzz.adapter.ContactsAdapter;
import com.ameba.ggn.ez_buzz.adapter.ContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gagandeep on 27 Jan 2016.
 */
public class ContactsListView extends RecyclerView
{
    private boolean mScrollable;

    public ContactsListView(Context context)
    {
        super(context);
        mScrollable = false;
    }

    public ContactsListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mScrollable = false;
    }

    public ContactsListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mScrollable = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        return !mScrollable || super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++)
        {
            animate(getChildAt(i), i);

            if (i == getChildCount() - 1)
            {
                getHandler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mScrollable = true;
                    }
                }, i * 100);
            }
        }
    }

    private void animate(View view, final int pos)
    {
        view.animate().cancel();
        view.setTranslationY(100);
        view.setAlpha(0);
        view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(pos * 100);
    }

    public void showAllContacts(final Context context, final SearchView searchView)
    {
        this.setLayoutManager(new LinearLayoutManager(context));


        new GetAllContacts(context, new CallBack()
        {
            @Override
            public void onFinish(String output)
            {

            }

            @Override
            public void getContactList(final List<ContactsModel> outputList)
            {

                ContactsAdapter adapter = new ContactsAdapter(context, outputList);
                setAdapter(adapter);


                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextSubmit(String s)
                    {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s)
                    {
                        List<ContactsModel> list = new ArrayList<ContactsModel>();
                        list.clear();


                        for (ContactsModel contactsModel : outputList)
                        {
                            if (contactsModel.getName().toUpperCase().contains(s.toUpperCase()))
                            {
                                list.add(contactsModel);
                            }
                        }

                        ContactsAdapter adapter = new ContactsAdapter(context, list);
                        setAdapter(adapter);


                        return false;
                    }
                });


            }
        }).execute();


    }


    public void showRecentContacts(final Context context, final SearchView searchView)
    {
        this.setLayoutManager(new LinearLayoutManager(context));


        new GetRecentContacts(context, new CallBack()
        {
            @Override
            public void onFinish(String output)
            {

            }

            @Override
            public void getContactList(final List<ContactsModel> outputList)
            {

                ContactsAdapter adapter = new ContactsAdapter(context, outputList);
                setAdapter(adapter);


                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextSubmit(String s)
                    {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s)
                    {
                        List<ContactsModel> list = new ArrayList<ContactsModel>();
                        list.clear();


                        for (ContactsModel contactsModel : outputList)
                        {
                            if (contactsModel.getName().toUpperCase().contains(s.toUpperCase()))
                            {
                                list.add(contactsModel);
                            }
                        }

                        ContactsAdapter adapter = new ContactsAdapter(context, list);
                        setAdapter(adapter);


                        return false;
                    }
                });


            }
        }).execute();


    }
}
