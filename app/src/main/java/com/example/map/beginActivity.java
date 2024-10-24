package com.example.map;

import static com.example.map.Util.Altitude;
import static com.example.map.Util.AverageSpeed;
import static com.example.map.Util.DistanceSum;
import static com.example.map.Util.ElapsedTime;
import static com.example.map.Util.ExtraDATE;
import static com.example.map.Util.Latitude;
import static com.example.map.Util.Latitudes;
import static com.example.map.Util.Longitude;
import static com.example.map.Util.Longitudes;
import static com.example.map.Util.initLocation;
import static com.example.map.Util.initMap;
import static com.example.map.Util.requestPermission;
import static com.example.map.Util.showMsg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.amap.api.services.route.DistanceSearch;
import com.example.map.database.RecordDBHelper;
import com.example.map.entity.SportRecord;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class beginActivity extends AppCompatActivity implements AMapLocationListener, LocationSource {

    private static final LatLonPoint BeiJingTianAnMen = new LatLonPoint(39.908822999999984, 116.39747);

    public final List<LatLng> pathPoints = new ArrayList<>();
    public final Handler timerHandler = new Handler();
    public MapView mMapView = null;
    public OnLocationChangedListener mListener = null;

    public AMapLocationClient mLocationClient = null;

    public AMapLocationClientOption mLocationOption = null;
    public AMap aMap = null;

    public TextView hour_speed;
    public TextView average_speed;

    public TextView distance;
    public float distance_sum;

    public List<LatLonPoint> latLonPoints;
    public DistanceSearch distanceSearch;
    public LatLonPoint prePoint;
    float avg_speed;

    //record max average speed and judge valid speed
    public float maxHourSpeed;
    private Polyline polyline;
    private long startTime;
    private long elapsedTime;
    private long endTime;
    private TextView timerTextView;
    private final Runnable timerRunnable = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            // 计算经过的时间
            elapsedTime = System.currentTimeMillis() - startTime;

            // 转换为秒显示
            int seconds = (int) (elapsedTime / 1000);
            avg_speed = distance_sum / ((float) seconds / 3600);
            if (seconds < 1) {
                average_speed.setText(String.format("%4.2f", 0.00));
            } else {
                average_speed.setText(String.format("%4.2f", avg_speed));
            }

            int minutes = seconds / 60;
            int seconds_temp = seconds % 60;

            // 更新TextView
            timerTextView.setText(String.format("%02d:%02d", minutes, seconds_temp));

            // 再次调用自身以继续计时
            timerHandler.postDelayed(this, 1000);
            Log.d("BeginActivity", "hour_speed 和 timerTextView 已初始化");
        }
    };
    private double StartAltitude;
    private double LastAltitude;

    private RecordDBHelper recordDBHelper;
    private SportRecord sportRecord;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

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
        distance = findViewById(R.id.distance);
        mMapView = findViewById(R.id.map);

        aMap = mMapView.getMap();

        sportRecord=new SportRecord("pic/"+LocalDate.now().toString(),0,0,0,0, LocalDate.now(),0);

        recordDBHelper=RecordDBHelper.getInstance(this);
        recordDBHelper.InsertRecord(sportRecord);

        initMap(savedInstanceState, this, mMapView, aMap, null);

        try {
            distanceSearch = new DistanceSearch(this);
            distanceSearch.setDistanceSearchListener((distanceResult, errorCode) -> {
                if (errorCode == 1000) {
                    List<DistanceItem> distanceResults = distanceResult.getDistanceResults();
                    if (!distanceResults.isEmpty()) {

                        DistanceItem distanceItem = distanceResults.get(0);
                        //distance_sum 单位为km
                        float itemDistance = distanceItem.getDistance();

                        //if item distance per second bigger than average speed cut it to be average speed
                        float dist=itemDistance / 1000.0f;
                        distance_sum += Math.min(dist, maxHourSpeed); // 将距离的单位转换为公里并累加


                        distance.setText(String.format("%4.2f ", distance_sum));  // 将距离显示在 distanceT 文本框中
                        Log.d("Distance", "Distance: " + distance_sum + " meters");

                        //make record update
                        sportRecord.setAltitude((float) (LastAltitude-StartAltitude));
                        sportRecord.setDistanceSum(distance_sum);
                        sportRecord.setElapsedTime(elapsedTime);
                        sportRecord.setTimeDate(LocalDate.now());
                        sportRecord.setAverageSpeed(avg_speed);
                        // update
                        recordDBHelper.UpdateRecord(sportRecord);
                    } else {
                        Log.i("BeginActivity", "onDistanceSearched: no distance,try move");
                    }
                } else {
                    Log.e("BeginActivity", "distanceSearch errorCode: " + errorCode);
                }
            });
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }

        polyline = aMap.addPolyline(new PolylineOptions().color(Color.parseColor("#11AEF7"))   // 轨迹线的颜色
                .width(10f)          // 轨迹线的宽度
                .geodesic(true));    // 使用大地曲线来计算最短路径


        hour_speed = findViewById(R.id.hour_speed);
        average_speed = findViewById(R.id.average_speed);

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

                // 如果进度超过 80%，强制将进度设置为 80
                if (progress > 90) {
                    seekBar.setProgress(90); // 强制进度最大为80%
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() == 90) {

                    // 停止拖动时停止计时
                    endTime = System.currentTimeMillis();
                    timerHandler.removeCallbacks(timerRunnable); // 停止计时
                    elapsedTime = endTime - startTime; // 计算总耗时

                    Intent intent = new Intent(beginActivity.this, overActivity.class);
                    double[] latitudes = new double[pathPoints.size()];
                    double[] longitudes = new double[pathPoints.size()];

                    for (int i = 0; i < pathPoints.size(); i++) {
                        latitudes[i] = pathPoints.get(i).latitude;
                        longitudes[i] = pathPoints.get(i).longitude;
                    }


                    //轨迹图
                    intent.putExtra(Latitudes, latitudes);
                    intent.putExtra(Longitudes, longitudes);
                    // 将计时结果传递给OverActivity
                    intent.putExtra(ElapsedTime, elapsedTime);
                    // 将格式化后的日期传递给OverActivity
                    intent.putExtra(ExtraDATE, formattedDate);
                    double distance = LastAltitude - StartAltitude;
                    //海拔爬升值
                    intent.putExtra(Altitude, distance);
                    //平均速度
                    intent.putExtra(AverageSpeed, avg_speed);
                    //里程
                    intent.putExtra(DistanceSum, distance_sum);
                    //经纬度坐标
                    intent.putExtra(Latitude, prePoint.getLatitude());
                    intent.putExtra(Longitude, prePoint.getLongitude());

                    // update record as over status
                    sportRecord.setStatus(1);
                    sportRecord.setElapsedTime(elapsedTime);
                    sportRecord.setDistanceSum(distance_sum);
                    sportRecord.setAverageSpeed(avg_speed);
                    sportRecord.setAltitude((float) distance);
                    //update
                    recordDBHelper.UpdateRecord(sportRecord);

                    startActivity(intent);
                    onDestroy();
                } else {
                    //reset
                    seekBar.setProgress(0);
                }

            }
        });


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


    //接收异步返回的定位结果
    @SuppressLint("DefaultLocale")
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

                if (StartAltitude == 0) {
                    StartAltitude = aMapLocation.getAltitude();
                }
                LastAltitude = aMapLocation.getAltitude();
                //速度
                float speed= (float) (aMapLocation.getSpeed() * 3.6);
                maxHourSpeed=Math.max(speed,maxHourSpeed);

                hour_speed = findViewById(R.id.hour_speed);
                hour_speed.setText(String.format("%.2f",speed));


                if (prePoint == null) {
                    latLonPoints = new ArrayList<>();
                    prePoint = new LatLonPoint(latitude, longitude);
                    latLonPoints.add(prePoint);
                } else {
                    // 每次更新将上一个点和当前点加入查询
                    //latLonPoints.clear();
                    latLonPoints.add(prePoint);

                }
                final DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
                distanceQuery.setOrigins(latLonPoints);
                distanceQuery.setDestination(new LatLonPoint(latitude, longitude));

                distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
                distanceSearch.calculateRouteDistanceAsyn(distanceQuery);


                prePoint.setLatitude(latitude);
                prePoint.setLongitude(longitude);

                showMsg(this, "速度" + speed);

                LatLng currentLatLng = new LatLng(latitude, longitude);

                // 添加当前位置到路径点列表
                pathPoints.add(currentLatLng);

                // 更新 Polyline 的点
                polyline.setPoints(pathPoints);

                if (mListener != null) {
                    mListener.onLocationChanged(aMapLocation);
                }

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("mapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }


    //请求权限结果
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
}
