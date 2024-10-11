package com.example.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class firstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first);


        // 获取截图文件路径
        String screenshotPath = getIntent().getStringExtra("screenshot_path");

        if (screenshotPath != null) {
            // 显示截图
            ImageView imageView = findViewById(R.id.imageView12);
            Bitmap bitmap = BitmapFactory.decodeFile(screenshotPath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "未接收到截图", Toast.LENGTH_SHORT).show();
        }


    }
}