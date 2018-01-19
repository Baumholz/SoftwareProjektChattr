package com.example.david.chattr.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.messaging.Message;

import java.util.ArrayList;

/**
 * Created by manu on 17.11.2017.
 */

public class ChatActivityListViewAdapter extends BaseAdapter{


    private ArrayList<Message> recipients ;
    private String recipientNR;

    public ChatActivityListViewAdapter(ArrayList<Message> messages, String recipientNR) {
        this.recipients = messages;
        this.recipientNR = recipientNR;
    }

    @Override
    public int getCount() {
        return recipients.size();
    }

    @Override
    public Object getItem(int i) {
        return recipients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        Context context = viewGroup.getContext();
        Message message = recipients.get(i);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(i);

        if(recipientNR.equals(message.getRecipientNr())) {
            Log.e("ChatAdapter", "RecipientNR: "+recipientNR + "message.getRecipientNr(): " + message.getRecipientNr());
            layoutResource = R.layout.item_chat_right;
        }else{
            layoutResource = R.layout.item_chat_left;
            Log.e("ChatAdapter", "RecipientNR: "+recipientNR + "message.getRecipientNr(): " + message.getRecipientNr());
        }

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
           view = inflater.inflate(layoutResource, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.text.setText(message.getContent());

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    private class ViewHolder {
        private TextView text;

        public ViewHolder(View v) {
            text = (TextView) v.findViewById(R.id.text);
        }
    }

}
