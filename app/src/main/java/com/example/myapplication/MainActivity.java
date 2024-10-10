package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private Qianbbbb qianbbbb;
    private Button button1,button2,button5,button6;
    private SeekBar seekBar;
    private TextView textView;
    private boolean isLeftGearHighlighted=true;
    private boolean isRightGearHighlighted=false;
    public Button button3;
    public Button button4;
    private TextView nowqianbo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar=findViewById(R.id.weitiao);
        textView=findViewById(R.id.textView);//逻辑不对需要改
        //············································

        //设置seekbar的监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handleSeekBarProgressChanged(progress);
                textView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始触摸滑块时执行的操作

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//停止触摸滑块时执行的操作

            }
        });

        qianbbbb=findViewById(R.id.qianbbbb);
        button1=findViewById(R.id.button1);//假设按钮1的id为button1
        button2=findViewById(R.id.button2);
        button5=findViewById(R.id.button3);
        button6=findViewById(R.id.button4);
        nowqianbo=findViewById(R.id.nowqian);//假设按钮2的id为button2
        button1.setOnClickListener(view ->  {
            toggleGears(true);
            updateHighlightInfo(true);
        });

        button2.setOnClickListener(view ->  {
            toggleGears(false);
            updateHighlightInfo(false);
        });

        button3=findViewById(R.id.houbo);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HouboActivity.class);
                startActivity(intent);
            }
        });

        button4=findViewById(R.id.shoubian);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Shoubian.class);
                startActivity(intent);

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,qianbogaodixianwei.class);
                startActivity(intent);
            }
        });

    }



    private void handleSeekBarProgressChanged(int progress){
        if (isLeftGearHighlighted&&progress==seekBar.getMax()){
            toggleGears(false);
            updateHighlightInfo(false);

        }else if (isRightGearHighlighted&&progress==seekBar.getMin()) {
            toggleGears(true);
            updateHighlightInfo(true);
        }
    }

    private void toggleGears(boolean isLeftHighlighted){
        isLeftGearHighlighted=isLeftHighlighted;
        isRightGearHighlighted=!isLeftHighlighted;
        qianbbbb.setQianbo1Highlighted(isLeftHighlighted);
        qianbbbb.setQianbo2Highlighted(!isLeftHighlighted);
    }

    private void updateHighlightInfo(boolean isLeftHighlighted) {
        if (isLeftHighlighted) {
            nowqianbo.setText(getResources().getString(R.string.highlight_left_gear));
        } else {
            nowqianbo.setText(getResources().getString(R.string.highlight_right_gear));
        }
    }





}