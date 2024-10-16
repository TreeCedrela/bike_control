package com.example.autobike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;

public class qianbogaodixianwei extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(com.example.map.R.layout.activity_qianbogaodixianwei);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SeekBar seekBar = findViewById(R.id.jiaozhungao);
        ImageButton addButton = findViewById(R.id.addbutton);
        ImageButton decreaseButton = findViewById(R.id.dreasebutton);
        TextView textView = findViewById(R.id.HighLimitValue);
        ImageButton backtoQianboButton=findViewById(R.id.backtoQianbo);
        Button buttondi=findViewById(R.id.di);

        buttondi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(qianbogaodixianwei.this,QianboLowerRejection.class);
                startActivity(intent);
            }
        });

        backtoQianboButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qianbogaodixianwei.this, MainActivity.class);
                startActivity(intent);
            }
        });

        seekBar.setMax(200);
        seekBar.setMin(-200);
        seekBar.setProgress(0);
        textView.setText("微调值："+0);
        textView.setTextSize(25);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("微调值："+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始触摸滑块时执行的操作

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//停止触摸滑块时执行的操作

            }
        });

        addButton.setOnClickListener(v -> {
            int progress = seekBar.getProgress();
            progress++;
            if (progress > seekBar.getMax()) {
                progress = seekBar.getMax();
                seekBar.setProgress(progress);
            } else {
                seekBar.setProgress(progress);
            }
            textView.setText("微调值："+progress);
        });

        decreaseButton.setOnClickListener(v -> {
            int progress;
            progress = seekBar.getProgress();
            progress--;
            if (progress < -200) {
                progress =-200;
            }
            seekBar.setProgress(progress);
            textView.setText("微调值："+progress);
        });
    }
}