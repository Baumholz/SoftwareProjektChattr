package com.example.david.chattr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.faq.Question;

import java.util.ArrayList;

/**
 * Created by david on 28.12.17.
 */

public class FaqListAdapter extends BaseAdapter {

    private ArrayList<Question> questions;

    public FaqListAdapter(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        return questions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        Question question = questions.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.faq_list_entry,null,false); //Use our Layout
        }

        TextView questionTextView = (TextView) view.findViewById(R.id.faqQuestion);
        TextView answerTextView = (TextView) view.findViewById(R.id.faqAnswer);

        String quest = question.getQuest();
        questionTextView.setText(quest);
        answerTextView.setText(question.getAnswer());

        return view;
    }
}
