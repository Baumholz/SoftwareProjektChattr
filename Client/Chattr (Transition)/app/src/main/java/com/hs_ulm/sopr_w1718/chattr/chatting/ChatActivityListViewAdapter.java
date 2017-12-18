package com.example.david.chattr.chatting;

import android.content.Context;
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
    public ChatActivityListViewAdapter(ArrayList<Message> messages) {this.recipients = messages;
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

        Context context = viewGroup.getContext();
        Message message = recipients.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_chat_list_view,null,false); //Use our Layout
        }

        TextView chatInput = view.findViewById(R.id.listViewText);
        chatInput.setText(message.getContent());
       // chatInput.setBackgroundColor(Color.GREEN);
        return view;
    }
}
