package com.example.autobike;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.autobike.bluetooth.BleCallback;
import com.example.map.R;

import com.example.autobike.utils.BleHelper;
import com.example.autobike.bluetooth.BleCallback;

public class Shoubian extends AppCompatActivity {

    private Button swapButton,button1,button2,button3;
    private ImageView icon1,icon2;

    private BluetoothGatt bluetoothGatt;
    private boolean isConnected = false;
    private BleCallback bleCallback;
    private EditText etCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(com.example.map.R.layout.activity_shoubian);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //初始化
        bleCallback = new BleCallback();
        //获取上个页面传递过来的设备
        BluetoothDevice device = getIntent().getParcelableExtra("device");
        //连接gatt 设置Gatt回调
        bluetoothGatt = device.connectGatt(this, false, bleCallback);

        swapButton=findViewById(R.id.swapBUtton);
        icon1=findViewById(R.id.icon1);
        icon2=findViewById(R.id.icon2);
        button1=findViewById(R.id.qianbo);
        button2=findViewById(R.id.houbo);
        button3=findViewById(R.id.shoubian);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shoubian.this,MainActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shoubian.this,HouboActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shoubian.this,Shoubian.class);
                startActivity(intent);
            }
        });

        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int icon1Left=icon1.getLeft();
                int icon1Top=icon1.getTop();
                int icon2Left=icon2.getLeft();
                int icon2Top=icon2.getTop();
                BleHelper.sendCommand(bluetoothGatt, "2A0600006985",true);
                icon1.layout(icon2Left,icon2Top,icon2Left+icon1.getWidth(),icon2Top+icon1.getWidth());
                icon2.layout(icon1Left,icon1Top,icon1Left+icon2.getWidth(),icon1Top+icon2.getWidth());
            }
        });
    }
}