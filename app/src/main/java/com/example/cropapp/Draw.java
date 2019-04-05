package com.example.cropapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.views.DrawView;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;


public class Draw extends AppCompatActivity {

    //DrawView drawView;
    //ImageView imageView;
    Uri uri;
    File file;
    PhotoEditorView mPhotoEditorView;
    Typeface mTextRobotoTf;
    PhotoEditor mPhotoEditor;
    Typeface mEmojiTypeFace;
    Button mButton, saveButton;
    private String currentPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        //drawView = (DrawView) findViewById(R.id.draw_view);
        //imageView = findViewById(R.id.imageView2);
        mButton = findViewById(R.id.undobtn);
        saveButton = findViewById(R.id.savebtn);

        Bundle extras = getIntent().getExtras();
        uri = Uri.parse(extras.getString("imageUri"));
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mPhotoEditorView.getSource().setImageURI(uri);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();
        mPhotoEditor.setBrushDrawingMode(true);



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.undo();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            saveImage();

            }
        });




    }


    @SuppressLint("MissingPermission")
    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //showLoading("Saving...");
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + ""
                    + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                       // hideLoading();
                        //showSnackbar("Image Saved Successfully");
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //hideLoading();
                        //showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                //hideLoading();
                //showSnackbar(e.getMessage());
            }
        }
    }

    private boolean requestPermission(String writeExternalStorage) {
        if(Build.VERSION.SDK_INT >= 23){

            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            return true;
        }
        return true;
    }


}
