package com.example.autobike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.R;
import com.example.autobike.database.RecordDBHelper;
import com.example.autobike.entity.SportRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class recordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<SportRecord> dataList;

    @SuppressLint("WrongViewCast")
    private  RecordDBHelper recordDBHelper;

    @SuppressLint({"ResourceType", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ImageButton backButton = findViewById(R.id.imageButton);
        backButton.setOnClickListener(v->{
            Intent intent=new Intent(recordActivity.this,MapActivity.class);
            startActivity(intent);
        });



        // 初始化数据
        RecordDBHelper recordDBHelper = RecordDBHelper.getInstance(this);

        dataList=recordDBHelper.QueryRecordsByMonth(0);

        LinearLayout monthsLayout = findViewById(R.id.monthsLayout);
        //TODO add month query
        List<Integer> monthList = IntStream.rangeClosed(1,12).boxed().collect(Collectors.toList());
        //range month list and set click listener
        for (Integer month : monthList) {
            TextView textView = new TextView(this);
            textView.setText(month+"月");
            textView.setPadding(20, 16, 20, 2);
//            textView.setBackgroundResource(android.R.attr.selectableItemBackground); // 设置背景为可点击样式
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(24);

            // 设置点击事件
            textView.setOnClickListener(v -> {
                Toast.makeText(this, "Clicked: " + month, Toast.LENGTH_SHORT).show();
            });

            // 设置布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 8, 20, 2); // 设置TextView之间的间距
            textView.setLayoutParams(params);

            // 将TextView添加到LinearLayout中
            monthsLayout.addView(textView);
        }



        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 创建并设置适配器
        adapter = new ItemAdapter(dataList,this);
        recyclerView.setAdapter(adapter);


    }
}