package com.mateus.bootcamp.quizzical2;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements QuizRepository.QuizCallback{

    public static final String KEY_QUESTION_ID = "quiz_id";

    private Button trueButton;
    private Button falseButton;
    private TextView answerTextView;
    private TextView questionTextView;
    private Button nextButton;

    private boolean userAnswer;
    private boolean isQuestionAnswered = false;

    private Quiz quiz;
    private int currentQuestionIndex = 0;
    private int score = 0;


/*
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        falseButton.setWidth(trueButton.getWidth());
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        answerTextView = findViewById(R.id.answer_text);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("HI","True button clicked");
                checkAnswer(true);

            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("HI","False button clicked");
                checkAnswer(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("HI","False button clicked");
                nextQuestion();
            }
        });

        if (savedInstanceState != null) { //no savedInstanceState when activity is first launched
            isQuestionAnswered = savedInstanceState.getBoolean(QUESTION_ANSWERED, false);
            userAnswer = savedInstanceState.getBoolean(USER_ANSWER);
            currentQuestionIndex = savedInstanceState.getInt(CURRENT_QUESTION_INDEX, 0);
            score = savedInstanceState.getInt(SCORE, 0);
            answerTextView.setText(savedInstanceState.getString(ANSWER_MESSAGE));
        }
//restore it using the key
        //Load quiz for quiz.class
        // quiz = Quiz.getInstance(getApplicationContext());

        //Load quiz from the json file
        //quiz = new QuizRepository(this).getreQuiz();//get quiz from JSON file
        Intent intent = getIntent();
        int id = intent.getIntExtra(KEY_QUESTION_ID, -1);
        new QuizRepository(this).getRemoteQuiz(id, this);//get quiz from HTTP

/**Moved to onSuccess callback
        showQuestion();

        if (isQuestionAnswered) {
            checkAnswer(userAnswer);
        }
**/
    }
    private void checkAnswer(boolean answerToCheck)
    {
        userAnswer = answerToCheck;
        nextButton.setEnabled(true);
        if(!isQuestionAnswered) {
            if (answerToCheck == quiz.getQuestions().get(currentQuestionIndex).getAnswer()) {
                answerTextView.setText(R.string.correct_answer);
                score++;
            } else {
                answerTextView.setText(R.string.wrong_answer);
            }
        }
        isQuestionAnswered = true;

    }
    private static final String USER_ANSWER = "user_answer";
    private static final String QUESTION_ANSWERED = "question_answered";
    private static final String CURRENT_QUESTION_INDEX = "current_question_index";
    private static final String SCORE = "score";
    private static final String ANSWER_MESSAGE = "answer_message";


/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        answerTextView.setText(savedInstanceState.getString(ANSWER_MESSAGE));
    }
*/
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(USER_ANSWER, userAnswer);
        bundle.putBoolean(QUESTION_ANSWERED, isQuestionAnswered);
        bundle.putInt(CURRENT_QUESTION_INDEX, currentQuestionIndex);
        bundle.putInt(SCORE, score);
        bundle.putString(ANSWER_MESSAGE, answerTextView.getText().toString());

    }

    private void showQuestion() {
        Question question = quiz.getQuestions().get(currentQuestionIndex);
        questionTextView.setText(question.getStatement());
        //
    }
    private void nextQuestion() {
        answerTextView.setText("");
        //currentQuestionIndex = (currentQuestionIndex + 1) % quiz.getQuestions().size();
        currentQuestionIndex++;
        nextButton.setEnabled(false);
        if(currentQuestionIndex>=quiz.getQuestions().size()){
            //show result Activity
            Intent resultActivityIntent = new Intent(this, ResultActivity.class);
            resultActivityIntent.putExtra(ResultActivity.KEY_SCORE, score);
            resultActivityIntent.putExtra(ResultActivity.KEY_TOTAL_QUESTIONS, quiz.getQuestions().size());
            startActivity(resultActivityIntent);
        }else{
            isQuestionAnswered = false;
            showQuestion();
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(this,"Unable to fetch quiz", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(final Quiz quiz) {
        //this.quiz = quiz;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.quiz = quiz;
                showQuestion();

                if (isQuestionAnswered) {
                    checkAnswer(userAnswer);
                }
            }
        });

    }
}
