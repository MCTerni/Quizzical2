package com.mateus.bootcamp.quizzical2;

import android.content.Context;
import android.util.Log;
import android.view.textclassifier.TextLinks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;

public class QuizRepository {

    private final Context context;

    public QuizRepository(Context context) {
        this.context = context;
    }
/*
    public Quiz getQuiz() {
        InputStream assetInputStream;
        try {
            assetInputStream = context.getAssets().open(context.getString(R.string.quiz_json));

        } catch (IOException e) {
            Log.e("QuizRepo", "unable to open quiz.json");
            return null;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        TypeAdapter<Quiz> jsonAdapter = gson.getAdapter(Quiz.class);
        Reader reader = new InputStreamReader(assetInputStream);

       // Moshi moshi = new Moshi.Builder().build();
       // JsonAdapter<Quiz> jsonAdapter = moshi.adapter(Quiz.class);

        try {
            //Quiz quiz = jsonAdapter.fromJson(Okio.buffer(Okio.source(assetInputStream)));


            Quiz quiz = jsonAdapter.fromJson(reader);
            return quiz;
        } catch (IOException e) {
            Log.e("QuizRepo", "Could not parse json");
            return null;
        }


    }*/

public void getRemoteQuiz(int id, final QuizCallback quizCallback){
    OkHttpClient cliente = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://oolong.tahnok.me/cdn/quizzes/" + id + ".json")
            .build();
    Response response;
    cliente.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            quizCallback.onFailure();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(!response.isSuccessful()){
                quizCallback.onFailure();
            }
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Quiz> jsonAdapter = moshi.adapter(Quiz.class);
            Quiz quiz = jsonAdapter.fromJson(response.body().source());
            quizCallback.onSuccess(quiz);
        }
    });
}
public void getRemoteQuizzes(final QuizzesCallback callback) {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://oolong.tahnok.me/cdn/quizzes.json")
            .build();

    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            callback.onFailure();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (!response.isSuccessful()) {
                callback.onFailure();
            }

            Moshi moshi = new Moshi.Builder().build();
            Type type = Types.newParameterizedType(List.class, Quiz.class);
            JsonAdapter<List<Quiz>> jsonAdapter = moshi.adapter(type);
            List<Quiz> quizzes = jsonAdapter.fromJson(response.body().source());

            callback.onSuccess(quizzes);
        }
    });



/**these try catch crash the app
        try {
            Response response = cliente.newCall(request).execute();
            if(!response.isSuccessful()){
                return null;
            }
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Quiz> jsonAdapter = moshi.adapter(Quiz.class);
            Quiz quiz = jsonAdapter.fromJson(Okio.buffer(response.body().source()));
            return  quiz;
        } catch (IOException e) {
            Log.e("QuizRepo", "Error contacting server", e);
                    return null;
        }
**/
    }
    public interface QuizCallback{
        void onFailure();
        void onSuccess(Quiz quiz);
    }
    public interface QuizzesCallback{
        void onFailure();
        void onSuccess(List<Quiz> quizzes);
    }
}
