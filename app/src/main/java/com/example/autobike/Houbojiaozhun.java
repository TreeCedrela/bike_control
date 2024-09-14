package com.example.autobike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;

public class Houbojiaozhun extends AppCompatActivity {
    private Houbbbb houbbbb;//用于储存每根线的状态
    private Button leftButton,rightButton,button1,button3,button7,button8;
    private SeekBar seekBar;
    private TextView nowhoubo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(com.example.map.R.layout.activity_houbojiaozhun);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        houbbbb=findViewById(R.id.houbbbb);
        leftButton=findViewById(R.id.button1_1);
        rightButton=findViewById(R.id.button2_2);
        seekBar=findViewById(R.id.weitiao_2);
        button3=findViewById(R.id.shoubian);
        button1=findViewById(R.id.qianbo);
        nowhoubo=findViewById(R.id.nowhou);
        button7=findViewById(R.id.buttonhoubo1);
        button8=findViewById(R.id.buttonhoubojiaozhun);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Houbojiaozhun.this,MainActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Houbojiaozhun.this,Shoubian.class);
                startActivity(intent);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Houbojiaozhun.this,HouboActivity.class);
                startActivity(intent);
            }
        });

        houbbbb.currentHighlightedIndex=6;


        leftButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (houbbbb.currentHighlightedIndex > 0) {
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] - 10;
                    houbbbb.currentHighlightedIndex--;
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] + 10;
                    houbbbb.invalidate();

                    nowhoubo.setText((houbbbb.currentHighlightedIndex+1)+"spr");
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (houbbbb.currentHighlightedIndex < 11) {
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] - 10;
                    houbbbb.currentHighlightedIndex++;
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] + 10;
                    houbbbb.invalidate();
                    nowhoubo.setText((houbbbb.currentHighlightedIndex+1)+"spr");
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    if ((houbbbb.currentHighlightedIndex > 0)) {
                        houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] - 10;
                        houbbbb.currentHighlightedIndex--;
                        houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] + 10;
                        seekBar.invalidate();
                        nowhoubo.setText((houbbbb.currentHighlightedIndex+1)+"spr");
                    }
                } else if (progress == 500) {
                    if (houbbbb.currentHighlightedIndex < 11) {
                        houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] - 10;
                        houbbbb.currentHighlightedIndex++;
                        houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] + 10;
                        seekBar.invalidate();
                        nowhoubo.setText((houbbbb.currentHighlightedIndex+1)+"spr");
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
