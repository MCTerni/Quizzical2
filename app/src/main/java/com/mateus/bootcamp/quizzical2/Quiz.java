package com.mateus.bootcamp.quizzical2;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class Quiz {


    private String title;
    private int id;

    private List<Question> questions = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
/**it's not beeing called anymore
 * private static Quiz quiz;
    public static Quiz getInstance(Context context) {
        if (quiz == null) {
            quiz = new Quiz();
            quiz.addQuestion(new Question(context.getString(R.string.question0), false));
            quiz.addQuestion(new Question(context.getString(R.string.question1), true));
        }
        return quiz;
    }
    **/
}

