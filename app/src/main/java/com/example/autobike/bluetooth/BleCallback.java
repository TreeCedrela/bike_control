package com.example.autobike.bluetooth;

import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.example.autobike.utils.BleConstant;
import com.example.autobike.utils.BleHelper;
import com.example.autobike.utils.ByteUtils;



public class BleCallback extends BluetoothGattCallback {
    private static final String TAG = BleCallback.class.getSimpleName();
    public static String buffer = "";
    public static String s = "";
    public static String content = "";
    public static String device_name = "";               //存储设备型号
    public static String app_edition = "";               //存储软件版本
    public static int front_speed1 = 0;              //存储前拨速别1对应的电机转动角度
    public static int front_speed2 = 0;              //存储前拨速别2对应的电机转动角度
    public static int front_speed3 = 0;              //存储前拨速别3对应的电机转动角度
    public static int current_front_speed = 0 ;          //存储当前速别的电机转动角度
    public static int current_rear_speed = 0 ;
    public static String crc = "" ;                          //存储CRC校验位


    /**
     * 连接状态改变回调
     *
     * @param gatt     gatt
     * @param status   gatt连接状态
     * @param newState 新状态
     */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        //Log.d(TAG,Thread.currentThread().getName());
        if (status == BluetoothGatt.GATT_SUCCESS) {
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED://连接成功
                    Log.d(TAG, "连接成功");
                    //获取MtuSize
                    //gatt.requestMtu(247);
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED://断开连接
                    Log.e(TAG, "断开连接");
                    break;
                default:
                    break;
            }
        } else {
            Log.e(TAG, "onConnectionStateChange: " + status);
        }
    }
    /**
     * Mtu改变回调
     *
     * @param gatt   gatt
     * @param mtu    new MTU size
     * @param status gatt状态
     */
//    @Override
//    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
//        Log.d(TAG, "onMtuChanged：mtu： " + mtu);
//        //发现服务
//        gatt.discoverServices();
//    }
    /**
     * 发现服务回调
     *
     * @param gatt   gatt
     * @param status gatt状态
     */
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.d(TAG, "onServicesDiscovered");

        boolean notifyOpen = BleHelper.enableIndicateNotification(gatt);

        if (!notifyOpen) {
            Log.e(TAG, "开启通知属性异常");
            gatt.disconnect();
        }

    }
    /**
     * 描述符写入回调
     *
     * @param gatt       gatt
     * @param descriptor 描述符
     * @param status     gatt状态
     */
    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (BleConstant.ENABLE_NOTIFICATION_UUID.equals(descriptor.getUuid().toString().toLowerCase())) {
            if (status == GATT_SUCCESS) {
                Log.d(TAG, "onDescriptorWrite: 通知开启成功");
                //1.发装置型号
                BleHelper.sendCommand(gatt, "20030000000000000000000092A3",true);
                BleHelper.sendCommand(gatt, "3A0300000000000000000000B8B1",true);
                //2.软件版本
                BleHelper.sendCommand(gatt, "21030000000000000000000091D6",true);
                BleHelper.sendCommand(gatt, "3B0300000000000000000000BBC4",true);
            } else {
                Log.d(TAG, "onDescriptorWrite: 通知开启失败");
            }
        }
    }

    /**
     * 特性读取回调
     *
     * @param gatt           gatt
     * @param characteristic 特性
     * @param status         gatt状态
     */
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "当前特性: " + characteristic.getUuid().toString());
    }

    /**
     * 特性写入回调
     *
     * @param gatt           gatt
     * @param characteristic 特性
     * @param status         gatt状态
     */
    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        String command = ByteUtils.bytesToHexString(characteristic.getValue());
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "onCharacteristicWrite: 写入成功：" + command);
        } else {
            Log.d(TAG, "onCharacteristicWrite: 写入失败：" + command);
        }

        //读取特性
        Log.d(TAG, "onCharacteristicChanged: 读取特性 " + gatt.readCharacteristic(characteristic));
    }

    /**
     * 特性改变回调
     *
     * @param gatt           gatt
     * @param characteristic 特性
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        content = ByteUtils.bytesToHexString(characteristic.getValue());

        Log.d(TAG, "onCharacteristicChanged: 收到内容：" + content);
        //1.设备型号
        if(content.substring(0,2).equals("20") ){
            buffer = "";
            s=content.substring(4,24);
            buffer = buffer + s;
        }
        else if(content.substring(0,2).equals("3a")){
            s=content.substring(4,24);
            buffer = buffer + s;
            device_name = hexToAscii(s);
        }
        //2.软件版本
        if(content.substring(0,2).equals("21") ){
            buffer = "";
            s=content.substring(4,24);
            buffer = buffer + s;
        }
        else if(content.substring(0,2).equals("3B")){
            s=content.substring(4,24);
            buffer = buffer + s;
            app_edition = hexToAscii(s);
        }

        //3.当前前拨速别
        if(content.substring(0,2).equals("15") ){
            buffer = "";
            s=content.substring(4,6);
            buffer = buffer + s;
            current_front_speed = Integer.parseInt(buffer,16);
        }
        //4.当前后拨速别
        if(content.substring(0,2).equals("16") ){
            buffer = "";
            s=content.substring(4,6);
            buffer = buffer + s;
            current_rear_speed = Integer.parseInt(buffer,16);
        }



        Log.d(TAG, "buffer：" + buffer);
        Log.d(TAG, "onCharacteristicChanged: 读取特性 " + gatt.readCharacteristic(characteristic));
    }

    //将16进制转成对应的ascll码
    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
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


