package com.ameba.ggn.ez_buzz.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.adapter.SuperAdapterG;
import com.ameba.ggn.ez_buzz.realmUtills.ContactInfo;
import com.ameba.ggn.ez_buzz.realmUtills.RealmHelperG;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


    private List<ContactInfo> contactInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memo_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        contactInfoList = RealmHelperG.getInstance(getActivity()).GET_ALL_MEMOS();


        recyclerView.setAdapter(new SuperAdapterG<ContactInfo, viewHolder>(contactInfoList, R.layout.memo_inflator, viewHolder.class)
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

                holder.view.setTag(model);
                holder.view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View view)
                    {
                        final ContactInfo contactInfo = (ContactInfo) view.getTag();

                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                        alertDialog.setTitle("Memo");

                        alertDialog.setMessage("Edit or Delete memo for " + contactInfo.getNumber() + " ?");

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int id)
                            {
                                alertDialog.dismiss();
                                RealmHelperG.getInstance(getActivity()).DELETE_MEMO(contactInfo.getNumber());
                                contactInfoList.remove(contactInfo);
                                notifyDataSetChanged();
                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Edit", new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int id)
                            {
                                alertDialog.dismiss();


                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog_memo);
                                builder.setTitle("Edit memo");
                                builder.setMessage("Edit memo for " + contactInfo.getNumber());
                                final EditText ed = new EditText(getActivity());
                                ed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                ed.setHint("Enter memo here..");
                                ed.setText(contactInfo.getMemo());
                                ed.setTextColor(getResources().getColor(R.color.orange));
                                ed.setHintTextColor(getResources().getColor(R.color.extremelt_grey));

                                builder.setView(ed);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
//                                        tvMemo.setText(ed.getText().toString());

                                        contactInfo.setMemo(ed.getText().toString());

                                        ContactInfo contactInfo1 = new ContactInfo();
                                        contactInfo1.setName(contactInfo.getName());
                                        contactInfo1.setMemo(ed.getText().toString());
                                        contactInfo1.setLastCallTime(System.currentTimeMillis() + "");
                                        contactInfo1.setNumber(contactInfo.getNumber());

                                        RealmHelperG.getInstance(getActivity()).SAVE_MEMO(contactInfo1);


                                        notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("Cancel", null);

                                builder.show();


                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int id)
                            {
                                alertDialog.cancel();

                            }
                        });


                        alertDialog.show();
                    }
                });


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
