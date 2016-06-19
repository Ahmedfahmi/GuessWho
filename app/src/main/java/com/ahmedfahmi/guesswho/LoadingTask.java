package com.ahmedfahmi.guesswho;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ahmed Fahmi on 6/9/2016.
 */
public class LoadingTask extends AsyncTask<String, Integer, String> {

    private URL url = null;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private InputStreamReader reader;
    private String result = "";
    private String data;

    public interface LoadingTaskFinishedListener {
        void onTaskFinished(); // If you want to pass something back to the listener add a param to this method
    }

    private final ProgressBar progressBar;
    // This is the listener that will be told when this task is finished
    private final LoadingTaskFinishedListener finishedListener;




    public String getData() {
        return data;
    }

    public LoadingTask(ProgressBar progressBar, LoadingTaskFinishedListener finishedListener) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
    }


    @Override
    protected String doInBackground(String... urls) {
        try {
            url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            reader = new InputStreamReader(inputStream);
            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
        // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        data = result;
        finishedListener.onTaskFinished(); // Tell whoever was listening we have finished
    }
}

