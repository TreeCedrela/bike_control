package com.example.autobike;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.autobike.bluetooth.BleCallback;
import com.example.autobike.utils.BleHelper;
import com.example.map.R;

public class HouboActivity extends AppCompatActivity {
    private Houbbbb houbbbb;//用于储存每根线的状态
    private Button button1,button3,button7,button8;
    private TextView BehindDialSpeed,BehindValue,Behinddevicenumber;
    private ImageView behindbattery;


    private BluetoothGatt bluetoothGatt;
    private boolean isConnected = false;
    private BleCallback bleCallback;
    private EditText etCommand;

    private int behindDialNumber = 0;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.houbo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button jumpButton=findViewById(R.id.button8);

        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HouboActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        //初始化
        bleCallback = new BleCallback();
        //获取上个页面传递过来的设备
        BluetoothDevice device = getIntent().getParcelableExtra("device");
        //连接gatt 设置Gatt回调
        bluetoothGatt = device.connectGatt(this, false, bleCallback);

        houbbbb=findViewById(R.id.houbbbb);
        ImageButton leftButton=findViewById(R.id.BehindReduceButton);
        ImageButton rightButton=findViewById(R.id.BehindAddButton);
        button3=findViewById(R.id.shoubian);
        button1=findViewById(R.id.qianbo);
        BehindDialSpeed=findViewById(R.id.BehindDialSpeed);
        BehindValue=findViewById(R.id.BehindValue);//后拨校准
        button7=findViewById(R.id.buttonhoubo1);
        button8=findViewById(R.id.buttonhoubojiaozhun);//校准界面跳转
        ImageButton addbehindvale=findViewById(R.id.AddBehindValue);//增加微调值
        ImageButton reducebehindvale=findViewById(R.id.DreaseBehindValue);//微调值减小
        behindbattery=findViewById(R.id.BehindBattery);//后拨电池
        Behinddevicenumber=findViewById(R.id.DeviceNumberBehind);//设备型号

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HouboActivity.this,QianboActivity.class);
                intent.putExtra("device",device);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HouboActivity.this,Shoubian.class);
                intent.putExtra("device",device);
                startActivity(intent);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        //档位“-”
        leftButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                BleHelper.sendCommand(bluetoothGatt, "0E060000943A",true);
                if (houbbbb.currentHighlightedIndex > 0) {
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] - 10;
                    houbbbb.currentHighlightedIndex--;
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] + 10;
                    houbbbb.invalidate();

                    BehindDialSpeed.setText((houbbbb.currentHighlightedIndex+1)+"spr");
                }
            }
        });
        //档位“+”
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleHelper.sendCommand(bluetoothGatt, "0D0600000FE6",true);

                if (houbbbb.currentHighlightedIndex < 11) {
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] - 10;
                    houbbbb.currentHighlightedIndex++;
                    houbbbb.lineHeights[houbbbb.currentHighlightedIndex] = houbbbb.lineHeights[houbbbb.currentHighlightedIndex] + 10;
                    houbbbb.invalidate();
                    BehindDialSpeed.setText((houbbbb.currentHighlightedIndex+1)+"spr");
                }
            }
        });

        addbehindvale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behindDialNumber < 5) {
                    behindDialNumber++;
                    updateFrontValueText();
                    //BleHelper.sendCommand(bluetoothGatt, "2C060004E1C", true);
                }
            }
        });


        reducebehindvale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behindDialNumber > -5) {
                    behindDialNumber--;
                    updateFrontValueText();
                    //BleHelper.sendCommand(bluetoothGatt, "14060000E736", true);
                }
            }
        });

    }

    private void updateFrontValueText() {
        BehindValue.setText("当前微调值:"+behindDialNumber);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请保证后拨链条处于第七挡位！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HouboActivity.this, Houbojiaozhun.class);

                        startActivity(intent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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


