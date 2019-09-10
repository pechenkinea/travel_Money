package com.pechenkin.travelmoney.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.MainActivity;

public class RunWithProgressBar<T> extends AsyncTask<Void, Void, T> {

    private ProgressDialog processDialog;

    private final DoInBackground<T> doInBackground;
    private final DoPost<T> doPost;

    public RunWithProgressBar(@NonNull DoInBackground<T> doInBackground, RunWithProgressBar.DoPost<T> doPost) {
        this.doInBackground = doInBackground;
        this.doPost = doPost;
        execute();
    }


    @Override
    protected T doInBackground(Void... voids) {
        return this.doInBackground.execute();
    }

    @Override
    protected void onPreExecute() {
        processDialog = Help.createProgressDialog(MainActivity.INSTANCE);
        processDialog.show();
    }


    @Override
    protected void onPostExecute(T result) {
        processDialog.dismiss();
        if (doPost != null) {
            this.doPost.execute(result);
        }
    }


    public interface DoInBackground<T> {
        T execute();
    }

    public interface DoPost<T> {
        void execute(T t);
    }


}
