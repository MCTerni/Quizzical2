package com.mateus.bootcamp.quizzical2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity implements QuizRepository.QuizzesCallback {

    private ListView listView;

    private QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quiz quiz = quizAdapter.getItem(position);
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.putExtra("quiz_id", quiz.getId());
                startActivity(intent);
            }
        });

        new QuizRepository(this).getRemoteQuizzes(this);
    }

    @Override
    public void onFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        Toast.makeText(ListActivity.this, "Could not retrieve quiz list", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSuccess(final List<Quiz> quizzes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                quizAdapter = new QuizAdapter(ListActivity.this, quizzes);
                listView.setAdapter(quizAdapter);
            }
        });
    }

    static class QuizAdapter extends ArrayAdapter<Quiz> {

        public QuizAdapter(@NonNull Context context, @NonNull List<Quiz> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Quiz quiz = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = (TextView) convertView;

            textView.setText(quiz.getTitle());

            return textView;
        }
    }
}

