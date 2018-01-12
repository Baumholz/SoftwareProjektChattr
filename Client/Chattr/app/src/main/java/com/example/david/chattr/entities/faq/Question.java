package com.example.david.chattr.entities.faq;

/**
 * Created by david on 28.12.17.
 */

public class Question {
    private String quest;
    private String answer;

    public Question(String quest, String answer) {
        this.quest = quest;
        this.answer = answer;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String question) {
        this.quest = quest;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
