package com.ahmedfahmi.guesswho;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public String data;
    private ArrayList<String> imagesSrc;
    private ArrayList<String> namesSrc;
    private ArrayList<String> answers;
    private ImageView imageView;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Random rand;
    private int correctAnswerPosition;
    private String correctAnswer;
    private int celebId;
    private String imgUrl;
    private Bitmap celebrityImage;
    private ImageDownLoader imageDownLoader;
    private Intent intentOfSplashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("C_", "oncreate");


        initiate();
        generate();
    }

    private void initiate() {


        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageDownLoader = new ImageDownLoader();
        intentOfSplashActivity = getIntent();

    }


    public void startGuess() {
        if (answers != null) {
            answers.clear();
        }
        rand = new Random();
        celebId = rand.nextInt(imagesSrc.size());
        imgUrl = imagesSrc.get(celebId);

        imageDownLoader = new ImageDownLoader();

        try {
            celebrityImage = imageDownLoader.execute(imgUrl).get();
            imageView.setImageBitmap(celebrityImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        correctAnswer = namesSrc.get(celebId);
        correctAnswerPosition = rand.nextInt(4);

        answers = new ArrayList<String>(4);/////set size


        for (int i = 0; i < 4; i++) {
            if (i == correctAnswerPosition) {
                answers.add(correctAnswer);
            } else {
                String answer = namesSrc.get(rand.nextInt(namesSrc.size()));
                while (answer.equals(correctAnswer)) {
                    answer = namesSrc.get(rand.nextInt(namesSrc.size()));
                }
                answers.add(answer);
            }

        }
        btn1.setText(answers.get(0));
        btn2.setText(answers.get(1));
        btn3.setText(answers.get(2));
        btn4.setText(answers.get(3));


    }


    public void celebChosen(View view) {

        if (view.getTag().toString().equals(Integer.toString(correctAnswerPosition))) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Wrong! answer is " + correctAnswer, Toast.LENGTH_SHORT).show();

        }
        startGuess();

    }

    public void generate() {
        imagesSrc = new ArrayList<String>();
        namesSrc = new ArrayList<String>();

        String content = "";

        content = intentOfSplashActivity.getStringExtra("Data");
        String[] splitContent = content.split("\"<div class=\"sidebarContainer\">\"");
//import Images
        Pattern p = Pattern.compile("img src=\"(.*?)\"");
        Matcher m = p.matcher(splitContent[0]);

        while (m.find()) {
            imagesSrc.add(m.group(1));
            System.out.println(m.group(1));

        }
//import Names
        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(splitContent[0]);
        while (m.find()) {
            namesSrc.add(m.group(1));
            System.out.println(m.group(1));
        }


        startGuess();


    }


}
