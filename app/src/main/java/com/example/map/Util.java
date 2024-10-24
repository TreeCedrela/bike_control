package com.example.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Util {

    //请求权限码
    public static final int REQUEST_PERMISSIONS = 9527;
    public static final int DefaultZoom = 20;

    //const intent value
    public static final String Latitudes = "latitudes";
    public static final String Longitudes = "longitudes";
    public static final String ElapsedTime = "elapsedTime";
    public static final String ExtraDATE = "extraDate";
    public static final String Altitude = "altitude";
    public static final String AverageSpeed = "average_speed";
    public static final String DistanceSum = "distance_sum";
    public static final String Latitude = "latitude";
    public static final String Longitude = "longitude";
    public static final String ImageURI = "imageUri";
    public static final String TimeDate = "timeDate";
    //status string
    public static final String Status = "Status";


    public Util() {

    }

    //Toast提示
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    //动态请求权限
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    public static boolean requestPermission(Activity activity) {
        String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(activity, permissions)) {
            //true 有权限 开始定位
            showMsg(activity, "已获得权限，可以定位啦！");
            return true;
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(activity, "需要权限", REQUEST_PERMISSIONS, permissions);
            showMsg(activity, "已获得权限，可以定位啦！");
            return false;
        }
    }


    //初始化地图
    public static void initMap(Bundle savedInstanceState, LocationSource locationSource, MapView mMapView, AMap aMap, LatLonPoint latLonPoint) {

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        MyLocationStyle myLocationStyle = new MyLocationStyle();

        //圆圈边框填充
        myLocationStyle.strokeWidth(1);
        myLocationStyle.radiusFillColor(0x0001);
        aMap.setMyLocationStyle(myLocationStyle);

        if (locationSource != null) {
            // 设置定位监听
            aMap.setLocationSource(locationSource);
        }

        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);

        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(DefaultZoom);
        if (latLonPoint != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), 18, 30, 0));
            aMap.moveCamera(cameraUpdate);
        } else {
            aMap.moveCamera(mCameraUpdate);
        }


    }


    //初始化定位
    public static void initLocation(AMapLocationClient mLocationClient, AMapLocationClientOption mLocationOption) {
        //初始化定位
        try {
            //设置定位回调监听

            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(true);
            mLocationOption.setSensorEnable(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);

        } catch (Exception e) {
            Log.e("initLocation", "error:", e);
        }
    }
}
