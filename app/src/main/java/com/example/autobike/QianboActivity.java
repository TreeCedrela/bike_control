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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.autobike.utils.BleHelper;
import com.example.autobike.bluetooth.BleCallback;
import com.example.map.R;


public class QianboActivity extends AppCompatActivity {

    private Qianbbbb qianbbbb;
    private Button button1,button2,button5,button6;
    private SeekBar seekBar;
    private TextView textView1,textview2,devicenumber,frontdialspeed;
    private boolean isLeftGearHighlighted=true;
    private boolean isRightGearHighlighted=false;
    public Button button3;
    public Button button4;
    private TextView nowqianbo;


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



        seekBar=findViewById(R.id.weitiao);
        textView1=findViewById(R.id.textView);//设备信息
        textview2=findViewById(R.id.FrontValue);//微调值存储
        devicenumber=findViewById(R.id.DeviceNumber);//设备唯一码
        frontdialspeed=findViewById(R.id.FrontDialSpeed);//前拨速别
        //············································

        //设置seekbar的监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // handleSeekBarProgressChanged(progress);
                textview2.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始触摸滑块时执行的操作

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//停止触摸滑块时执行的操作

            }
        });

        qianbbbb=findViewById(R.id.qianbbbb);
        button1=findViewById(R.id.button1);//减小速别
        button2=findViewById(R.id.button2);//增加速别
        button5=findViewById(R.id.button3);//单挡微调界面跳转
        button6=findViewById(R.id.button4);//高低限位界面跳转
        nowqianbo=findViewById(R.id.FrontDialSpeed);
        button1.setOnClickListener(view ->  {

            BleHelper.sendCommand(bluetoothGatt,"110600005B73",true);
            toggleGears(true);
            updateHighlightInfo(true);
        });

        button2.setOnClickListener(view ->  {
            BleHelper.sendCommand(bluetoothGatt, "12060000C0AF",true);
            toggleGears(false);
            updateHighlightInfo(false);
        });

        /*
         * 前拨微调加减
         * “+”：BleHelper.sendCommand(bluetoothGatt, "2C0600004E1C",true);
         * “-”：BleHelper.sendCommand(bluetoothGatt, "14060000E736",true);
         *
         */
        button3=findViewById(R.id.houbo);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QianboActivity.this,HouboActivity.class);
                //intent.putExtra("device",myDevice.getDevice());
                startActivity(intent);

            }
        });

        button4=findViewById(R.id.shoubian);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QianboActivity.this,Shoubian.class);
                startActivity(intent);

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QianboActivity.this, qianbogaodixianwei.class);
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
           // nowqianbo.setText(BleCallback.buffer);

        } else {
            nowqianbo.setText(getResources().getString(R.string.highlight_right_gear));
        }
    }

    //计算CRC校验位
    public static String calculateCRC16(String input) {
        // 将字符串转换为字节数组，使用 UTF-8 编码
        byte[] data = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        int crc = 0xFFFF;  // 初始值为 0xFFFF
        for (byte b : data) {
            crc ^= (b << 8);  // 将当前字节的高8位与CRC异或
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {  // 判断最高位是否为1
                    crc = (crc << 1) ^ 0x1021;  // 左移并与多项式0x1021异或
                }
                else {
                    crc <<= 1;  // 否则只左移1位
                }
            }
        }
        return String.format("%04X", crc & 0xFFFF);
    }




}