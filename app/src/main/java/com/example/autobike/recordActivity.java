package com.example.autobike;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class recordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<MapItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 初始化数据
        dataList = new ArrayList<>();
        dataList.add(new MapItem("1212",333.0F,22.0F,10.0F,30,new Date(2023-11-22)));
        dataList.add(new MapItem("1212",333.0F,22.0F,10.0F,30,new Date(2023-11-22)));
        dataList.add(new MapItem());
        // 可以动态添加更多数据

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 创建并设置适配器
        adapter = new ItemAdapter(dataList,this);
        recyclerView.setAdapter(adapter);
    }
}