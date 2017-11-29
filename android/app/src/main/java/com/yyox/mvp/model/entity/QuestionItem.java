package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-05-12.
 */

public class QuestionItem {

    public QuestionItem() {
        this.name = "";
        this.questions = new ArrayList<>();
    }

    public QuestionItem(String name, List<Question> question) {
        this.name = name;
        this.questions = question;
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestion() {
        return questions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestion(List<Question> questions) {
        this.questions = questions;
    }

    private String name;
    private List<Question> questions;

}
