package com.ameba.ggn.ez_buzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.adapter.SuperAdapterG;
import com.ameba.ggn.ez_buzz.realmUtills.ContactInfo;
import com.ameba.ggn.ez_buzz.realmUtills.RealmHelperG;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemoListFragment extends Fragment
{


    public MemoListFragment()
    {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memo_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new SuperAdapterG<ContactInfo, viewHolder>(RealmHelperG.getInstance(getActivity()).GET_ALL_MEMOS(), R.layout.memo_inflator, viewHolder.class)
        {
            @Override
            protected void populateViewHolderG(viewHolder holder, ContactInfo model, int position)
            {
                String name = model.getName();

                holder.txtvPhoneNo.setText(model.getNumber());

                holder.txtvNotes.setText(model.getMemo());
                holder.txtvTitle.setText(name.equals("unknown") ? model.getNumber() : name);

                try
                {
                    holder.txtvTime.setText(new SimpleDateFormat("MMM dd, hh:mm aa").format(new Date(Long.parseLong(model.getLastCallTime()))));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        });

        return view;
    }


    public static class viewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtvNotes, txtvTime, txtvPhoneNo, txtvTitle;
        private View view;

        public viewHolder(View itemView)
        {
            super(itemView);
            txtvTitle = (TextView) itemView.findViewById(R.id.txtvTitle);
            txtvNotes = (TextView) itemView.findViewById(R.id.txtvNotes);
            txtvTime = (TextView) itemView.findViewById(R.id.txtvTime);
            txtvPhoneNo = (TextView) itemView.findViewById(R.id.txtvPhoneNo);

            view = itemView;
        }
    }


}