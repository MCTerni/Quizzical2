package com.mateus.bootcamp.quizzical2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "score";
    public static final String KEY_TOTAL_QUESTIONS = "total";

    private TextView resultTextView;
    private Button returnButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.result_text);
        returnButton = findViewById(R.id.replay_button);


        Intent intent = getIntent();
        int score = intent.getIntExtra(KEY_SCORE, -1);
        int totalQuestions = intent.getIntExtra(KEY_TOTAL_QUESTIONS, -1);

        String result = String.format("%d / %d", score, totalQuestions);
        resultTextView.setText(result);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("HI","False button clicked");
                returnMain();
            }
        });

    }
    private void returnMain(){
        //return to Main Activity
        Intent resultActivityIntent = new Intent(this, ListActivity.class);
        startActivity(resultActivityIntent);
    }

}
