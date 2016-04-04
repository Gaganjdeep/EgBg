package com.ameba.ggn.ez_buzz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameba.ggn.ez_buzz.R;
import com.ameba.ggn.ez_buzz.utillG.CallType;
import com.ameba.ggn.ez_buzz.utillG.RoundedCornersGaganImg;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gagandeep on 27 Jan 2016.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;

    Context con;

    private List<ContactsModel> dataList;


    public ContactsAdapter(Context context, List<ContactsModel> dList)
    {
        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);

        callTYpeIcon = new HashMap<>();
        callTYpeIcon.put(CallType.INCOMING, android.R.drawable.sym_call_incoming);
        callTYpeIcon.put(CallType.OUTGOING, android.R.drawable.sym_call_outgoing);
        callTYpeIcon.put(CallType.MISSCALL, android.R.drawable.sym_call_missed);
    }

    @Override
    public MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.contacts_inflator, parent, false);
        return new MyViewHolderG(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final ContactsModel current = dataList.get(position);


        holder.title.setText(current.getName());
        holder.phoneNo.setText(current.getPhoneNumber());

        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data", current);

                ((Activity) con).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) con).finish();
            }
        });


        if (current.getCallType() == CallType.ALLCALLS)
        {
            holder.icon.setVisibility(View.GONE);
        }
        else
        {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(callTYpeIcon.get(current.getCallType()));
        }


    }


    HashMap<CallType, Integer> callTYpeIcon;


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView title, phoneNo;
        View                   view;
        RoundedCornersGaganImg icon;

        public MyViewHolderG(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.name);
            phoneNo = (TextView) itemView.findViewById(R.id.phone);
            icon = (RoundedCornersGaganImg) itemView.findViewById(R.id.image);
            view = itemView;
        }
    }

}
