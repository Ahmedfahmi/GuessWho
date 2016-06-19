package com.ahmedfahmi.guesswho;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class SplashActivity extends AppCompatActivity implements LoadingTask.LoadingTaskFinishedListener {

    private String data = "";
    private ProgressBar progressBar;
    private boolean active = false;
    LoadingTask loadingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("C_", "splash");
        setContentView(R.layout.activity_splash);
        final ImageView splashIcon = (ImageView) findViewById(R.id.spalshIcon);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splashRelativeLayout);
        active = true;

        final int middle = relativeLayout.getWidth() / 2;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (active) {
                    Log.i("logging", "i'm runnable");
                    splashIcon.setTranslationX(middle);
                    splashIcon.animate().translationXBy(1000f).setDuration(2000);
                    handler.postDelayed(this, 2000);//run after 3 second
                }

            }
        };
        handler.post(runnable);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingTask = new LoadingTask(progressBar, this);
        loadingTask.execute("http://www.posh24.com/celebrities");
    }

    @Override
    public void onTaskFinished() {
        active = false;
        String content = loadingTask.getData();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("Data", content);

        startActivity(intent);

        finish();
    }
}
