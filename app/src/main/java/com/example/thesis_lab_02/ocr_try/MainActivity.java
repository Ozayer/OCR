package com.example.thesis_lab_02.ocr_try;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.imagetotext.R;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button cameraButton, galleryButton;
        cameraButton = (Button) findViewById(R.id.cameraButton);
        galleryButton = (Button) findViewById(R.id.galleryButton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int CAMERA_PIC_REQUEST = 1;
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = selectedImageUri.getPath();
                Log.v("IMage", selectedImagePath);
                SharedPreferences sharedPref = getSharedPreferences("kk", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ipath", selectedImagePath);
                editor.commit();
                nextActivityIntent();
            }
        }
    }

    /**
     * method to display image in next Activity
     */
    public void nextActivityIntent() {
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        startActivity(intent);
    }
}

