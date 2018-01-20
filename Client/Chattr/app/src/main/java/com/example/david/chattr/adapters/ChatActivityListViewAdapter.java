package com.example.david.chattr.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.messaging.Message;

import java.util.ArrayList;

/**
 * Created by manu on 17.11.2017.
 */

public class ChatActivityListViewAdapter extends BaseAdapter{


    private ArrayList<Message> messages ;
    private String recipientNR;

    public ChatActivityListViewAdapter(ArrayList<Message> messages, String recipientNR) {
        this.messages = messages;
        this.recipientNR = recipientNR;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        Context context = viewGroup.getContext();
        Message message = messages.get(i);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(i);

        if(recipientNR.equals(message.getRecipientNr())) {
            Log.e("Option 1", "RecipientNR: "+recipientNR + "message.getRecipientNr(): " + message.getRecipientNr());
            layoutResource = R.layout.item_chat_right;
        }else{
            layoutResource = R.layout.item_chat_left;
            Log.e("Option 2", "RecipientNR: "+recipientNR + "message.getRecipientNr(): " + message.getRecipientNr());
        }


           view = inflater.inflate(layoutResource, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);


      TextView textView = view.findViewById(R.id.text);
       textView.setText(message.getContent());
     //   holder.text.setText(message.getContent());

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
