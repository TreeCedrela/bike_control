package com.example.autobike;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;

public class Utill {

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


    //Toast提示
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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


        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(DefaultZoom);
        if (latLonPoint != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), 18, 30, 0));
            aMap.moveCamera(cameraUpdate);
        } else {
            aMap.moveCamera(mCameraUpdate);
        }


    }

}
