package com.example.autobike;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceItem;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.example.map.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class continueActivity extends AppCompatActivity implements AMapLocationListener,LocationSource, DistanceSearch.OnDistanceSearchListener {
    public MapView mMapView = null;
    public AMap aMap =null;
    public LocationSource.OnLocationChangedListener mListener=null;

    //外部学习所加

    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    public LatLonPoint currentPoint;

    public DistanceSearch distanceSearch;
    public List<LatLonPoint> latLonPoints;

    public TextView hour_speed;
    public TextView distanceT;

    // 在类的成员变量区域
    private Polyline polyline;
    private final List<LatLng> pathPoints = new ArrayList<>();


    private long startTime;
    private long elapsedTime;
    private long endTime;
    private TextView timerTextView;

    private  Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        hour_speed=findViewById(R.id.hour_speed);

        // 绑定TextView用于显示计时器
        timerTextView = findViewById(R.id.sport_time);



        // 启动时开始计时
        startTime = System.currentTimeMillis();
        timerHandler.post(timerRunnable); // 开始计时

        Log.d("BeginActivity", "hour_speed 和 timerTextView 已初始化");

        // 获取当天日期
        Calendar calendar = Calendar.getInstance();

        // 定义日期格式化器
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // 将日期格式化为字符串
        String formattedDate = formatter.format(calendar.getTime());


        SeekBar seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if(seekBar.getProgress()==seekBar.getMax()){

                    // 停止拖动时停止计时
                    endTime = System.currentTimeMillis();
                    timerHandler.removeCallbacks(timerRunnable); // 停止计时
                    elapsedTime = endTime - startTime; // 计算总耗时

                    Intent intent=new Intent(continueActivity.this,overActivity.class);

                    double[] latitudes = new double[pathPoints.size()];
                    double[] longitudes = new double[pathPoints.size()];

                    for (int i = 0; i < pathPoints.size(); i++) {
                        latitudes[i] = pathPoints.get(i).latitude;
                        longitudes[i] = pathPoints.get(i).longitude;
                    }

                    intent.putExtra("latitudes", latitudes);
                    intent.putExtra("longitudes", longitudes);

                    // 将计时结果传递给OverActivity
                    intent.putExtra("elapsedTime", elapsedTime);

                    // 将格式化后的日期传递给OverActivity
                    intent.putExtra("EXTRA_DATE", formattedDate);
                    startActivity(intent);
                }

            }
        });

        try {
            initLocation();
        } catch (AMapException e) {
            e.printStackTrace();
        }
        requestPermission();
        initMap(savedInstanceState);
        mLocationClient.startLocation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图

        //timerHandler.removeCallbacks(timerRunnable); // 停止计时器更新

        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (polyline != null) {
            polyline.remove();
        }
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

    private  Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            // 计算经过的时间
            elapsedTime = System.currentTimeMillis() - startTime;

            // 转换为秒显示
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            // 更新TextView
            timerTextView.setText(String.format("%02d:%02d", minutes, seconds));

            // 再次调用自身以继续计时
            timerHandler.postDelayed(this, 1000);
            Log.d("BeginActivity", "hour_speed 和 timerTextView 已初始化");
        }
    };



    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                String address = aMapLocation.getAddress();
//                tvContent.setText(address == null?"无地址":address);
                //纬度
                double latitude = aMapLocation.getLatitude();
                //经度
                double longitude = aMapLocation.getLongitude();
                //速度
                final float speed = aMapLocation.getSpeed();

                hour_speed=findViewById(R.id.hour_speed);
                hour_speed.setText(String.format("%.2f",speed*3.6));


                LatLonPoint dstPoint = new LatLonPoint(latitude, longitude);

                //以下计算距离前的预配置
                //设置当前为起点
                currentPoint= new LatLonPoint(latitude,longitude);
                final DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
                latLonPoints.add(currentPoint);
                distanceQuery.setOrigins(latLonPoints);
                distanceQuery.setDestination(dstPoint);
                distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
                distanceSearch.calculateRouteDistanceAsyn(distanceQuery);



                StringBuilder stringBuffer=new StringBuilder();
                stringBuffer.append("纬度:").append(latitude).append("\n");
                stringBuffer.append("经度:").append(longitude).append("\n");
                stringBuffer.append("地址:").append(address).append("\n");

                Log.d("MainActivity",stringBuffer.toString());
//                showMsg(address);
                showMsg("速度"+speed);

                LatLng currentLatLng = new LatLng(latitude, longitude);

                // 添加当前位置到路径点列表
                pathPoints.add(currentLatLng);

                // 更新 Polyline 的点
                polyline.setPoints(pathPoints);

                if (mListener!=null){
                    mListener.onLocationChanged(aMapLocation);
                }

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
    /**
     * Toast提示
     * @param msg 提示内容
     */
    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位
            showMsg("已获得权限，可以定位啦！");
            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 初始化定位
     */
    private void initLocation() throws AMapException {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(10000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(true);
            mLocationOption.setSensorEnable(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            distanceSearch=new DistanceSearch(this);
            distanceSearch.setDistanceSearchListener(this);
            latLonPoints = new ArrayList<LatLonPoint>();

        }
    }
    /**
     * 初始化地图
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mMapView.getMap();

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);

        polyline = aMap.addPolyline(new PolylineOptions()
                .color(Color.BLUE)   // 轨迹线的颜色
                .width(10f)          // 轨迹线的宽度
                .geodesic(true));    // 使用大地曲线来计算最短路径
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient != null) {
            mLocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
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
        List<DistanceItem> distanceResults = distanceResult.getDistanceResults();
        if (!distanceResults.isEmpty()) {
            DistanceItem distanceItem = distanceResults.get(0);
            float distance = distanceItem.getDistance();
            distanceT.setText(String.format("%.2f 米", distance));  // 将距离显示在 distanceT 文本框中
            Log.d("Distance", "Distance: " + distance + " meters");
        }
    }
}
