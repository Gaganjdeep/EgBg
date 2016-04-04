package com.ameba.ggn.ez_buzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.utillG.ContactsListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentContactsFragment extends Fragment
{

    public RecentContactsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent_contacts, container, false);


        ContactsListView listViewContacts = (ContactsListView) v.findViewById(R.id.listViewContacts);


        SearchView  searchView = (SearchView)v.findViewById(R.id.searchView);



        if (getArguments().getBoolean("all"))
        {
            listViewContacts.showAllContacts(getActivity(),searchView);
        }
        else
        {
            listViewContacts.showRecentContacts(getActivity(),searchView);
        }




        return v;
    }

}
