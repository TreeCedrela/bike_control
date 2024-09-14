package com.example.autobike;

import static com.example.autobike.Util.Altitude;
import static com.example.autobike.Util.AverageSpeed;
import static com.example.autobike.Util.DistanceSum;
import static com.example.autobike.Util.ElapsedTime;
import static com.example.autobike.Util.ImageURI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amap.api.maps.MapView;
import com.example.map.R;

public class concreteActivity extends AppCompatActivity {

    private TextView VDistanceView;
    private TextView VAverageSpeed;
    private TextView VAltitude;
    private TextView VElapsedTime;
    private MapView VMapImage;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_concrete);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        VAltitude = findViewById(R.id.concreteAltitude);
        VDistanceView = findViewById(R.id.concreteSumDistance);
        VAverageSpeed = findViewById(R.id.concreteAverageSpeed);
        VElapsedTime = findViewById(R.id.concreteElapsedTime);
//        VElapsedTime = findViewById(R.id.concreteElapsedTime);
        //TODO impl Map Image
        VMapImage = findViewById(R.id.bmapView);
        String imageURI = getIntent().getStringExtra(ImageURI);

        float altitude = getIntent().getFloatExtra(Altitude, 0);
        float distanceSum = getIntent().getFloatExtra(DistanceSum, 0);
        float averageSpeed = getIntent().getFloatExtra(AverageSpeed, 0);
        float elapsedTime = getIntent().getFloatExtra(ElapsedTime, 0);


        VAltitude.setText(String.format("%4.2f", altitude));
        VDistanceView.setText(String.format("%4.2f", distanceSum));
        VAverageSpeed.setText(String.format("%4.2f", averageSpeed));
        VElapsedTime.setText(String.format("%4.2f", elapsedTime));


//        // 定义日期格式化器
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//        // 将日期格式化为字符串
//        String formattedDate = formatter.format("2023-11-22");


    }


}