package com.example.thesis_lab_02.ocr_try;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.android.imagetotext.R;

public class StartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        RelativeLayout startScreen = (RelativeLayout) findViewById(R.id.startScreen);
        skipScreen();
        startScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void skipScreen() {
        final int TIME_OUT =2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                startActivity(intent);
            }
        }, TIME_OUT);
    }
}
