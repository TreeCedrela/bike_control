package com.example.autobike;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.autobike.utils.BleHelper;
import com.example.autobike.bluetooth.BleCallback;
import com.example.map.R;


public class QianboActivity extends AppCompatActivity {

    private Qianbbbb qianbbbb;
    private Button  button5, button6;
    private TextView textView1, frontvalue, devicenumber, frontdialspeed;
    public Button button3;
    public Button button4;
    // 新增：用于存储当前的数值，代替SeekBar的进度值
    private int frontDialNumber = 0;
    private ImageView frontbattery;
    private ImageButton button1,button2;


    private BluetoothGatt bluetoothGatt;
    private boolean isConnected = false;
    private BleCallback bleCallback;
    private EditText etCommand;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.map.R.layout.activity_qianbo);


        //初始化
        bleCallback = new BleCallback();
        //获取上个页面传递过来的设备
        BluetoothDevice device = getIntent().getParcelableExtra("device");
        //连接gatt 设置Gatt回调
        bluetoothGatt = device.connectGatt(this, false, bleCallback);

        Button jumpButton=findViewById(R.id.button7);

        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QianboActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        textView1 = findViewById(R.id.textView);//设备信息
        frontvalue = findViewById(R.id.FrontValue);//微调值存储
        devicenumber = findViewById(R.id.DeviceNumber);//设备唯一码
        frontdialspeed = findViewById(R.id.FrontDialSpeed);//前拨速别

//        // 新增：读取当前哪个圆环对应的轮子高亮并设置
//        readAndSetHighlightedRing();


        qianbbbb = findViewById(R.id.qianbbbb);
        frontbattery=findViewById(R.id.FrontBattery);//前拨电池电量
        button1 = findViewById(R.id.button1);//减小速别
        button2 = findViewById(R.id.button2);//增加速别
        button5 = findViewById(R.id.button3);//单挡微调界面跳转
        button6 = findViewById(R.id.button4);//高低限位界面跳转
        button1.setOnClickListener(view -> {
            BleHelper.sendCommand(bluetoothGatt, "110600005B73", true);

            if (!qianbbbb.isQianbo1Highlighted()) {
                qianbbbb.setNextInnerRingHighlighted();

            }
        });

        button2.setOnClickListener(view -> {
            BleHelper.sendCommand(bluetoothGatt, "12060000C0AF", true);
            if (!qianbbbb.isQianbo3Highlighted()) {
                qianbbbb.setNextOuterRingHighlighted();
            }
        });


        // 新增：添加增加数值的按钮及点击事件处理
        ImageButton addButton = findViewById(R.id.AddFrontDialNumber);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frontDialNumber < 5) {
                    frontDialNumber++;
                    updateFrontValueText();
                    BleHelper.sendCommand(bluetoothGatt, "2C060004E1C", true);
                }
            }
        });

        // 新增：添加减少数值的按钮及点击事件处理
        ImageButton decreaseButton = findViewById(R.id.DreaseFrontDialNumber);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frontDialNumber > -5) {
                    frontDialNumber--;
                    updateFrontValueText();
                    BleHelper.sendCommand(bluetoothGatt, "14060000E736", true);
                }
            }
        });


        button3 = findViewById(R.id.houbo);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QianboActivity.this, HouboActivity.class);
                intent.putExtra("device",device);
                startActivity(intent);

            }
        });

        button4 = findViewById(R.id.shoubian);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QianboActivity.this, Shoubian.class);
                intent.putExtra("device",device);
                startActivity(intent);

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QianboActivity.this, qianbogaodixianwei.class);
                intent.putExtra("device",device);
                startActivity(intent);
            }
        });


//    private void readAndSetHighlightedRing() {//获取前拨速别
//        String command = "11030000" + calculateCRC16("11030000");
//        BleHelper.sendCommand(bluetoothGatt, command, true);
//
//
//        int highlightedRingIndex = BleCallback.highlightedRingIndex;
//
//        if (highlightedRingIndex == 0) {
//            qianbbbb.setQianbo1Highlighted(true);
//        } else if (highlightedRingIndex == 1) {
//            qianbbbb.setQianbo2Highlighted(true);
//        } else if (highlightedRingIndex == 2) {
//            qianbbbb.setQianbo3Highlighted(true);
//        }
//    }


    }

    private void updateFrontValueText() {
        frontvalue.setText(BleCallback.buffer);
    }
}