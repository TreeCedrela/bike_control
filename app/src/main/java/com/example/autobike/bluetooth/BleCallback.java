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
    public static String buffer = "";
    public static String s = "";
    public static String content = "";
    private static final String TAG = BleCallback.class.getSimpleName();

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
                BleHelper.sendCommand(gatt, "41",true);
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
        // ！！！！！！！！！
        // （buffer需清0）
        // ！！！！！！！！！
        if(content.substring(0,2).equals("20") ){
            buffer = "";
            s=content.substring(3,12);
            buffer = buffer + s;
        }
        else if(content.substring(0,2).equals("3a")){
            s=content.substring(3,12);
            buffer = buffer + s;
            convertAsciiToChar(s);
        }
        //2.软件版本
        // ！！！！！！！！！
        // （buffer需清0）
        // ！！！！！！！！！
        if(content.substring(0,2).equals("21") || content.substring(0,2).equals("3b")  ){
            s=buffer.substring(3,12);
            buffer = buffer + s;
            //convertAsciiToChar(s);
        }


        Log.d(TAG, "buffer：" + buffer);
        Log.d(TAG, "onCharacteristicChanged: 读取特性 " + gatt.readCharacteristic(characteristic));
    }

    // 将 String 中的 ASCII 码转换为对应的字符
    public static String convertAsciiToChar(String input) {
        StringBuilder result = new StringBuilder();

        // 每两个字符表示一个 ASCII 码，将其转为整数后，再转换为字符
        for (int i = 0; i < input.length(); i += 2) {
            String asciiCode = input.substring(i, i + 2);
            int charCode = Integer.parseInt(asciiCode);
            result.append((char) charCode);
        }

        return result.toString();
    }
}


