package com.example.autobike;

import static com.example.autobike.Utill.Altitude;
import static com.example.autobike.Utill.AverageSpeed;
import static com.example.autobike.Utill.DistanceSum;
import static com.example.autobike.Utill.ElapsedTime;
import static com.example.autobike.Utill.ExtraDATE;
import static com.example.autobike.Utill.Latitude;
import static com.example.autobike.Utill.Longitude;
import static com.example.autobike.Utill.initMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.example.map.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class overActivity extends AppCompatActivity {

    private MapView mMapView;
    private Polyline polyline;
    private final List<LatLng> pathPoints = new ArrayList<>();

    @SuppressLint({"DefaultLocale", "SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        // 接收计时结果
        long elapsedTime = getIntent().getLongExtra(ElapsedTime, 0);

        // 从Intent中获取传递过来的日期
        String formattedDate = getIntent().getStringExtra(ExtraDATE);

        float latitude=getIntent().getFloatExtra(Latitude,0);
        float longitude=getIntent().getFloatExtra(Longitude,0);

        float distanceSum = getIntent().getFloatExtra(DistanceSum, 0);
        float averageSpeed = getIntent().getFloatExtra(AverageSpeed, 0);
        float altitude = getIntent().getFloatExtra(Altitude, 0);

        TextView timeView = findViewById(R.id.textView9);
        TextView VAverageSpeed = findViewById(R.id.average_speed);
        TextView dateTextView = findViewById(R.id.timeDate);
        TextView VAltitude = findViewById(R.id.altitude);
        TextView VDistanceSum = findViewById(R.id.distanceSum);

        ImageButton cross = findViewById(R.id.imageButton4);

        cross.setOnClickListener(v->{
            Intent intent =new Intent(overActivity.this,MapActivity.class);
            startActivity(intent);
        });

        VAverageSpeed.setText(String.format("%4.2f",averageSpeed));
        VAltitude.setText(String.format("%4.2f",altitude));
        VDistanceSum.setText(String.format("%4.2f",distanceSum));

        int seconds= (int)(elapsedTime/1000);
        int minutes = seconds / 60;
        int seconds_temp = seconds % 60;

        // 更新TextView
        timeView.setText(String.format("%02d:%02d", minutes, seconds_temp));


        // 假设有一个TextView来显示日期
        dateTextView.setText(formattedDate+" 骑行");

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        AMap aMap = mMapView.getMap();

        initMap(savedInstanceState,null,mMapView, aMap,new LatLonPoint(latitude,longitude));

        // 初始化 Polyline
        polyline = aMap.addPolyline(new PolylineOptions()
                .color(Color.parseColor("#11AEF7"))
                .width(10f)
                .geodesic(true));

        // 从 Intent 中接收轨迹数据
        Intent intent = getIntent();
        double[] latitudes = intent.getDoubleArrayExtra("latitudes");
        double[] longitudes = intent.getDoubleArrayExtra("longitudes");

        if (latitudes != null && longitudes != null) {
            for (int i = 0; i < latitudes.length; i++) {
                pathPoints.add(new LatLng(latitudes[i], longitudes[i]));
            }
        }

        // 回放轨迹
        replayPath();
    }
    private void replayPath() {
        if (pathPoints == null || pathPoints.isEmpty()) {
            Toast.makeText(this, "No path to replay", Toast.LENGTH_SHORT).show();
            return;

        }

        // 清除现有轨迹线
        polyline.setPoints(new ArrayList<>());

        // 计算路径点的LatLngBounds，以确保轨迹路径在可视范围内
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng point : pathPoints) {
            boundsBuilder.include(point);
        }
        LatLngBounds bounds = boundsBuilder.build();

        // 将地图移动到路径的可视范围内
        mMapView.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // 100 是边距，可以根据需要调整

        Handler handler = new Handler();
        final int[] index = {0};

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (index[0] < pathPoints.size()) {
                    polyline.setPoints(pathPoints.subList(0, index[0] + 1));
                    index[0]++;
                    handler.postDelayed(this, 50);
                }
                else {
                    // 轨迹回放完成后进行截图
                    takeMapScreenshot();
                }
            }
        };

        handler.post(runnable);
    }

    private void takeMapScreenshot() {
        mMapView.getMap().getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                // 将截图保存到本地文件
                saveBitmapToFile(bitmap);
            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int status) {
                if (status == 0) {
                    // 截图成功
                    saveBitmapToFile(bitmap);
                } else {
                    // 截图失败
                    Toast.makeText(overActivity.this, "截图", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveBitmapToFile(Bitmap bitmap) {
        try {
            File file = new File(getExternalCacheDir(), "map_screenshot.png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            //如果是在当前界面继续操作，则如下：
//            showScreenshot(bitmap);

            // 将文件路径传递到下一个Activity
//            Intent intent = new Intent(overActivity.this, firstActivity.class);
//            intent.putExtra("screenshot_path", file.getAbsolutePath());
//           startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
           Toast.makeText(overActivity.this, "保存截图失败", Toast.LENGTH_SHORT).show();
        }
    }

//    private void showScreenshot(Bitmap bitmap) {
//        ImageView imageView = findViewById(R.id.screenshot_image);
//        imageView.setImageBitmap(bitmap);
//    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }
}
