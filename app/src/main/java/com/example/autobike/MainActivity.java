package com.example.autobike;

import static com.example.autobike.Util.initLocation;
import static com.example.autobike.Util.initMap;
import static com.example.autobike.Util.requestPermission;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.route.DistanceItem;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.example.map.R;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements AMapLocationListener, LocationSource, DistanceSearch.OnDistanceSearchListener {

    public MapView mMapView = null;

    public OnLocationChangedListener mListener = null;

    public AMap aMap = null;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button beginBt = findViewById(R.id.startButton);
        Button continueLastTimeBt = findViewById(R.id.continueLastTime);
        Button sportRecordBt = findViewById(R.id.sport_record_button);

        // TODO: 2024/9/7 feat blueprint view jump
        Button blueprintBt = findViewById(R.id.blueprint_button);

        beginBt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, beginActivity.class);
            startActivity(intent);
        });

        continueLastTimeBt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, continueActivity.class);
            startActivity(intent);
        });

        sportRecordBt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, recordActivity.class);
            startActivity(intent);
        });

        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);
            initLocation(mLocationClient, mLocationOption);
        } catch (AMapException e) {
            Log.e("onCreate", "error:", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        requestPermission(this);

        mLocationClient.startLocation();

        mMapView = findViewById(R.id.map);

        aMap = mMapView.getMap();

        initMap(savedInstanceState, this, mMapView, aMap,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    //接收异步返回的定位结果
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                String address = aMapLocation.getAddress();
                //纬度
                double latitude = aMapLocation.getLatitude();
                //经度
                double longitude = aMapLocation.getLongitude();
                //速度
                final float speed = aMapLocation.getSpeed();

                String locatedInfo = "纬度:" + latitude + "\n" + "经度:" + longitude + "\n" + "地址:" + address + "\n";

                Log.d("MainActivity", locatedInfo);

                if (mListener != null) {
                    mListener.onLocationChanged(aMapLocation);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 请求权限结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient != null) {
            mLocationClient.startLocation();//启动定位
        }
    }


    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onDistanceSearched(DistanceResult distanceResult, int i) {
        final List<DistanceItem> distanceResults = distanceResult.getDistanceResults();
        final DistanceItem distanceItem = distanceResults.get(0);
        final float distance = distanceItem.getDistance();
        final float duration = distanceItem.getDuration();
        Log.d("Distance and duration:", distance + "::" + duration);
    }
}
