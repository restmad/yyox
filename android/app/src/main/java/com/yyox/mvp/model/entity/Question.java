package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-05-12.
 */

public class Question {

    public Question() {
        this.question = "";
        this.answer = "";
    }

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String question;
    private String answer;
}
