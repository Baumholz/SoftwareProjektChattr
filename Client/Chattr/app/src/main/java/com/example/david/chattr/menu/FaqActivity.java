package com.example.david.chattr.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.david.chattr.R;
import com.example.david.chattr.adapters.FaqListAdapter;
import com.example.david.chattr.entities.faq.Question;

import java.util.ArrayList;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FAQ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("Who made this App?", "Michael Kurras, Trinity Merrell Manuele Schneckenburger, David Hierholz."));
        questions.add(new Question("Where does the name 'Chattr' comes from?", "It's a mix of the words 'Chat' and 'Cheddar', the cheese."));
        questions.add(new Question("Which communication protocol is used for the chat?", "MQTT protocol."));
        questions.add(new Question("What is this NFC thing and how does it work?", "NFC stands for Near Field Communication. " +
                "If you have two phones with NFC capability, you can exchange contacts by holding the phones close to each other."));

        ListView faqListView = (ListView) findViewById(R.id.faqListView);
        FaqListAdapter adapter = new FaqListAdapter(questions);
        faqListView.setAdapter(adapter);
    }

    //for the back button
    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
