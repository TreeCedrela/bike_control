package com.example.autobike;

import static com.example.autobike.Util.Altitude;
import static com.example.autobike.Util.AverageSpeed;
import static com.example.autobike.Util.DistanceSum;
import static com.example.autobike.Util.ElapsedTime;
import static com.example.autobike.Util.ImageURI;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.example.map.R;

import java.util.ArrayList;
import java.util.List;


public class concreteActivity extends AppCompatActivity {

    private TextView VDistanceView;
    private TextView VAverageSpeed;
    private TextView VAltitude;
    private TextView VElapsedTime;
    private MapView mMapView;
    public AMap aMap = null;
    private Button backButton;

    @SuppressLint({"DefaultLocale", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_concrete);
//
//        backButton=findViewById(R.id.imageButton2);
//        backButton.setOnClickListener(v->{
//            Intent intent=new Intent(concreteActivity.this,recordActivity.class);
//            startActivity(intent);
//        });

        VAltitude = findViewById(R.id.concreteAltitude);
        VDistanceView = findViewById(R.id.concreteSumDistance);
        VAverageSpeed = findViewById(R.id.concreteAverageSpeed);
        VElapsedTime = findViewById(R.id.concreteElapsedTime);
//        VElapsedTime = findViewById(R.id.concreteElapsedTime);
        //TODO impl Map Image
        mMapView = findViewById(R.id.bmapView);
        mMapView.onCreate(savedInstanceState);
        String imageURI = getIntent().getStringExtra(ImageURI);

        float altitude = getIntent().getFloatExtra(Altitude, 0);
        float distanceSum = getIntent().getFloatExtra(DistanceSum, 0);
        float averageSpeed = getIntent().getFloatExtra(AverageSpeed, 0);
        float elapsedTime = getIntent().getFloatExtra(ElapsedTime, 0);


        VAltitude.setText(String.format("%4.2f", altitude));
        VDistanceView.setText(String.format("%4.2f", distanceSum));
        VAverageSpeed.setText(String.format("%4.2f", averageSpeed));
        VElapsedTime.setText(String.format("%4.2f", elapsedTime));






//定义了一个地图view
        MapView mapView = (MapView) findViewById(R.id.bmapView);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
//初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }



        // 添加轨迹绘制功能
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(39.9042, 116.4074)); // 北京
        points.add(new LatLng(39.2304, 116.4737)); // 上海
        points.add(new LatLng(39.5431, 116.0579)); // 深圳

        aMap.setOnMapLoadedListener(() -> {
            // 地图加载完成后，添加轨迹线
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(points)
                    .width(10)
                    .color(Color.parseColor("#11AEF7"));  // 设置轨迹线颜色
            aMap.addPolyline(polylineOptions);
        });

        // 将地图移动到轨迹可视范围内
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));



//        // 定义日期格式化器
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//        // 将日期格式化为字符串
//        String formattedDate = formatter.format("2023-11-22");


    }
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


}